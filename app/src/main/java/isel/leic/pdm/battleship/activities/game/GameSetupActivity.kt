package isel.leic.pdm.battleship.activities.game

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.lifecycleScope
import isel.leic.pdm.battleship.DependenciesContainer
import isel.leic.pdm.battleship.activities.lobby.GameState
import isel.leic.pdm.battleship.domain.Game
import isel.leic.pdm.battleship.preferences.UserCredentialsEncryptedSharedPreferences
import isel.leic.pdm.battleship.utils.CheckProblemJson
import isel.leic.pdm.battleship.utils.viewModelInit
import kotlinx.coroutines.launch

class GameSetupActivity: ComponentActivity() {

    private val viewModel by viewModels<GameViewModel> {
        viewModelInit {
            val app = (application as DependenciesContainer)
            GameViewModel(app.gameService)
        }
    }

    companion object {
        const val GAME_ID_PARCELABLE = "game_id_parcelable"
        const val PLAYER_ID_PARCELABLE = "player_id_parcelable"
        fun navigate(context: Context, playerId: Int = 0, gameId: Int = 0) {
            with(context) {
                val intent = Intent(this, GameSetupActivity::class.java)
                intent.putExtra(PLAYER_ID_PARCELABLE, playerId)
                intent.putExtra(GAME_ID_PARCELABLE, gameId)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val currentGame = viewModel.onGoingGame.collectAsState()

            GameSetupScreen(
                userInfo = UserCredentialsEncryptedSharedPreferences(this).userInfo!!,
                game = GameSetupScreenState(currentGame.value),
                onPlaceShips = { viewModel.placeShips(currentGame.value!!.id, it) },
                onBackRequested = { finish() },
                onTimeEnd = { }
            )

            if (viewModel.state.collectAsState().value == GameState.STARTED && currentGame.value?.state == Game.State.PLAYER_A_TURN) {
                GameActivity.navigate(this@GameSetupActivity, setPlayerId, setGameId)
                finish()
            }

            CheckProblemJson(error = viewModel.error)
        }

        lifecycleScope.launch {
            if (setGameId != 0) viewModel.getGame(setGameId)
            try {

                viewModel.otherPlaced.collect {
                    if (it) {
                        GameActivity.navigate(this@GameSetupActivity, setPlayerId, setGameId)
                        finish()
                    }
                }
            } catch(_: Exception) { }
        }
    }

    private val setPlayerId: Int
        get() = intent.getIntExtra(PLAYER_ID_PARCELABLE, 0)
    private val setGameId: Int
        get() = intent.getIntExtra(GAME_ID_PARCELABLE, 0)

}

