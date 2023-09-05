package isel.leic.pdm.battleship.ui

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import isel.leic.pdm.battleship.R
import isel.leic.pdm.battleship.ui.theme.BlueTopBar

const val NavigateBackTag = "NavigateBackButton"

@Composable
fun TopBar(
    onBackRequested: (() -> Unit)? = null,
    onAboutRequested: (() -> Unit)? = null,
    onNavRequested: (() -> Unit)? = null
) {
    TopAppBar(
        title = { stringResource(id = R.string.app_name) },
        navigationIcon = {
            if(onBackRequested != null) {
                IconButton(onClick = onBackRequested) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.testTag(NavigateBackTag))
                }
            }
        },
        actions = {
            if(onNavRequested != null) {
                IconButton(onClick = onNavRequested) {
                    Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null)
                }
            } else if(onAboutRequested != null) {
                IconButton(onClick = onAboutRequested) {
                    Icon(imageVector = Icons.Default.Info, contentDescription = null)
                }
            }
        },
        backgroundColor = BlueTopBar,
    )
}

@Preview
@Composable
fun TopBarPreview() {
    MaterialTheme {
        TopBar()
    }
}
