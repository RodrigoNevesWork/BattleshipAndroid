package isel.leic.pdm.battleship.activities.rule.preferences

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.lifecycleScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import isel.leic.pdm.battleship.DependenciesContainer
import isel.leic.pdm.battleship.activities.user.UserInfoActivity
import isel.leic.pdm.battleship.preferences.GameRuleEncryptedSharedPreferences
import isel.leic.pdm.battleship.ui.ErrorAlert
import isel.leic.pdm.battleship.utils.CheckProblemJson
import isel.leic.pdm.battleship.utils.viewModelInit
import kotlinx.coroutines.launch

class GameRuleSelectActivity: ComponentActivity() {

    private val viewModel by viewModels<GameRuleViewModel> {
        viewModelInit {
            val app = (application as DependenciesContainer)
            GameRuleViewModel(app.gameService)
        }
    }

    companion object {
        fun navigate(context: Context) {
            with(context) {
                val intent = Intent(this, GameRuleSelectActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val gameRules by viewModel.allGameRules.collectAsState()

            GameRuleSelect(
                onBackRequested = { finish() },
                gameRules = GameRuleSelectState(gameRules = gameRules),
                onCLick = {
                    GameRuleEncryptedSharedPreferences(context = this).gameRule = it.id
                    finish()
                },
                //gameRule = gameRules.list.find { it.id == GameRuleEncryptedSharedPreferences(context = this).gameRule }
            )

            CheckProblemJson(error = viewModel.error)

        }

        lifecycleScope.launch {
            viewModel.getAllGameRules()
        }
    }

    @Preview
    @Composable
    private fun ErrorMessage() {
        ErrorAlert(
            title = "Unauthenticated",
            message = "You have to login",
            buttonText = "OK",
            onDismiss = { UserInfoActivity.navigate(this) }
        )
    }
}
