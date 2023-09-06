package isel.leic.pdm.battleship.domain

import android.os.Parcelable
import isel.leic.pdm.battleship.domain.board.Board
import isel.leic.pdm.battleship.domain.board.Square
import isel.leic.pdm.battleship.domain.board.coordinates.Coordinates
import isel.leic.pdm.battleship.domain.board.isMissShot
import isel.leic.pdm.battleship.domain.board.positionsAvailable
import isel.leic.pdm.battleship.domain.ship.Direction
import isel.leic.pdm.battleship.domain.ship.FleetComposition
import isel.leic.pdm.battleship.services.sse.models.SseEvent
import kotlinx.parcelize.Parcelize


/**
 * BattleShip Game information
 * [state], [playerA], [playerB]
 * @property state the turn changes with each play
 * @property playerA
 * @property playerB
 */
@Parcelize
class Game(
    val id: Int = 0,
    val name: String? = null,
    val state: State = State.GAME_SETUP,
    val playerA: Player = Player(),
    val playerB: Player = Player(),
    val gameRule: GameRule = GameRule(),
) : Parcelable, SseEvent() {

    enum class State {
        GAME_SETUP,
        PLAYER_A_TURN,
        PLAYER_B_TURN,
        PLAYER_A_WON,
        PLAYER_B_WON;

        val isEnded: Boolean
            get() = this == PLAYER_A_WON || this == PLAYER_B_WON

    }

    fun copy(
        id: Int = this.id,
        name: String? = this.name,
        state: State = this.state,
        playerA: Player = this.playerA,
        playerB: Player = this.playerB,
        gameRule: GameRule = this.gameRule,
    ) = Game(id, name, state, playerA, playerB, gameRule)
}

/**
 * Starts the game.
 * @return a game with the correct turn and the player A loaded Board.
 */
fun startGame(name:String, playerA: Player, playerB: Player, gameRule: GameRule): Game =
    Game(
        id = 0,
        name = name,
        state = Game.State.GAME_SETUP,
        playerA = playerA.copy(board = Board(gameRule.gridSize)),
        playerB = playerB.copy(board = Board(gameRule.gridSize)),
        gameRule = gameRule,
    )

/**
 * Attempt to place a ship on the board at the indicated position.
 * @return a new game if the place is valid or an error that identifies the problem.
 */
fun Game.placeShip(playerId: Int, ship: FleetComposition, at: Coordinates, direction: Direction): Game {
    val player = if (playerId == playerA.id) playerA else playerB
    val boardRes = player.board.placeShip(ship, at, direction)
    if (boardRes == null)
            throw when{
                player.board.fleet.countEqualShips(ship) >= ship.quantity -> PlayError.ExceededShips()
                player.board.grid.positionsAvailable(ship, at, direction).isEmpty() -> PlayError.InvalidCoordinates()
                else -> PlayError.Unexpected()
            }
    else {
        val newGame =
            if (player == playerA)
                copy(playerA = playerA.copy(board = boardRes))
            else
                copy(playerB = playerB.copy(board = boardRes))
        return newGame.tryGoToShootingPhase()
    }
}

fun Game.tryGoToShootingPhase(): Game =
        when {
            playerA.board.fleet.allShipsPlaced(gameRule) &&
                    playerB.board.fleet.allShipsPlaced(gameRule) -> copy(state = Game.State.PLAYER_A_TURN)
            else -> this
        }

/**
 * Attempt a shot on the board at the indicated position.
 * @return A new game if the shot is valid or an error that identifies the problem.
 */
fun Game.shotShip(playerId: Int, target: Coordinates): Game {

    if (state.isEnded) throw PlayError.GameOver()
    if (state == Game.State.GAME_SETUP) throw PlayError.GameNotStarted()

    val enemyPlayer = if (playerId == playerA.id) playerB else playerA

    val boardRes =
        if (playerId == playerA.id && state == Game.State.PLAYER_A_TURN || playerId == playerB.id && state == Game.State.PLAYER_B_TURN)
            enemyPlayer.board.shot(target)
        else
            throw PlayError.InvalidTurn()

    if (boardRes == null) {
        val squareState = enemyPlayer.board.grid.getState(target)
        val ship = enemyPlayer.board.fleet.getShipOrNull(target) ?: throw PlayError.NoShip()
        throw when {
            enemyPlayer.board.isShipSunk(ship) -> PlayError.ShipSunk()
            squareState != Square.State.WATER && squareState != Square.State.SHIP -> PlayError.SquareNotToShoot()
            else -> PlayError.Unexpected()
        }
    }
    else {
        val newGame =
            if (playerId == playerA.id)
                copy(playerB = playerB.copy(board = boardRes))
            else
                copy(playerA = playerA.copy(board = boardRes))
        val state =
            when {
                newGame.playerA.board.allShipSunks() -> Game.State.PLAYER_B_WON
                newGame.playerB.board.allShipSunks() -> Game.State.PLAYER_A_WON
                boardRes.grid.isMissShot(target) -> if (playerId == playerA.id) Game.State.PLAYER_B_TURN else Game.State.PLAYER_A_TURN
                else -> newGame.state
            }
        return newGame.copy(state = state)
    }

}

