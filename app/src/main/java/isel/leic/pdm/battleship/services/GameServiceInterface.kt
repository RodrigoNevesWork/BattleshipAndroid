package isel.leic.pdm.battleship.services

import isel.leic.pdm.battleship.domain.Game
import isel.leic.pdm.battleship.domain.GameRules
import isel.leic.pdm.battleship.http.model.*
import kotlinx.coroutines.flow.Flow

interface GameServiceInterface {
    suspend fun start(game: GameInputModel): Flow<MatchmakerOutputModel>

    suspend fun getGameById(gameId: Int): Flow<Game>

    suspend fun getGameByPlayer(playerId: Int): Flow<GameIdOutputModel>

    suspend fun placeAllShips(gameId: Int, shipsToPlace: List<PlaceShipInputModel>): Flow<PlaceShipOutputModel>

    suspend fun makePlay(gameId: Int, shot: ShotInputModel): Flow<ShotOutputModel>

    suspend fun getAllGameRules(): Flow<GameRules>

    suspend fun forfeit(userAIDs : Pair<Int,Int>, userBID : Int, turn : Int): Flow<Game>

}
