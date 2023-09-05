package isel.leic.pdm.battleship.activities.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import isel.leic.pdm.battleship.domain.Game
import isel.leic.pdm.battleship.http.model.*
import isel.leic.pdm.battleship.services.GameServiceInterface
import isel.leic.pdm.battleship.utils.ProblemJson
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameViewModel(
    private val gameService: GameServiceInterface
): ViewModel() {

    private val _onGoingGame = MutableStateFlow<Game?>(null)
    val onGoingGame
        get() = _onGoingGame.asStateFlow()

    private val _placed = MutableStateFlow(false)
    val placed
        get() = _placed.asStateFlow()

    private val _winner = MutableStateFlow<Int?>(null)
    val winner
        get() = _winner.asStateFlow()

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
                    _placed.value = event.placed
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


    fun forfeit(forfeitModel: ForfeitInputModel): Job =
        viewModelScope.launch {
            gameService.forfeit(forfeitModel)
        }
}
