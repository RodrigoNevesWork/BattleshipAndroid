package isel.leic.pdm.battleship.activities.user

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import isel.leic.pdm.battleship.R
import isel.leic.pdm.battleship.preferences.UserInfo
import isel.leic.pdm.battleship.preferences.userInfoOrNull
import isel.leic.pdm.battleship.ui.*
import isel.leic.pdm.battleship.ui.theme.BattleshipTheme
import isel.leic.pdm.battleship.ui.theme.Grey
import isel.leic.pdm.battleship.ui.theme.White
import kotlin.math.min

const val UserScreenTag = "UserScreen"
const val NicknameInputTag = "NicknameInput"
const val PasswordInputTag = "PasswordInput"

@Composable
fun UserInfoScreen(
    onSaveRequest: (UserInfo) -> Unit = { },
    onBackRequest: () -> Unit = { },
) {
    BattleshipTheme{
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag(tag = UserScreenTag),
            backgroundColor = MaterialTheme.colors.background,
            topBar = {
                TopBar(
                    onBackRequested = onBackRequest,
                )
            },
        ) { innerPadding ->
            val windowInfo = rememberWindowSize()
            Background {
                if(windowInfo.screenWidthInfo is WindowInfo.WindowType.Compact){
                    DisplayPrompts(
                        innerPadding = innerPadding,
                        onSaveRequest = onSaveRequest,
                    )
                } else {
                    DisplayPrompts(
                        innerPadding = innerPadding,
                        onSaveRequest = onSaveRequest,
                        modifier = Modifier.fillMaxWidth(0.5f)
                    )
                }
            }
        }
    }
}

@Composable
fun DisplayPrompts(
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier,
    onSaveRequest: (UserInfo) -> Unit = { }
) {
    var displayedNick by remember { mutableStateOf(value = "") }
    var displayedPassword by remember { mutableStateOf(value = "") }

    val enteredInfo = userInfoOrNull(
        username = displayedNick,
        password = displayedPassword
    )

    var editing by remember { mutableStateOf(value = enteredInfo == null) }

    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(paddingValues = innerPadding)
    ) {
        Row {
            Text(
                text = stringResource(id = R.string.user_screen_title),
                style = MaterialTheme.typography.h1,
                color = Grey,
            )
        }
        Row {
            Column(
                modifier = modifier
            ) {
                TextShow(
                    textToDisplay = displayedNick,
                    editing = editing,
                    labelId = R.string.user_screen_nickname_tip,
                    iconImageVector = Icons.Default.Face,
                    testTag = NicknameInputTag,
                    updateText = { displayedNick = ensureInputBounds(it) }
                )
                TextShow(
                    textToDisplay = displayedPassword,
                    editing = editing,
                    labelId = R.string.user_screen_password_tip,
                    iconImageVector = Icons.Default.Info,
                    testTag = PasswordInputTag,
                    updateText = { displayedPassword = ensureInputBounds(it) },
                    hide = true
                )
            }
        }
        Row{
            EditFab(
                onClick =
                if (!editing) { { editing = true } }
                else if (enteredInfo == null) null
                else { { onSaveRequest(enteredInfo) } },
                mode = if (editing) FabMode.Save else FabMode.Edit
            )
        }
    }
}

@Composable
fun TextShow(
    textToDisplay: String,
    editing: Boolean,
    labelId: Int,
    iconImageVector: ImageVector,
    testTag: String,
    updateText: (string: String) -> Unit = {},
    hide: Boolean = false
) {
    OutlinedTextField(
        value = textToDisplay,
        onValueChange = updateText,
        maxLines = 3,
        label = { Text(stringResource(id = labelId)) },
        leadingIcon = {
            Icon(
                iconImageVector,
                contentDescription = ""
            )
        },
        readOnly = !editing,
        modifier = Modifier
            .padding(horizontal = 48.dp)
            .fillMaxWidth()
            .testTag(testTag)
            .semantics {
                if (!editing) this[SemanticsPropertyKey("ReadOnly")] =
                    Unit
            },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = White,
            leadingIconColor = Grey,
            focusedBorderColor = White,
            unfocusedBorderColor = Grey,
            disabledBorderColor = Grey,
            focusedLabelColor = White,
            unfocusedLabelColor = Grey,
            disabledLabelColor = Grey,
            disabledTextColor = Grey,
        ),
        visualTransformation = if (hide) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = if (hide) KeyboardOptions(keyboardType = KeyboardType.Password) else KeyboardOptions.Default
    )
}

private const val MAX_INPUT_SIZE = 50
private fun ensureInputBounds(input: String) =
    input.trim().also {
        it.substring(range = 0 until min(a = it.length, b = MAX_INPUT_SIZE))
    }

@Preview(showBackground = true)
@Composable
private fun PreferencesScreenViewModePreview() {
    UserInfoScreen(
        onSaveRequest = { },
        onBackRequest = { }
    )
}

@Preview(device = Devices.AUTOMOTIVE_1024p, widthDp = 1280, heightDp = 720, showBackground = true)
@Composable
private fun PreferencesScreenLandscape() {
    UserInfoScreen(
        onSaveRequest = { },
        onBackRequest = { }
    )
}
