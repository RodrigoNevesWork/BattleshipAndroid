package isel.leic.pdm.battleship.activities.lobby

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import isel.leic.pdm.battleship.domain.Game
import isel.leic.pdm.battleship.http.model.*
import isel.leic.pdm.battleship.services.GameServiceInterface
import isel.leic.pdm.battleship.services.sse.EventBus
import isel.leic.pdm.battleship.services.sse.SseEventListener
import isel.leic.pdm.battleship.services.sse.models.SseEvent
import isel.leic.pdm.battleship.utils.ProblemJson
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Represents the current game state
 */
enum class GameState { IDLE, STARTING, STARTED }

class LobbyViewModel(
    private val gameService: GameServiceInterface
): ViewModel(), SseEventListener {

    private val _lobby = MutableStateFlow(MatchmakerOutputModel(false, 0))
    val lobby = _lobby.asStateFlow()

    private val _gameId = MutableStateFlow(GameIdOutputModel(0))
    val gameId = _gameId.asStateFlow()

    private var _state by mutableStateOf(GameState.IDLE)
    val state: GameState
        get() = _state

    private var _error by mutableStateOf<ProblemJson?>(null)
    val error : ProblemJson?
        get() = _error

    fun matchmaker(game: GameInputModel): Job =
        viewModelScope.launch {
            try {
                gameService.start(game).collect { event ->
                    _lobby.value = event
                    _state = when(event.created) {
                        true -> {
                            _gameId.value = GameIdOutputModel(event.id)
                            GameState.STARTED
                        }
                        false -> { GameState.STARTING }
                    }
                }
            } catch (e: Exception) {
                if (e is ProblemJson) _error = e
            }
        }


    init {
        EventBus.registerListener(this)
    }

    override fun onCleared() {
        EventBus.unregisterListener(this)
        super.onCleared()
    }

    override fun onEventReceived(eventData: SseEvent) {
        if (eventData is Game && state == GameState.STARTING) {
            _gameId.value = GameIdOutputModel(eventData.id)
            if (eventData.id != 0) _state = GameState.STARTED
        }
    }
}
