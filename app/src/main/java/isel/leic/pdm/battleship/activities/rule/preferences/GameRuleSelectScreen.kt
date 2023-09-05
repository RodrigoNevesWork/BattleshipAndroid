package isel.leic.pdm.battleship.activities.rule.preferences

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import isel.leic.pdm.battleship.R
import isel.leic.pdm.battleship.domain.GameRule
import isel.leic.pdm.battleship.domain.GameRules
import isel.leic.pdm.battleship.ui.Background
import isel.leic.pdm.battleship.ui.TopBar
import isel.leic.pdm.battleship.ui.theme.BattleshipTheme

data class GameRuleSelectState(
    val gameRules: GameRules = GameRules(emptyList())
)

// The game rules list is a string list because there is no object for the rules yet
// and to be able to use the preview for it
@Composable
fun GameRuleSelect(
    onBackRequested: () -> Unit = { },
    gameRules: GameRuleSelectState = GameRuleSelectState(),
    onCLick: (chosenGameRule: GameRule) -> Unit = { },
    //gameRule: GameRule? = null
) {
    BattleshipTheme {
        Scaffold(
            topBar = { TopBar(onBackRequested = onBackRequested) }
        ) { innerPadding ->
            Background {
                var chosenGameRule by remember { mutableStateOf<GameRule?>(null) }
                var textGameRule by remember { mutableStateOf("") }
                var isExpanded by remember { mutableStateOf(value = false) }
                var state by remember { mutableStateOf(value = SelectState.NotSelected) }
                Column {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues = innerPadding)
                            .weight(0.5f)
                    ) {
                        Text(
                            text = stringResource(id = R.string.user_game_rule_choice),
                            style = MaterialTheme.typography.h5,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colors.secondary
                        )
                        OutlinedTextField(
                            value = textGameRule,
                            onValueChange = { textGameRule = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    isExpanded = !isExpanded
                                }
                                .alpha(ContentAlpha.high),
                            enabled = false,
                            label = {
                                Text(
                                    text =
                                    if (chosenGameRule == null)
                                        stringResource(id = R.string.user_game_rule_placeholder)
                                    else
                                        "Game Rule ${chosenGameRule?.id}",
                                    color = MaterialTheme.colors.secondary
                                )
                            },
                            trailingIcon = {
                                Icon(imageVector = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                                    contentDescription = null,
                                    modifier = Modifier.clickable {
                                        isExpanded = !isExpanded
                                    }
                                )
                            }
                        )
                        Box {
                            DropdownMenu(
                                expanded = isExpanded,
                                onDismissRequest = { isExpanded = false },
                                modifier = Modifier
                            ) {
                                gameRules.gameRules.list.forEach { item ->
                                    DropdownMenuItem(
                                        onClick = {
                                            state = SelectState.Selected
                                            chosenGameRule = item
                                            isExpanded = false
                                        }
                                    ) {
                                        Text(
                                            text = "Game Rule ${item.id}",
                                            style = MaterialTheme.typography.caption,
                                            textAlign = TextAlign.Justify,
                                            color = MaterialTheme.colors.primary
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.sizeIn(minHeight = 4.dp, maxHeight = 4.dp))
                        CheckChosenRulesButton(
                            onCLick = { onCLick(chosenGameRule!!) },
                            checkEnabled = { state == SelectState.Selected })
                    }
                    if (chosenGameRule != null) {
                        LazyColumn(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(0.6f)
                        ){
                            item(){
                                Text(
                                    text = "Game Rule: ${chosenGameRule!!.id}\n" +
                                            "Grid Side: ${chosenGameRule!!.gridSize}\n" +
                                            "Shots per Round: ${chosenGameRule!!.shotsRound}\n" +
                                            "Time to Place: ${chosenGameRule!!.timeToPlace}\n" +
                                            "Time to Shoot: ${chosenGameRule!!.gridSize}\n" +
                                            "Ships: \n"
                                    ,
                                    color = MaterialTheme.colors.secondary
                                )
                            }
                            items(chosenGameRule!!.ships.size) { idx ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "\t Ship Name: ${chosenGameRule!!.ships[idx].shipName}\n" +
                                                "\t Ship Size: ${chosenGameRule!!.ships[idx].shipSize}\n" +
                                                "\t Quantity: ${chosenGameRule!!.ships[idx].quantity}" +
                                                "\n \n"
                                    )
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}

enum class SelectState { NotSelected, Selected }

@Composable
fun CheckChosenRulesButton(
    onCLick: () -> Unit = { },
    checkEnabled: () -> Boolean = { true }
) {
    Button(
        onClick = onCLick,
        modifier = Modifier.size(80.dp),
        border = BorderStroke(1.dp, Color.Transparent),
        shape = CircleShape,
        colors = ButtonDefaults.outlinedButtonColors(backgroundColor = Color.Transparent),
        enabled = checkEnabled()
    ) {
        Icon(
            Icons.Default.Check,
            contentDescription = null,
            modifier = Modifier.size(50.dp),
            tint = MaterialTheme.colors.onSecondary
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GameRuleSelectScreenShow() {
    GameRuleSelect(gameRules = GameRuleSelectState(GameRules(listOf(GameRule(1, 10, 1, 20, 20, listOf())))))
}
