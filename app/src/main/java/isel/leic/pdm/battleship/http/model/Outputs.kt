package isel.leic.pdm.battleship.http.model

import android.os.Parcelable
import isel.leic.pdm.battleship.domain.Game
import isel.leic.pdm.battleship.domain.board.Square
import isel.leic.pdm.battleship.utils.SirenEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class RankingOutputModel(
    val currentPage: Int,
    val lastPage: Int,
    val users: List<UserRankingOutputModel>
) : Parcelable

val RankingOutputModelType = SirenEntity.getType<RankingOutputModel>()

@Parcelize
data class UserRankingOutputModel(
    val rank: Int,
    val user: UserOutputModel
) : Parcelable

@Parcelize
data class SearchOutputModel(
    val users: List<UserRankingOutputModel>
) : Parcelable

val SearchOutputModelType = SirenEntity.getType<SearchOutputModel>()

@Parcelize
class UserOutputModel(
    val id: Int,
    val username: String,
    val points: Int,
    val gamesPlayed: Int
): Parcelable

val UserOutputModelType = SirenEntity.getType<UserOutputModel>()


data class UserCredentialsOutputModel(
    val id: Int,
    val username: String,
    val token: String
)

val UserCredentialsOutputModelType = SirenEntity.getType<UserCredentialsOutputModel>()

data class MatchmakerOutputModel(
    val created: Boolean,
    val id: Int
)

val MatchmakerOutputModelType = SirenEntity.getType<MatchmakerOutputModel>()

data class GameStateOutputModel(
    val state: Game.State
)
/*
data class GameOutputModel(
    val id: Int,
    val state: Game.State,
    val playerA: PlayerOutputModel,
    val playerB: PlayerOutputModel,
    val gameRule: GameRule
)


fun GameOutputModel.toGame() = Game(
    id = id,
    state = state,
    playerA = Player(playerA.id),
    playerB = Player(playerB.id),
    gameRule = gameRule
)

 */
val GameModelType = SirenEntity.getType<Game>()


data class PlayerOutputModel(
    val id: Int
)

data class ShipOutputModel(
    val type: String,
    val positions: List<String>
)

data class PlaceShipOutputModel(
    val placed: Boolean
)

val PlaceShipOutputModelType = SirenEntity.getType<PlaceShipOutputModel>()

data class CoordinateOutputModel(
    val column: Char,
    val row: Int
)

data class ShotOutputModel(
    val coordinate: CoordinateOutputModel,
    val result: Square.State
)

val ShotOutputModelType = SirenEntity.getType<ShotOutputModel>()

data class TokenOutputModel(
    val token: String
)

data class GameIdOutputModel(
    val id: Int?
)

val GameIdOutputModelType = SirenEntity.getType<GameIdOutputModel>()

data class ForfeitOutputModel(
    val done: Boolean
)

val ForfeitOutputModelType = SirenEntity.getType<ForfeitOutputModel>()
