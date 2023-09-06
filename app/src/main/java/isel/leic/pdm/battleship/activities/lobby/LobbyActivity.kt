package isel.leic.pdm.battleship.activities.lobby

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import isel.leic.pdm.battleship.DependenciesContainer
import isel.leic.pdm.battleship.activities.game.GameSetupActivity
import isel.leic.pdm.battleship.activities.rule.preferences.GameRuleSelectActivity
import isel.leic.pdm.battleship.http.model.ForfeitInputModel
import isel.leic.pdm.battleship.http.model.GameInputModel
import isel.leic.pdm.battleship.preferences.GameRuleEncryptedSharedPreferences
import isel.leic.pdm.battleship.utils.viewModelInit

class LobbyActivity: ComponentActivity() {

    private val viewModel by viewModels<LobbyViewModel> {
        viewModelInit {
            val app = (application as DependenciesContainer)
            LobbyViewModel(app.gameService)
        }
    }

    companion object {
        fun navigate(context: Context) {
            with(context) {
                val intent = Intent(this, LobbyActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            if(GameRuleEncryptedSharedPreferences(this).gameRule == 0) GameRuleSelectActivity.navigate(this)

            val currentLobby by viewModel.lobby.collectAsState()
            val currentGameId by viewModel.gameId.collectAsState()

            LobbyScreen(
                onBackRequested = { finish() },
                onForfeitRequested = { viewModel.forfeit(ForfeitInputModel(currentLobby.id)) },
                waitingForPlayer = viewModel.state == GameState.STARTING
            )

            //CheckProblemJson(error = viewModel.error)

            if (viewModel.state == GameState.IDLE)
                viewModel.matchmaker(GameInputModel(gameRule = GameRuleEncryptedSharedPreferences(this).gameRule))

            if (viewModel.state == GameState.STARTED) {
                GameSetupActivity.navigate(this, currentLobby.id)
                finish()
            }

            if (viewModel.state == GameState.STARTED && currentGameId.id != null) {
                GameSetupActivity.navigate(this, gameId = currentGameId.id!!)
                finish()
            }

        }

    }
}
