package isel.leic.pdm.battleship.activities.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import isel.leic.pdm.battleship.R
import isel.leic.pdm.battleship.ui.Background
import isel.leic.pdm.battleship.ui.TopBar
import isel.leic.pdm.battleship.ui.theme.BattleshipTheme

@Composable
fun GameEndScreen(
    isLocalPlayerVictorious: Boolean = true,
    points: Int = 5,
    onEndRequested: () -> Unit = { },
    onBackRequested: () -> Unit = { }
) {
    BattleshipTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            backgroundColor = MaterialTheme.colors.background,
            topBar = { TopBar(onBackRequested = onBackRequested) }
        ) { innerPadding ->
            Background {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues = innerPadding)
                ) {
                    val text = stringResource(id = if(isLocalPlayerVictorious) R.string.game_screen_local_player_won else R.string.game_screen_enemy_player_won) + "\n" +
                            stringResource(id = if(isLocalPlayerVictorious) R.string.player_won_points else R.string.player_lost_points) + " " + points

                    Text(
                        text = text,
                        style = MaterialTheme.typography.h4,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colors.primary
                    )

                    Button(onClick = onEndRequested) {
                        Text(text = stringResource(id = R.string.game_end_button))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EndScreenPreview() {
    GameEndScreen()
}
