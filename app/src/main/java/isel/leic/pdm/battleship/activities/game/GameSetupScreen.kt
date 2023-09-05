package isel.leic.pdm.battleship.activities.game

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import isel.leic.pdm.battleship.R
import isel.leic.pdm.battleship.activities.board.BoardBuilder
import isel.leic.pdm.battleship.domain.*
import isel.leic.pdm.battleship.domain.board.coordinates.Column
import isel.leic.pdm.battleship.domain.board.coordinates.Coordinates
import isel.leic.pdm.battleship.domain.board.coordinates.Row
import isel.leic.pdm.battleship.domain.ship.Direction
import isel.leic.pdm.battleship.http.model.PlaceShipInputModel
import isel.leic.pdm.battleship.http.model.UserCredentialsOutputModel
import isel.leic.pdm.battleship.http.model.toPlaceShipInputModel
import isel.leic.pdm.battleship.ui.Background
import isel.leic.pdm.battleship.ui.TopBar
import isel.leic.pdm.battleship.ui.theme.BattleshipTheme
import isel.leic.pdm.battleship.utils.CountDownTimer
import kotlin.math.abs

data class GameSetupScreenState(
    val game: Game? = null
)

// TODO: Solve issue with composition happening as the navigation happens
@Composable
fun GameSetupScreen(
    userInfo: UserCredentialsOutputModel,
    game: GameSetupScreenState = GameSetupScreenState(),
    onPlaceShips: (ships: List<PlaceShipInputModel>) -> Unit = { },
    onBackRequested: () -> Unit = { },
    onTimeEnd: () -> Unit = { }
) {
    BattleshipTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = { TopBar(onBackRequested = onBackRequested) },
            backgroundColor = MaterialTheme.colors.background
        ) { innerPadding ->
            Background {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(paddingValues = innerPadding)
                ) {
                    if(game.game == null) {
                        Text(
                            text = stringResource(id = R.string.game_not_started),
                            style = MaterialTheme.typography.h3,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colors.primary
                        )
                    } else {
                        var currentGame by remember { mutableStateOf(game.game) }
                        var pastGame by remember { mutableStateOf(currentGame) }

                        var direction by remember { mutableStateOf(Direction.VERTICAL) }

                        val mutableFleet by remember { mutableStateOf(currentGame.gameRule.totalShips().toMutableList()) }

                        var ship by remember { mutableStateOf(mutableFleet.removeFirstOrNull()) }
                        var time by remember { mutableStateOf(game.game.gameRule.timeToPlace) }

                        CountDownTimer(time = time, subTime = { time-- }, end = { onTimeEnd() })

                        val player = if (userInfo.id == pastGame.playerA.userId) pastGame.playerA else pastGame.playerB

                        Text(
                            text = stringResource(id = R.string.game_setup_time) + " " + time,
                            style = MaterialTheme.typography.h3,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colors.primary
                        )

                        Text(
                            text = stringResource(id = R.string.game_setup_ship_name) + " " + ship?.shipName + "\n"
                                    + stringResource(id = R.string.game_setup_ship_size) + " " + ship?.shipSize,
                            style = MaterialTheme.typography.h4,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colors.primary
                        )
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = {
                                    currentGame = pastGame
                                    ship = mutableFleet.removeFirstOrNull()
                                    if (ship == null)
                                        onPlaceShips(player.board.fleet.ships.map { it.toPlaceShipInputModel() })
                                },
                                enabled = pastGame != currentGame
                            ) {
                                Text(text = stringResource(id = R.string.game_setup_place_ship))
                            }

                            Spacer(modifier = Modifier.width(5.dp))

                            Button(onClick = { direction = direction.other }) {
                                Text(text = stringResource(id = R.string.game_setup_rotate_ship))
                            }
                        }

                        BoardBuilder(
                            board = player.board,
                            onTileSelected = {
                                try {
                                    pastGame = currentGame.placeShip(
                                        playerId = player.id,
                                        ship = ship!!,
                                        at = it,
                                        direction = direction
                                    )
                                } catch (err: PlayError) {
                                    if (err is PlayError.InvalidCoordinates) {
                                        // TODO: Solve issue with ship not showing up in some tiles when too close to another ship
                                        val temp = when (direction) {
                                            Direction.VERTICAL -> Coordinates(it.column, Row(it.row.value - abs(it.row.value + ship!!.shipSize - player.board.grid.side)))
                                            Direction.HORIZONTAL -> Coordinates(Column(it.column.value - abs(it.column.value + ship!!.shipSize - player.board.grid.side)), it.row)
                                        }
                                        pastGame = currentGame.placeShip(
                                            playerId = player.id,
                                            ship = ship!!,
                                            at = temp,
                                            direction = direction
                                        )
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
/*
@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun GameSetupPreview() {
    GameSetupScreen(
        userInfo = UserCredentialsOutputModel(0, "", ""),
        game = GameSetupScreenState(
            startGame(
                name = "Test",
                playerA = Player(1, Instant.ofEpochSecond(20), 1, Board(10)),
                playerB = Player(2, Instant.ofEpochSecond(20), 2, Board(10)),
                gameRule = GameRule(1, 10, 1, 20, 20, listOf(FleetComposition(1, "Destroyer", 2, 2)))
            )
        )
    )
}

 */
