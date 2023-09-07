package isel.leic.pdm.battleship.activities.game

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import isel.leic.pdm.battleship.R
import isel.leic.pdm.battleship.activities.board.BoardBuilder
import isel.leic.pdm.battleship.domain.*
import isel.leic.pdm.battleship.http.model.ShotInputModel
import isel.leic.pdm.battleship.http.model.UserInfoOutputModel
import isel.leic.pdm.battleship.ui.Background
import isel.leic.pdm.battleship.ui.TopBar
import isel.leic.pdm.battleship.ui.theme.BattleshipTheme

data class GameScreenState(
    val game: Game? = null
)

@Composable
fun GameScreen(
    userInfo: UserInfoOutputModel,
    game: GameScreenState = GameScreenState(null),
    onShot: (gameId: Int, shot: ShotInputModel) -> Unit = { gameId, shot -> },
    timeToShoot: Int,
    onBackRequested: () -> Unit = { },
    onForfeitRequested: () -> Unit = { },
    toEndActivity: (isLocalPlayerVictorious: Boolean) -> Unit = { }
) {
    BattleshipTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            backgroundColor = MaterialTheme.colors.background,
            topBar = { TopBar(onBackRequested = onBackRequested) }
        ) { innerPadding ->
            Background {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues = innerPadding)
                ) {
                    if(game.game == null || game.game.state == Game.State.GAME_SETUP) {
                        Text(
                            text = stringResource(id = R.string.game_loading),
                            style = MaterialTheme.typography.h3,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colors.primary
                        )
                        Text(
                            text = stringResource(id = R.string.game_wating),
                            style = MaterialTheme.typography.h4,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colors.primary
                        )
                    } else {
                        val theGame = game.game

                        Text(
                            text = stringResource(id = R.string.game_play_time) + " " + timeToShoot,
                            style = MaterialTheme.typography.h2,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colors.primary
                        )
                        val isLocalPlayer = (theGame.state == Game.State.PLAYER_A_TURN && userInfo.id == theGame.playerA.userId) || (theGame.state == Game.State.PLAYER_B_TURN && userInfo.id == theGame.playerB.userId)
                        Text(
                            text = stringResource(id = if (isLocalPlayer) R.string.game_screen_local_player_turn else R.string.game_screen_enemy_player_turn),
                            style = MaterialTheme.typography.h4,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colors.primary
                        )

                        val selectedBoard = when(theGame.state) {
                            Game.State.PLAYER_A_TURN -> if(userInfo.id == theGame.playerA.userId) theGame.playerB.board.copy(grid = theGame.playerB.board.grid.hide()) else theGame.playerB.board
                            Game.State.PLAYER_B_TURN -> if(userInfo.id == theGame.playerB.userId) theGame.playerA.board.copy(grid = theGame.playerA.board.grid.hide()) else theGame.playerA.board
                            else -> theGame.playerA.board
                        }

                        BoardBuilder(
                            board = selectedBoard,
                            onTileSelected = {
                                onShot(theGame.id, ShotInputModel(it.toString()))
                            },
                            enabledClick = (theGame.state == Game.State.PLAYER_A_TURN && userInfo.id == theGame.playerA.userId) || (theGame.state == Game.State.PLAYER_B_TURN && userInfo.id == theGame.playerB.userId),
                            modifier = Modifier.weight(weight = 1.0f, fill = true)
                        )

                        Button(onClick = onForfeitRequested) {
                            Text(text = stringResource(id = R.string.game_forfeit))
                        }

                        if (theGame.state.isEnded) {
                            toEndActivity(
                                theGame.state == Game.State.PLAYER_A_WON && theGame.playerA.userId == userInfo.id ||
                                        theGame.state == Game.State.PLAYER_B_WON && theGame.playerB.userId == userInfo.id
                            )
                        }
                    }
                }
            }
        }
    }
}
/*
@Preview(showBackground = true)
@Composable
fun GamePreview() {
    GameScreen(
        game = GameScreenState(
            startGame(
                name = "Test",
                playerA = Player(1, 20, 1, Board(10)),
                playerB = Player(2, 20, 2, Board(10)),
                gameRule = GameRule(1, 10, 1, 20, 20, listOf())
            )
        )
    )
}

 */
