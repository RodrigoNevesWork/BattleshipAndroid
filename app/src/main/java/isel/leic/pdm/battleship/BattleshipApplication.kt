package isel.leic.pdm.battleship

import android.app.Application
import com.google.gson.Gson
import isel.leic.pdm.battleship.services.GameService
import isel.leic.pdm.battleship.services.GameServiceInterface
import isel.leic.pdm.battleship.services.UserService
import isel.leic.pdm.battleship.services.UserServiceInterface
import isel.leic.pdm.battleship.services.sse.SseService
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

const val TAG = "BattleshipApp"

/**
 * The contract for the object that holds all the globally relevant dependencies.
 */
interface DependenciesContainer {
    val userService: UserServiceInterface
    val gameService: GameServiceInterface
    val sseService: SseService
}

class BattleshipApplication: DependenciesContainer, Application() {

    private val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(0, TimeUnit.MINUTES)
            .writeTimeout(0, TimeUnit.MINUTES)
            .readTimeout(0, TimeUnit.MINUTES)
            .build()
    }

    private val jsonEncoder: Gson
        get() = Gson()

    override val userService: UserServiceInterface
        get() = UserService(
            httpClient = httpClient,
            context = this,
            jsonEncoder = jsonEncoder
        )

    override val gameService: GameServiceInterface
        get() = GameService(
            httpClient = httpClient,
            context = this,
            jsonEncoder = jsonEncoder
        )

    override val sseService: SseService
        get() = SseService(
            httpClient = httpClient,
            jsonEncoder = jsonEncoder
        )
}