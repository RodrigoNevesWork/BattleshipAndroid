package isel.leic.pdm.battleship

import android.app.Application
import android.util.Log
import androidx.compose.ui.unit.Constraints
import com.google.gson.Gson
import isel.leic.pdm.battleship.services.GameService
import isel.leic.pdm.battleship.services.GameServiceInterface
import isel.leic.pdm.battleship.services.UserService
import isel.leic.pdm.battleship.services.UserServiceInterface
import okhttp3.Cache
import okhttp3.OkHttpClient
import java.net.URL
import java.util.concurrent.TimeUnit

const val TAG = "BattleshipApp"

/**
 * The contract for the object that holds all the globally relevant dependencies.
 */
interface DependenciesContainer {
    val userService: UserServiceInterface
    val gameService: GameServiceInterface
}

private val battleshipAPI_URL = URL("http://localhost:8080")

class BattleshipApplication: DependenciesContainer, Application() {

    private val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .cache(Cache(directory = cacheDir, maxSize = 50 * 1024 * 1024))
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
}