package isel.leic.pdm.battleship.ui

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import isel.leic.pdm.battleship.ui.theme.BattleshipTheme

enum class FabMode { Edit, Save }

const val EditButtonTag = "EditButton"
const val SaveButtonTag = "SaveButton"

@Composable
fun EditFab(
    onClick: (() -> Unit)? = null,
    mode: FabMode = FabMode.Edit,
) {
    Button(
        onClick = onClick ?: { },
        enabled = if (mode == FabMode.Edit) true else onClick != null,
        shape = CircleShape,
        modifier = Modifier
            .defaultMinSize(minWidth = 56.dp, minHeight = 56.dp)
            .testTag(
                tag =
                if (mode == FabMode.Edit) EditButtonTag
                else SaveButtonTag
            )
    ){
        if (mode == FabMode.Edit) {
            Icon(imageVector = Icons.Default.Edit, contentDescription = "")
        } else {
            Icon(imageVector = Icons.Default.Add, contentDescription = "")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EditFabEditModePreview() {
    BattleshipTheme {
        EditFab(onClick = { }, mode = FabMode.Edit)
    }
}

@Preview(showBackground = true)
@Composable
private fun EditFabUpdateModePreview() {
    BattleshipTheme {
        EditFab(onClick = { }, mode = FabMode.Save)
    }
}

@Preview(showBackground = true)
@Composable
private fun DisabledEditFabUpdateModePreview() {
    BattleshipTheme {
        EditFab(mode = FabMode.Save)
    }
}
