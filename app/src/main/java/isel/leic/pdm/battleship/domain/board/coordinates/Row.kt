package isel.leic.pdm.battleship.domain.board.coordinates

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Represents a row index in a BattleShip board.
 * @param value the row index.
 */
@Parcelize
data class Row(val value: Int) : Parcelable {

    val number = value

}

/**
 * Int extension for expressing row indexes.
 */
fun Int.toRow() = Row(this)
