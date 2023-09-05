package isel.leic.pdm.battleship.activities.board.tile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import isel.leic.pdm.battleship.R
import isel.leic.pdm.battleship.domain.board.Square

@Composable
fun Tile(
    modifier: Modifier = Modifier,
    state: Square.State = Square.State.WATER,
    onSelected: () -> Unit = { }
) {
    Box(
        modifier = modifier
            .background(color = MaterialTheme.colors.background)
            .fillMaxSize()
            .padding(all = 1.dp)
            .clickable(enabled = onSelected != { }) { onSelected() }
            .border(1.dp, Color.Black)
    ) {
        val stateImageId = when(state) {
            Square.State.WATER -> R.drawable.water
            Square.State.SHIP -> R.drawable.battleship
            Square.State.HIT -> R.drawable.explosion
            Square.State.MISS -> R.drawable.miss
        }

        Image(
            painter = painterResource(id = stateImageId),
            contentDescription = "Shows to the user what is in the square.",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
    }
}
