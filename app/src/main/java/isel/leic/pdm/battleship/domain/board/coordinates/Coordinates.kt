package isel.leic.pdm.battleship.domain.board.coordinates

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Represents coordinates in the BattleShip board.
 */
@Parcelize
data class Coordinates(val column: Column, val row: Row) : Parcelable {
    constructor(value: Int, side: Int) : this((value % side).toColumn(), (value / side).toRow())
    fun toIndex(side: Int) = row.value * side + column.value

    /**
     * Adjacent of a Coordinates.
     */
    fun isAdjacent(coordinates: Coordinates): Boolean {
        return (column.value) - (coordinates.column.value) in -1..1 &&
                (row.value) - (coordinates.row.value) in -1..1
    }
    override fun toString() = "${column.letter}${row.number}"

}

fun Int.toCoordinates(side: Int) = Coordinates(this, side)
