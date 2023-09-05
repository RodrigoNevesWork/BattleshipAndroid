package isel.leic.pdm.battleship.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import isel.leic.pdm.battleship.R
import isel.leic.pdm.battleship.domain.Game

const val GameEndedDialogTag = "GameEndedDialog"
const val GameEndedDialogDismissButtonTag = "GameEndedDialogDismissButton"

@Composable
fun GameEndedDialog(
    localPLayerId: Int,
    game: Game,
    onDismissRequested: () -> Unit = { }
) {

    val dialogTextId =
        if (game.state == Game.State.PLAYER_A_WON && game.playerA.id == localPLayerId ||
            game.state == Game.State.PLAYER_B_WON && game.playerB.id == localPLayerId)
            R.string.game_ended_dialog_text_local_won
        else R.string.game_ended_dialog_text_opponent_won

    AlertDialog(
        onDismissRequest = onDismissRequested,
        buttons = {
            Box(
                contentAlignment = Alignment.BottomEnd,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp)
            ) {
                OutlinedButton(
                    border = BorderStroke(0.dp, Color.Unspecified),
                    onClick = onDismissRequested,
                    modifier = Modifier.testTag(GameEndedDialogDismissButtonTag)
                ) {
                    Text(
                        text = stringResource(id = R.string.game_ended_ok_button),
                        fontSize = 14.sp
                    )
                }
            }
        },
        title = { Text(stringResource(id = R.string.game_ended_dialog_title)) },
        text = { Text(stringResource(id = dialogTextId)) },
        modifier = Modifier.testTag(GameEndedDialogTag)
    )
}
/*
@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
private fun MatchEndedDialogLocalWonPreview() {
    BattleshipTheme {
        GameEndedDialog(
            localPLayerId = 1,
            game = startGame(
                name = "Test",
                playerA = Player(1, Instant.ofEpochSecond(20), 1, Board(10)),
                playerB = Player(2, Instant.ofEpochSecond(20), 2, Board(10)),
                gameRule = GameRule(1, 10, 1, 20, 20, listOf())
            )
        )
    }
}


 */