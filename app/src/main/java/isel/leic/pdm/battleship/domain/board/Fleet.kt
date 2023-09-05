package isel.leic.pdm.battleship.domain.board

import android.os.Parcelable
import isel.leic.pdm.battleship.domain.GameRule
import isel.leic.pdm.battleship.domain.board.coordinates.Coordinates
import isel.leic.pdm.battleship.domain.ship.Direction
import isel.leic.pdm.battleship.domain.ship.FleetComposition
import isel.leic.pdm.battleship.domain.ship.Ship
import isel.leic.pdm.battleship.domain.ship.place
import kotlinx.parcelize.Parcelize

/**
 * Represents the board fleet.
 * @property ships List of Ships.
 */
@Parcelize
data class Fleet(val ships: List<Ship> = emptyList()) : Parcelable {

    fun getShipOrNull(coordinates: Coordinates) = ships.find { it.positions.contains(coordinates) }

    /**
     * Adds to the [Fleet] a specified [Ship].
     */
    fun addShip(ship: FleetComposition, at: Coordinates, direction: Direction) =
        if (countEqualShips(ship) < ship.quantity)
            Fleet(ships + ship.place(at, direction))
        else null

    /**
     * Counts the amount of the specified [FleetComposition].
     */
    fun countEqualShips(type: FleetComposition) = ships.count { it.type == type }

    fun allShipsPlaced(gameRule: GameRule) = ships.size == gameRule.totalShips

    fun isEmpty() = ships.isEmpty()

    fun isNotEmpty() = isEmpty().not()

    fun printFleetState() {
        ships.forEach { ship ->
            println("Ship: ${ship.type.shipName} - ${ship.type.shipSize} - ${ship.type.quantity}")
            ship.positions.forEach { position ->
                println("Position: ${position.row.value} - ${position.column.value}")
            }
        }
    }
}
