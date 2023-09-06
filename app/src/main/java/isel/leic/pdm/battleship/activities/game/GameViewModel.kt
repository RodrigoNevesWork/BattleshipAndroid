package isel.leic.pdm.battleship.activities.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import isel.leic.pdm.battleship.activities.lobby.GameState
import isel.leic.pdm.battleship.domain.Game
import isel.leic.pdm.battleship.http.model.*
import isel.leic.pdm.battleship.services.GameServiceInterface
import isel.leic.pdm.battleship.services.sse.EventBus
import isel.leic.pdm.battleship.services.sse.SseEventListener
import isel.leic.pdm.battleship.services.sse.models.SseEvent
import isel.leic.pdm.battleship.utils.ProblemJson
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameViewModel(
    private val gameService: GameServiceInterface
): ViewModel(), SseEventListener {

    companion object {
        private const val MAX_SHOOT_TIME = 120
        private const val ONE_SECOND_DELAY = 1000L
    }

    private val _onGoingGame = MutableStateFlow<Game?>(null)
    val onGoingGame
        get() = _onGoingGame.asStateFlow()

    private var _state = MutableStateFlow(GameState.IDLE)
    val state
        get() = _state.asStateFlow()

    private val _otherPlaced = MutableStateFlow(false)
    val otherPlaced
        get() = _otherPlaced.asStateFlow()

    private val _winner = MutableStateFlow<Int?>(null)
    val winner
        get() = _winner.asStateFlow()

    private val _timer = MutableStateFlow<Job?>(null)
    val timer
        get() = _timer.asStateFlow()

    private val _time = MutableStateFlow(20)
    val time
        get() = _time.asStateFlow()


    private var _error by mutableStateOf<ProblemJson?>(null)
    val error : ProblemJson?
        get() = _error

    fun getGame(gameId: Int): Job =
        viewModelScope.launch {
            try {
                gameService.getGameById(gameId).collect { event ->
                    _onGoingGame.value = event
                }
            } catch (e: Exception) {
                if (e is ProblemJson) _error = e
            }
        }

    fun placeShips(gameId: Int, shipsToPlace: List<PlaceShipInputModel>): Job =
        viewModelScope.launch {
            try {
                gameService.placeAllShips(gameId, shipsToPlace).collect { event ->
                    _otherPlaced.value = event.otherPlaced
                }
            } catch (e: Exception) {
                if (e is ProblemJson) _error = e
            }
        }

    fun shotShip(gameId: Int, shot: ShotInputModel): Job =
        viewModelScope.launch {
            try {
                gameService.makePlay(gameId, shot).collect { event ->
                    gameService.getGameById(gameId).collect {
                        _onGoingGame.value = it
                    }
                }
            } catch (e: Exception) {
                if (e is ProblemJson) _error = e
            }

        }

    /**
     * Decrements the timer to shoot
     */
    fun decrementTimeToShoot(onFinish: () -> Unit = {} ) {
        resetTimeToShoot()
        _timer.value = viewModelScope.launch {
            while (_time.value > 0) {
                delay(ONE_SECOND_DELAY)
                _time.value--
            }
            onFinish()
        }
    }

    /**
     * After time to shoot is completed resets the timer
     */
    fun resetTimeToShoot() {
        _time.value = MAX_SHOOT_TIME
        timer.value?.cancel()
    }

    fun forfeit(forfeitModel: ForfeitInputModel): Job =
        viewModelScope.launch {
            gameService.forfeit(forfeitModel)
        }

    init {
        EventBus.registerListener(this)
    }

    override fun onCleared() {
        EventBus.unregisterListener(this)
        super.onCleared()
    }

    override fun onEventReceived(eventData: SseEvent) {
        if (eventData is Game) {
            _onGoingGame.value = eventData
            _state.value = GameState.STARTED
            decrementTimeToShoot()
        }
    }
}
