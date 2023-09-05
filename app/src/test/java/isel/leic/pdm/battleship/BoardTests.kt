package isel.leic.pdm.battleship

import isel.leic.pdm.battleship.domain.board.Board
import isel.leic.pdm.battleship.domain.board.Square
import isel.leic.pdm.battleship.domain.board.coordinates.Coordinates
import isel.leic.pdm.battleship.domain.ship.Direction
import isel.leic.pdm.battleship.domain.ship.FleetComposition
import org.junit.Assert.*
import org.junit.Test

class BoardTests {
    @Test
    fun `creating a board with empty fleet`() {
        val board = Board(10)
        assertTrue(board.fleet.isEmpty())
    }

    @Test
    fun `adding a ship to the fleet`() {
        val board = Board(10)
        val ship = FleetComposition(1, "CARRIER", 1, 1)
        val coordinates = Coordinates(0, 1)
        val direction = Direction.HORIZONTAL
        val newFleet = board.fleet.addShip(ship, coordinates, direction)
        if(newFleet != null){
            assertTrue(newFleet.isNotEmpty())
        }else{
            throw Exception("Fleet is null")
        }
    }

    @Test
    fun `make a shot and check if the shot is valid`() {
        val board = Board(10)
        val coordinates = Coordinates(0, 1)
        val shotState = board.shotState(coordinates)
        assertTrue(shotState != null)
    }

    @Test
    fun `placing a ship, making a shot on him and check if it hit the ship`(){
        val board = Board(10)
        val ship = FleetComposition(1, "CARRIER", 1, 1)
        val coordinates = Coordinates(0, 1)
        val direction = Direction.HORIZONTAL
        val newBoard = board.placeShip(ship, coordinates, direction)
        val shotState = newBoard?.shotState(coordinates)
        assertTrue(shotState == Square.State.SHIP)
    }

}