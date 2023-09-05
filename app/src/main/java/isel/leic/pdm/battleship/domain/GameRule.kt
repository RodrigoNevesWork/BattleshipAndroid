package isel.leic.pdm.battleship.domain

import android.os.Parcelable
import isel.leic.pdm.battleship.domain.ship.FleetComposition
import isel.leic.pdm.battleship.utils.SirenEntity
import kotlinx.parcelize.Parcelize

data class GameRules(
    val list: List<GameRule>
)

val GameRulesType = SirenEntity.getType<GameRules>()

@Parcelize
data class GameRule(
    val id: Int = 0,
    val gridSize: Int = 0,
    val shotsRound: Int = 0,
    val timeToPlace: Int = 0,
    val timeToShoot: Int = 0,
    val ships: List<FleetComposition> = emptyList()
) : Parcelable {
    val totalShips: Int
        get() = ships.sumOf { it.quantity }

    fun totalShips(): List<FleetComposition> {
        val list = mutableListOf<FleetComposition>()

        ships.forEach { ship ->
            repeat(ship.quantity) {
                list.add(ship)
            }
        }
        return list
    }
}
