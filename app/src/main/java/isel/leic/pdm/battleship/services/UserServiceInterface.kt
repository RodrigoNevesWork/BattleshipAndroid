package isel.leic.pdm.battleship.services

import isel.leic.pdm.battleship.http.model.*
import isel.leic.pdm.battleship.preferences.UserInfo
import kotlinx.coroutines.flow.Flow

interface UserServiceInterface {
    suspend fun login(userInfo: UserInfo): UserCredentialsOutputModel

    suspend fun getUser(userId : Int): UserOutputModel

    fun getRanking(currentPage: Int): Flow<RankingOutputModel>

    suspend fun getUserRanking(username: String): Flow<SearchOutputModel>

    suspend fun getMe(): Flow<SearchOutputModel>
}