package isel.leic.pdm.battleship.activities.lobby

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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import isel.leic.pdm.battleship.R
import isel.leic.pdm.battleship.ui.Background
import isel.leic.pdm.battleship.ui.TopBar
import isel.leic.pdm.battleship.ui.theme.BattleshipTheme

const val LobbyScreenTag = "LobbyScreen"
const val ForfeitButtonTag = "ForfeitButton"

@Composable
fun LobbyScreen (
    onBackRequested: () -> Unit = { },
    waitingForPlayer: Boolean = false
) {
    BattleshipTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize().testTag(LobbyScreenTag),
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

                    Text(
                        text = if(!waitingForPlayer) stringResource(id = R.string.lobby_creation) else stringResource(id = R.string.waiting_lobby_dialog_title),
                        style = MaterialTheme.typography.h4,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colors.primary
                    )

                }
            }
        }
    }
}

