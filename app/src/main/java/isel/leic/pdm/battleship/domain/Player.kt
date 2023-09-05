package isel.leic.pdm.battleship.domain

import android.os.Parcelable
import isel.leic.pdm.battleship.domain.board.Board
import kotlinx.parcelize.Parcelize

@Parcelize
data class Player(
    val id: Int = 0,
    val playTime: String = "",
    val userId: Int = 0,
    val board: Board = Board()
) : Parcelable
