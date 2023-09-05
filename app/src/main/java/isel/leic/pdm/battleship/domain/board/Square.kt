package isel.leic.pdm.battleship.domain.board

import android.os.Parcelable
import isel.leic.pdm.battleship.domain.board.coordinates.Coordinates
import kotlinx.parcelize.Parcelize

/**
 * Square of the Board.
 */
@Parcelize
data class Square(
    val state: State,
    val coordinates: Coordinates
) : Parcelable {

    /**
     * Enum Class with the States possible of the [Square].
     */
    enum class State(val char: Char) {
        WATER(' '),
        SHIP('#'),
        HIT('*'),
        MISS('O');
    }

    fun changeState(newState: State): Square = copy(state = newState)
}
