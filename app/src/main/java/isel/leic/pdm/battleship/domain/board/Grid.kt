package isel.leic.pdm.battleship.domain.board

import android.os.Parcelable
import isel.leic.pdm.battleship.domain.board.coordinates.Column
import isel.leic.pdm.battleship.domain.board.coordinates.Coordinates
import isel.leic.pdm.battleship.domain.board.coordinates.Row
import isel.leic.pdm.battleship.domain.board.coordinates.toCoordinates
import isel.leic.pdm.battleship.domain.ship.Direction
import isel.leic.pdm.battleship.domain.ship.FleetComposition
import isel.leic.pdm.battleship.domain.ship.calculatePositions
import kotlinx.parcelize.Parcelize

/**
 * Represents the board grid.
 */
@Parcelize
data class Grid(
    val side: Int,
    val squares: List<Square> = buildEmptySquares(side)
) : Parcelable {

    operator fun get(column: Column, row: Row): Square = this[Coordinates(column, row)]

    operator fun get(coordinates: Coordinates): Square = squares[coordinates.toIndex(side)]

    /**
     * Returns the state that corresponds to the square
     */
    fun getState(coordinates: Coordinates) = get(coordinates).state

    /**
     * Places a ship on the grid.
     * @return returns a Grid with the new ship placed
     */
    fun placeShip(ship: FleetComposition, at: Coordinates, direction: Direction): Grid? {
        val positions = positionsAvailable(ship, at, direction)
        return if (positions.isEmpty()) null
        else copy(squares = replaceState(positions, Square.State.SHIP))
    }

    fun replaceState(list: List<Coordinates>, newState: Square.State): List<Square> =
        squares.map {
            if(list.contains(it.coordinates)) it.changeState(newState)
            else it
        }

    fun replaceState(at: Coordinates, newState: Square.State): List<Square> =
        squares.map {
            if(at == it.coordinates) it.changeState(newState)
            else it
        }

    /**
     * Takes a shot on a coordinates, altering the state's square.
     * @return returns a [Grid] if shot is valid else returns null
     */
    fun shotOnGrid(target: Coordinates): Grid? =
        when (getState(target)) {
            Square.State.WATER -> copy(squares = replaceState(target, Square.State.MISS))
            Square.State.SHIP -> copy(squares = replaceState(target, Square.State.HIT))
            else -> null
        }

    fun hide(): Grid =
        copy(
            squares = squares.map { if (it.state == Square.State.SHIP) it.changeState(Square.State.WATER) else it }
        )

    override fun toString(): String {
        val str = StringBuilder()
        squares.forEach { str.append(it.state.char) }
        return str.toString()
    }
}

/**
 * Checks if a [coordinates] has a miss hit.
 */
fun Grid.isMissShot(coordinates: Coordinates) = getState(coordinates) == Square.State.MISS

/**
 * Checks if you can place a ship in a location (doesn't overlap, isn't adjacent to another ship)
 * @return list if the positions are available otherwise returns an empty list
 */
fun Grid.positionsAvailable(ship: FleetComposition, coordinates: Coordinates, direction: Direction): List<Coordinates> {
    if ((coordinates.column.value + ship.shipSize > side && direction == Direction.HORIZONTAL)
        || (coordinates.row.value + ship.shipSize > side && direction == Direction.VERTICAL))
        return emptyList()
    val positions = ship.calculatePositions(coordinates, direction)

    squares
        .filter { it.state == Square.State.SHIP }
        .forEach {
            positions.forEach { square ->
                if (square.isAdjacent(it.coordinates)) return emptyList()
            }
        }
    return positions
}

/**
 * Helper function used to build an empty Battleship grid.
 */
fun buildEmptySquares(side: Int): List<Square> =
    List (
        size = side * side,
        init = { Square(Square.State.WATER, it.toCoordinates(side)) }
    )
