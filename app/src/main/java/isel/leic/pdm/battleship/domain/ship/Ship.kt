package isel.leic.pdm.battleship.domain.ship

import android.os.Parcelable
import isel.leic.pdm.battleship.domain.board.coordinates.Coordinates
import isel.leic.pdm.battleship.domain.board.coordinates.toColumn
import isel.leic.pdm.battleship.domain.board.coordinates.toRow
import kotlinx.parcelize.Parcelize


/**
 * Ship class in the game.
 * @property type type of the Ship
 * @property head position occupied by the head's Ship
 * @property direction direction of the Ship
 */
@Parcelize
data class Ship(
    val id: Int,
    val head: Coordinates,
    val direction: Direction,
    val type: FleetComposition
) : Parcelable {

    val positions: List<Coordinates>
        get() = type.calculatePositions(head, direction)
}


/**
 * Ship positions.
 */
fun FleetComposition.calculatePositions(head: Coordinates, direction: Direction) =
    List(shipSize) {
        if (direction === Direction.HORIZONTAL)
            Coordinates((head.column.value + it).toColumn(), head.row)
        else
            Coordinates(head.column, (head.row.value + it).toRow())
    }

fun FleetComposition.place(coordinates: Coordinates, direction: Direction) =
    Ship(
        id = 0,
        direction = direction,
        type = this,
        head = coordinates,
    )

