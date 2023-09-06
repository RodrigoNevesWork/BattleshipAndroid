package isel.leic.pdm.battleship.services

import android.content.Context
import com.google.gson.Gson
import isel.leic.pdm.battleship.http.Uri
import isel.leic.pdm.battleship.http.model.*
import isel.leic.pdm.battleship.preferences.UserCredentialsEncryptedSharedPreferences
import isel.leic.pdm.battleship.preferences.UserInfo
import isel.leic.pdm.battleship.utils.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import okhttp3.OkHttpClient

class UserService(
    private val httpClient: OkHttpClient,
    private val context: Context,
    jsonEncoder: Gson
): UserServiceInterface, Requests(jsonEncoder = jsonEncoder) {
    private fun getToken() =
        UserCredentialsEncryptedSharedPreferences(context).userInfo?.token

    private fun extendUrlUserWithId(userId: Int ) = getURLtoFetchAPI("${Uri.USER}/$userId")

    override suspend fun login(userInfo: UserInfo): UserInfoOutputModel {
        val request = requestPostBuilder(
            url = getURLtoFetchAPI(Uri.LOGIN),
            body = userInfo
        )
        try {
            val response = request.send(httpClient).check()
            return (response.extractProperties(
                UserInfoOutputModelType.type) as SirenEntity<UserInfoOutputModel>).properties!!.also {
                    credentials -> UserCredentialsEncryptedSharedPreferences(context).userInfo = credentials
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getUser(userId: Int): UserOutputModel {
        val request = requestGetBuilder(
            url = extendUrlUserWithId(userId)
        )
        try {
            val response = request.send(httpClient).check()
            return (response.extractProperties(
                UserOutputModelType.type) as SirenEntity<UserOutputModel>).properties!!
        } catch (e: Exception) {
            throw e
        }
    }

    override fun getRanking(currentPage: Int): Flow<RankingOutputModel> =
        callbackFlow {
            val request = requestGetBuilder(
                url = getURLtoFetchAPI("${Uri.RANKING}?currentPage=" + currentPage)
            )
            try {
                val response = request.send(httpClient).check()
                trySend((response.extractProperties(
                    RankingOutputModelType.type) as SirenEntity<RankingOutputModel>).properties!!
                )
            } catch (e: Exception) {
                close(e)
                throw e
            }
            awaitClose()
        }

    override suspend fun getUserRanking(username: String): Flow<SearchOutputModel> =
        callbackFlow {
            try {
                val request = requestGetBuilder(
                    url = getURLtoFetchAPI("${Uri.USER_RANKING}?username=$username")
                )
                val response = request.send(httpClient).check()
                trySend((response.extractProperties(
                    SearchOutputModelType.type) as SirenEntity<SearchOutputModel>).properties!!
                )
            } catch (e: Exception) {
                close(e)
                throw e
            }
            awaitClose()
        }

    override suspend fun getMe(): Flow<SearchOutputModel> =
        callbackFlow {
            try {
                val request =requestGetBuilder(
                    url = getURLtoFetchAPI(Uri.ME),
                    token = getToken()
                )
                val response = request.send(httpClient).check()
                trySend((response.extractProperties(
                    SearchOutputModelType.type) as SirenEntity<SearchOutputModel>).properties!!
                )
            } catch (e: Exception) {
                close(e)
                throw e
            }
            awaitClose()
        }
}
