package isel.leic.pdm.battleship.activities.lobby

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import isel.leic.pdm.battleship.http.model.*
import isel.leic.pdm.battleship.services.GameServiceInterface
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
): ViewModel() {

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

    fun getGameByPlayer(playerId: Int): Job =
        viewModelScope.launch {
            try {
                gameService.getGameByPlayer(playerId).collect { event ->
                    _gameId.value = event
                    if(event.id != 0) _state = GameState.STARTED
                }
            } catch (e: Exception) {
                if (e is ProblemJson) {
                    when (e.status) {
                        404 -> getGameByPlayer(playerId)
                    }
                    _error = e
                }
            }
        }

    fun forfeit(forfeitModel: ForfeitInputModel): Job =
        viewModelScope.launch {
            try {
                gameService.forfeit(forfeitModel)
            } catch (e: Exception) {
                if (e is ProblemJson) _error = e
            }
        }
}
