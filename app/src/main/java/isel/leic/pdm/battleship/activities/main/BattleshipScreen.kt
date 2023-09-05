package isel.leic.pdm.battleship.activities.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import isel.leic.pdm.battleship.R
import isel.leic.pdm.battleship.ui.Background
import isel.leic.pdm.battleship.ui.theme.BattleshipTheme

const val BattleshipScreenTag = "BattleshipScreen"
const val PlayButtonTag = "PlayButton"
const val RankingButtonTag = "RankingButton"
const val GameRulesButtonTag = "GameRulesButton"
const val UserInfoButtonTag = "UserInfoButton"
const val AboutButtonTag = "AboutButton"

@Composable
fun BattleshipScreen(
    onPlay: () -> Unit,
    onRanking: () -> Unit,
    onGameRules: () -> Unit,
    onUserInfo: () -> Unit,
    onAboutRequest: () -> Unit
) {
    BattleshipTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag(tag = BattleshipScreenTag),
            backgroundColor = MaterialTheme.colors.background
        ) { innerPadding ->
            MainContent(
                innerPadding = innerPadding,
                onPlay = onPlay,
                onRanking = onRanking,
                onGameRules = onGameRules,
                onUserInfo = onUserInfo,
                onAboutRequest = onAboutRequest
            )
        }
    }
}

@Composable
fun MainContent(
    innerPadding: PaddingValues,
    onPlay: () -> Unit,
    onRanking: () -> Unit,
    onGameRules: () -> Unit,
    onUserInfo: () -> Unit,
    onAboutRequest: () -> Unit
) {
    Background {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = R.drawable.banner),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxHeight(fraction = 0.5f)
                    .fillMaxWidth(fraction = 1f)
                    .padding(paddingValues = innerPadding),
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(weight = 1f),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                Button(onClick = onPlay, modifier = Modifier.testTag(tag = PlayButtonTag)) {
                    Text(stringResource(id = R.string.main_play_button))
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(weight = 1f),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
            }
            BottomBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(weight = 0.5f, fill = false),
                onRanking = onRanking,
                onGameRules = onGameRules,
                onUserInfo = onUserInfo,
                onAboutRequest = onAboutRequest
            )
        }
    }
}

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    onRanking: () -> Unit,
    onGameRules: () -> Unit,
    onUserInfo: () -> Unit,
    onAboutRequest: () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom,
    ) {
        MenuButton(
            painter = painterResource(id = R.drawable.ranking),
            testTag = RankingButtonTag,
            onClick = onRanking
        )

        MenuButton(
            painter = painterResource(id = R.drawable.options),
            testTag = GameRulesButtonTag,
            onClick = onGameRules
        )

        MenuButton(
            painter = painterResource(id = R.drawable.user),
            testTag = UserInfoButtonTag,
            onClick = onUserInfo
        )

        MenuButton(
            painter = painterResource(id = R.drawable.info),
            testTag = AboutButtonTag,
            onClick = onAboutRequest
        )
    }
}

@Composable
fun MenuButton(
    painter: Painter,
    testTag: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .testTag(tag = testTag)
            .size(size = 70.dp)
    ) {
        Image(
            painter = painter,
            contentDescription = null
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BattleshipScreenPreview() {
    BattleshipScreen(
        onAboutRequest = {  },
        onPlay = {  },
        onRanking = {  },
        onGameRules = {  },
        onUserInfo = {  }
    )
}

@Preview(device = Devices.AUTOMOTIVE_1024p, widthDp = 1280, heightDp = 720, showBackground = true)
@Composable
fun BattleshipScreenPreviewLandscape() {
    BattleshipScreen(
        onAboutRequest = {  },
        onPlay = {  },
        onRanking = {  },
        onGameRules = {  },
        onUserInfo = {  }
    )
}
