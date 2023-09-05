package isel.leic.pdm.battleship.http.model

import isel.leic.pdm.battleship.domain.ship.Ship
import java.time.Instant


data class UserInputModel(
    val username: String,
    val password: String
)

data class GameInputModel(
    val name: String? = null,
    val gameRule: Int
)

data class PlaceShipInputModel(
    val shipName: String,
    val at: String,
    val direction: Char
)

fun Ship.toPlaceShipInputModel() = PlaceShipInputModel(type.shipName, head.toString(), direction.value)

data class ShotInputModel(
    val target: String
)

data class ForfeitInputModel(
    val playerId: Int
)
