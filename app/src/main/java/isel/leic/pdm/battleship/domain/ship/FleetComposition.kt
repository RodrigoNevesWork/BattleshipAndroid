package isel.leic.pdm.battleship.domain.ship

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * All ship types that are in the game.
 * @property shipSize Number of squares occupied.
 * @property quantity Number of ships of this type in the starting fleet.
 */
@Parcelize
data class FleetComposition(
    val gameRule: Int,
    val shipName: String,
    val shipSize: Int,
    val quantity: Int
) : Parcelable
