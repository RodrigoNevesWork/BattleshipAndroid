package isel.leic.pdm.battleship.activities.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import isel.leic.pdm.battleship.R
import isel.leic.pdm.battleship.http.model.UserOutputModel
import isel.leic.pdm.battleship.ui.Background
import isel.leic.pdm.battleship.ui.TopBar

@Composable
fun UserScreen(
    user: UserOutputModel?,
    onBackRequest: () -> Unit = { },
) {
    Scaffold(
        topBar = {
            TopBar(onBackRequested = onBackRequest)
        }
    ) { innerPadding ->
        Background {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.user),
                    contentDescription = "Default user image"
                )

                Text(
                    text = "ID: " + user!!.id,
                    color = MaterialTheme.colors.secondary,
                    style = MaterialTheme.typography.h3
                )

                Text(
                    text = stringResource(id = R.string.user_username) + " " + user.username,
                    color = MaterialTheme.colors.secondary,
                    style = MaterialTheme.typography.h4
                )

                Text(
                    text = stringResource(id = R.string.user_points) + " " + user.points,
                    color = MaterialTheme.colors.secondary,
                    style = MaterialTheme.typography.h4
                )

                Text(
                    text = stringResource(id = R.string.user_games_played) + " " + user.gamesPlayed,
                    color = MaterialTheme.colors.secondary,
                    style = MaterialTheme.typography.h4
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewUserScreen() {
    UserScreen(user = UserOutputModel(id = 0, username = "Placeholder", points = 0, gamesPlayed = 0))
}
        