package isel.leic.pdm.battleship.services

import android.content.Context
import com.google.gson.Gson
import isel.leic.pdm.battleship.domain.APIError
import isel.leic.pdm.battleship.domain.Game
import isel.leic.pdm.battleship.domain.GameRules
import isel.leic.pdm.battleship.domain.GameRulesType
import isel.leic.pdm.battleship.http.Uri
import isel.leic.pdm.battleship.http.model.*
import isel.leic.pdm.battleship.preferences.UserCredentialsEncryptedSharedPreferences
import isel.leic.pdm.battleship.utils.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import okhttp3.OkHttpClient

class GameService(
    private val httpClient: OkHttpClient,
    val context: Context,
    jsonEncoder: Gson
): GameServiceInterface, Requests(jsonEncoder) {

    private val userInfo = UserCredentialsEncryptedSharedPreferences(context).userInfo ?: throw APIError.Unauthenticated()

    private fun extendUrlGameWithId(gameId: Int) = getURLtoFetchAPI("${Uri.GAME}/$gameId")

    override suspend fun start(game: GameInputModel): Flow<MatchmakerOutputModel> =
        callbackFlow {
            val request = requestPostBuilder(
                    url = getURLtoFetchAPI(Uri.START_GAME),
                    body = GameInputModel(name = "", gameRule = game.gameRule),
                    token = userInfo.token
                )
            try {
                val response = request.send(httpClient).check()
                trySend((response.extractProperties(
                    MatchmakerOutputModelType.type) as SirenEntity<MatchmakerOutputModel>).properties!!
                )
            } catch (e: Exception) {
                close(e)
                throw e
            }
            awaitClose()
        }

    override suspend fun getGameById(gameId: Int): Flow<Game> =
        callbackFlow {
            val request = requestGetBuilder(
                url = extendUrlGameWithId(gameId),
                token = userInfo.token
            )
            try {
                val response = request.send(httpClient).check()
                trySend((response.extractProperties(
                    GameModelType.type) as SirenEntity<Game>).properties!!
                )
            } catch(e: Exception) {
                close(e)
                throw e
            }
            awaitClose()
        }

    override suspend fun getGameByPlayer(playerId: Int): Flow<GameIdOutputModel> =
        callbackFlow {
            val request = requestGetBuilder(
                url = getURLtoFetchAPI("${Uri.GAME_BY_PLAYER}/$playerId"),
                token = userInfo.token
            )
            try {
                val response = request.send(httpClient).check()
                trySend((response.extractProperties(
                    GameIdOutputModelType.type) as SirenEntity<GameIdOutputModel>).properties!!
                )
            } catch (e: Exception) {
                close(e)
                throw e
            }
            awaitClose()
        }

    override suspend fun placeAllShips(gameId: Int, shipsToPlace: List<PlaceShipInputModel>): Flow<PlaceShipOutputModel> =
        callbackFlow {
            val request = requestPostBuilder(
                url = extendUrlGameWithId(gameId),
                body = shipsToPlace,
                token = userInfo.token
            )
            try {
                val response = request.send(httpClient).check()
                val e = response.extractProperties(
                    PlaceShipOutputModelType.type)as SirenEntity<PlaceShipOutputModel>
                trySend(e.properties!!)
            } catch (e: Exception) {
                close(e)
                throw e
            }

            awaitClose()
        }

    override suspend fun makePlay(gameId: Int, shot: ShotInputModel): Flow<ShotOutputModel> =
        callbackFlow {
            val request = requestPutBuilder(
                url = extendUrlGameWithId(gameId),
                body = shot,
                token = userInfo.token
            )
            try {
                val response = request.send(httpClient).check()
                trySend((response.extractProperties(
                    ShotOutputModelType.type) as SirenEntity<ShotOutputModel>).properties!!
                )
            } catch (e: Exception) {
                close(e)
                throw e
            }
            awaitClose()
        }

    override suspend fun getAllGameRules(): Flow<GameRules> =
        callbackFlow {
            val request = requestGetBuilder(
                url = getURLtoFetchAPI(Uri.GAME_RULES),
                token = userInfo.token
            )
            try {
                val response = request.send(httpClient).check()
                trySend((response.extractProperties(
                    GameRulesType.type) as SirenEntity<GameRules>).properties!!
                )
            } catch (e: Exception) {
                close(e)
                throw e
            }
            awaitClose()
        }

    override suspend fun forfeit(userAIDs: Pair<Int, Int>, userBID: Int, turn: Int): Flow<Game> =
        callbackFlow {

            if(!isMyTurn(userInfo.id,turn)) awaitClose()

            val forfeitModel = ForfeitInputModel(
                if(userAIDs.first == userInfo.id) userAIDs.second else userBID
            )

            val request = requestPostBuilder(
                url = getURLtoFetchAPI(Uri.FORFEIT),
                body = forfeitModel,
                token = userInfo.token
            )

            try {
                val response = request.send(httpClient).check()
                trySend(
                    (response.extractProperties(GameModelType.type) as SirenEntity<Game>).properties!!
                )
            } catch(e: Exception) {
                close(e)
                throw e
            }
            awaitClose()
        }

    private fun isMyTurn(userID : Int, turnID : Int) : Boolean = userID == turnID
}
