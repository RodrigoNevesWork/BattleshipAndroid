package isel.leic.pdm.battleship.domain

abstract class DomainException: Exception()

/**
 * Error types for play operation.
 */
abstract class PlayError(val error: String): DomainException() {

    class InvalidTurn: PlayError("Invalid Turn")
    class SquareNotToShoot: PlayError("Square Not To Shoot")
    class NoShip: PlayError("No Ship")
    class ShipSunk: PlayError("Ship Sunk")
    class InvalidCoordinates: PlayError("Invalid Coordinates")
    class CantPlace: PlayError("Cant Place")
    class GameOver: PlayError("Game Over")
    class GameNotStarted: PlayError("Game Not Started")
    class ExceededShips: PlayError("All Ships Placed")
    class Unexpected: PlayError("Unexpected Error")
}

abstract class APIError(val error: String): Exception() {
    class Unauthenticated: APIError("Unauthenticated")
}


