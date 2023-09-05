package isel.leic.pdm.battleship.activities.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import isel.leic.pdm.battleship.R
import isel.leic.pdm.battleship.ui.Background
import isel.leic.pdm.battleship.ui.TopBar
import isel.leic.pdm.battleship.ui.theme.BattleshipTheme

const val AboutScreenTag = "AboutScreen"

@Composable
fun AboutScreen(
    onEmailSend: () -> Unit = {},
    onBackRequested: () -> Unit = { },
    authors: List<Author> = emptyList()
) {
    BattleshipTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize().testTag(AboutScreenTag),
            backgroundColor = MaterialTheme.colors.background,
            topBar = { TopBar(onBackRequested = onBackRequested) }
        ) { innerPadding ->
            Background {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues = innerPadding)
                ) {
                    Authors(
                        onEmailSend = { onEmailSend() },
                        authors = authors
                    )
                }
            }
        }
    }
}

@Composable
fun Authors(
    onEmailSend: () -> Unit,
    authors: List<Author>
) {
    Column (
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 50.dp)
    ) {
        if(authors.isEmpty()) {
            Row {
                Text(
                    text = "No authors to show.",
                    color = MaterialTheme.colors.secondary,
                    style = MaterialTheme.typography.h6
                )
            }
        } else {
            authors.forEach {
                Row {
                    AuthorEntry(
                        author = it,
                        onClick = onEmailSend
                    )
                }
            }
        }
    }
}

@Composable
fun AuthorEntry(
    author: Author,
    onClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Image(
            painter = painterResource(id = R.drawable.user),
            contentDescription = null,
            modifier = Modifier
                .size(size = 80.dp),
        )
        Text(
            text = author.name,
            color = MaterialTheme.colors.secondary,
            style = MaterialTheme.typography.h5,
        )
        Text(
            text = author.number.toString(),
            color = MaterialTheme.colors.secondary,
            style = MaterialTheme.typography.h6
        )
        Icon(
            imageVector = Icons.Default.Email,
            contentDescription = null,
            tint = MaterialTheme.colors.secondary
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ShowNoAuthors() {
    AboutScreen()
}

@Preview(showBackground = true)
@Composable
fun ShowAuthors() {
    AboutScreen(
        authors = listOf(Author(number = 0, name = "Author", email = "AXXXXX@alunos.isel.pt"))
    )
}
