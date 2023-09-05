package isel.leic.pdm.battleship.activities.board

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import isel.leic.pdm.battleship.activities.board.tile.Tile
import isel.leic.pdm.battleship.domain.board.Board
import isel.leic.pdm.battleship.domain.board.coordinates.*

@Composable
fun BoardBuilder(
    modifier: Modifier = Modifier,
    board: Board,
    onTileSelected: (coordinate: Coordinates) -> Unit = { },
    enabledClick: Boolean = true
) {
    Column(
        modifier = modifier
            .padding(all = 32.dp)
            .fillMaxSize()
    ) {
        repeat(board.side) { row ->
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(weight = 1.0f)
                    .fillMaxSize()
            ) {
                repeat(board.side) { col ->
                    val item = board.grid.squares.find { it.coordinates == Coordinates(column = Column(col), row = Row(row)) }
                    Tile(
                        state = item!!.state,
                        onSelected = { if (enabledClick) onTileSelected(item.coordinates) },
                        modifier = Modifier.size(50.dp).weight(weight = 1.0f, fill = true)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun BoardPreview() {
    BoardBuilder(board = Board(10), onTileSelected = { })
}
