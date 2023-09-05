package isel.leic.pdm.battleship.domain.board.coordinates

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

const val FIRST_COLUMN_CHAR = 'A'

/**
 * Represents a column index in a BattleShip board.
 * @param value the row number.
 */
@Parcelize
data class Column(val value: Int) : Parcelable {

    val letter: Char = FIRST_COLUMN_CHAR + value

}

/**
 * Int extension for expressing column indexes
 */
fun Int.toColumn() = Column(this)
