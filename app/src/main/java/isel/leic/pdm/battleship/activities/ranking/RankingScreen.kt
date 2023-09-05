package isel.leic.pdm.battleship.activities.ranking

import android.view.KeyEvent.KEYCODE_ENTER
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import isel.leic.pdm.battleship.R
import isel.leic.pdm.battleship.http.model.RankingOutputModel
import isel.leic.pdm.battleship.http.model.SearchOutputModel
import isel.leic.pdm.battleship.http.model.UserOutputModel
import isel.leic.pdm.battleship.http.model.UserRankingOutputModel
import isel.leic.pdm.battleship.ui.Background
import isel.leic.pdm.battleship.ui.TopBar
import isel.leic.pdm.battleship.ui.theme.BattleshipTheme
import isel.leic.pdm.battleship.ui.theme.DarkBlue
import kotlin.math.min

data class RankingScreenState(
    val ranking: RankingOutputModel = RankingOutputModel(0, 0, emptyList())
)

data class RankingScreenSearchState(
    val ranking: SearchOutputModel = SearchOutputModel(emptyList())
)

@Composable
fun RankingScreen(
    state: RankingScreenState = RankingScreenState(),
    onBackRequest: () -> Unit = { },
    onMeClickRequest: () -> Unit = { },
    onSearchRequest: (username: String) -> Unit = { },
    search: RankingScreenSearchState = RankingScreenSearchState(),
    clearSearch: () -> Unit = { },
    onPersonClick: (UserOutputModel) -> Unit = { }
) {
    BattleshipTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            backgroundColor = MaterialTheme.colors.background,
            topBar = { TopBar(onBackRequested = onBackRequest) }
        ) { innerPadding ->
            Background {
                if(search.ranking.users.isNotEmpty()) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues = innerPadding)
                    ) {
                        Text(
                            text = stringResource(id = R.string.ranking_search_results),
                            color = Color.White,
                            style = MaterialTheme.typography.h4,
                            modifier = Modifier.padding(top = 10.dp, bottom = 0.dp)
                        )
                        UsersTable(users = search.ranking.users, modifier = Modifier.weight(weight = 1f, fill = false))
                    }
                    Column(
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(onClick = { clearSearch() }) {
                            Text(text = stringResource(id = R.string.ranking_back_button))
                        }
                    }
                } else {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues = innerPadding)
                    ) {
                        var typedUsername by remember { mutableStateOf(value = "") }

                        OutlinedTextField(
                            value = typedUsername,
                            onValueChange = { typedUsername = ensureInputBounds(it) },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Search,
                                    contentDescription = null
                                )
                            },
                            label = { Text(stringResource(id = R.string.ranking_search_placeholder)) },
                            maxLines = 3,
                            modifier = Modifier.onKeyEvent {
                                if(it.nativeKeyEvent.keyCode == KEYCODE_ENTER) {
                                    onSearchRequest(typedUsername)
                                    true
                                }
                                else false
                            }
                        )
                        Text(
                            text = stringResource(id = R.string.ranking),
                            color = Color.White,
                            style = MaterialTheme.typography.h4,
                            modifier = Modifier.padding(top = 10.dp, bottom = 0.dp)
                        )
                        Top3Table(users = state.ranking.users.take(3), onPersonClick = onPersonClick)
                        UsersTable(users = state.ranking.users.drop(3), modifier = Modifier.weight(weight = 1f, fill = false), onPersonClick = onPersonClick)
                    }
                    Column(
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(onClick = { onMeClickRequest() }) {
                            Text(text = stringResource(id = R.string.ranking_me_button))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Top3Table(
    users: List<UserRankingOutputModel>,
    onPersonClick: (UserOutputModel) -> Unit = { }
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, vertical = 50.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        items(users) {
            val image = when(it.rank) {
                1 -> R.drawable.first
                2 -> R.drawable.second
                else -> R.drawable.third
            }
            UserTopEntry(user = it, image = image, onPersonClick = onPersonClick)
        }
    }
}

@Composable
fun UserTopEntry(
    user: UserRankingOutputModel,
    image: Int,
    onPersonClick: (UserOutputModel) -> Unit = { }
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .padding(horizontal = 10.dp)
    ) {
        Image(
            painter = painterResource(id = image),
            contentDescription = null,
            modifier = Modifier
                .size(50.dp),
            alignment = Alignment.TopCenter
        )
        Text(
            text = user.user.username,
            color = Color.White,
            modifier = Modifier
                .padding(top = 8.dp)
                .clickable(enabled = true) {
                    onPersonClick(user.user)
            },
        )
        Text(
            text = user.user.points.toString(),
            color = Color.White,
            modifier = Modifier
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UsersTable(
    users: List<UserRankingOutputModel>,
    modifier: Modifier = Modifier,
    onPersonClick: (UserOutputModel) -> Unit = { }
) {

    val rankWeight = .2f
    val usernameWeight = .5f
    val pointsWeight = .3f

    LazyColumn(
        modifier = modifier
            .fillMaxSize(fraction = .8f)
            .padding(16.dp)
            .size(1.dp)
    ) {
        stickyHeader {
            Row(modifier = Modifier.background(DarkBlue)) {
                UserInfoEntry(text = stringResource(id = R.string.ranking_rank), weight = rankWeight)
                UserInfoEntry(text = stringResource(id = R.string.ranking_name), weight = usernameWeight)
                UserInfoEntry(text = stringResource(id = R.string.ranking_points), weight = pointsWeight)
            }
        }

        items(users) {
            Row(modifier = modifier.fillMaxWidth()) {
                UserInfoEntry(text = "#${it.rank}", weight = rankWeight, textAlign = TextAlign.Right, onPersonClick, it.user, true)
                UserInfoEntry(text = it.user.username, weight = usernameWeight, textAlign = TextAlign.Left, onPersonClick, it.user, true)
                UserInfoEntry(text = it.user.points.toString(), weight = pointsWeight, textAlign = TextAlign.Right, onPersonClick, it.user, true)
            }
        }
    }
}

@Composable
fun RowScope.UserInfoEntry(
    text: String,
    weight: Float,
    textAlign: TextAlign = TextAlign.Center,
    onPersonClick: (UserOutputModel) -> Unit = {},
    user: UserOutputModel? = null,
    enable: Boolean = false
) {
    Text(
        text = text,
        color = Color.White,
        modifier = Modifier
            .weight(weight)
            .padding(8.dp)
            .clickable(enabled = enable) {
                onPersonClick(user!!)
            },
        textAlign = textAlign
    )
}

private const val MAX_INPUT_SIZE = 50
private fun ensureInputBounds(input: String) =
    input.trim().also {
        it.substring(range = 0 until min(a = it.length, b = MAX_INPUT_SIZE))
    }

@Preview
@Composable
fun EmptyRankingScreenPreview() {
    RankingScreen()
}

@Preview
@Composable
fun SingleUserRankingScreenPreview() {
    RankingScreen(state = RankingScreenState(RankingOutputModel(
        currentPage = 0,
        lastPage = 0,
        users = listOf(
            UserRankingOutputModel(
                rank = 1,
                user = UserOutputModel(
                    id = 0,
                    username = "Placeholder",
                    points = 0,
                    gamesPlayed = 0
                )
            ))
    )))
}

@Preview
@Composable
fun MultipleUsersRankingScreenPreview() {
    RankingScreen(state = RankingScreenState(RankingOutputModel(
        currentPage = 0,
        lastPage = 0,
        users = listOf(
            UserRankingOutputModel(
                rank = 1,
                user = UserOutputModel(
                    id = 0,
                    username = "Placeholder",
                    points = 0,
                    gamesPlayed = 0
                )
            ),
            UserRankingOutputModel(
                rank = 2,
                user = UserOutputModel(
                    id = 0,
                    username = "Placeholder",
                    points = 0,
                    gamesPlayed = 0
                )
            ),
            UserRankingOutputModel(
                rank = 3,
                user = UserOutputModel(
                    id = 0,
                    username = "Placeholder",
                    points = 0,
                    gamesPlayed = 0
                )
            ),
            UserRankingOutputModel(
                rank = 4,
                user = UserOutputModel(
                    id = 0,
                    username = "Placeholder",
                    points = 0,
                    gamesPlayed = 0
                )
            ))
    )))
}

/*@Preview
@Composable
fun ScrollUsersRankingScreenPreview() {
    val users = mutableListOf<UserRankingOutputModel>()
    repeat(100) {
        users.add(UserRankingOutputModel(
            it + 1,
            UserOutputModel(
                id = 0,
                username = "Placeholder",
                points = 0,
                gamesPlayed = 0
            )
        ))
    }
    RankingScreen(
        state = RankingScreenState(RankingOutputModel(
            currentPage = 0,
            lastPage = 0,
            users = users)
        ))
}*/

@Preview
@Composable
fun SearchRankingScreenPreview() {
    val users = mutableListOf<UserRankingOutputModel>()
    repeat(3) {
        users.add(UserRankingOutputModel(
            it + 1,
            UserOutputModel(
                id = 0,
                username = "Placeholder",
                points = 0,
                gamesPlayed = 0
            )
        ))
    }
    RankingScreen(
        search = RankingScreenSearchState(SearchOutputModel(users = users))
    )
}
