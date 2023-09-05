package isel.leic.pdm.battleship.domain.board

import android.os.Parcelable
import isel.leic.pdm.battleship.domain.board.coordinates.Coordinates
import isel.leic.pdm.battleship.domain.ship.Direction
import isel.leic.pdm.battleship.domain.ship.FleetComposition
import isel.leic.pdm.battleship.domain.ship.Ship
import kotlinx.parcelize.Parcelize


/**
 * Represents the game board.
 */
@Parcelize
data class Board(
    val side: Int = 0,
    val grid: Grid = Grid(side),
    val fleet: Fleet = Fleet()
) : Parcelable {

    /**
     * Places a ship on the grid.
     * @return A Board with the new ship placed.
     */
    fun placeShip(ship: FleetComposition, at: Coordinates, direction: Direction): Board? {
        val finalFleet = fleet.addShip(ship, at, direction) ?: return null
        val finalGrid = grid.placeShip(ship, at, direction) ?: return null
        return copy(
            grid = finalGrid,
            fleet = finalFleet
        )
    }

    /**
     * Takes a shot on a position, altering the tile.
     * @return returns a [Board] if it shot is valid else returns null
     */
    fun shot(target: Coordinates): Board? {
        val finalGrid = grid.shotOnGrid(target) ?: return null
        return copy(grid = finalGrid)
    }

    fun allShipSunks(): Boolean =
        fleet.ships.all { ship ->
           isShipSunk(ship)
        }

    fun isShipSunk(ship: Ship): Boolean =
        ship.positions.all { grid.getState(it) == Square.State.HIT }

    fun shotState(target: Coordinates): Square.State? {
        shot(target)?.let { return grid.getState(target) }
        return null
    }

}
