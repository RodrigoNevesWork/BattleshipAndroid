package isel.leic.pdm.battleship.activities.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import isel.leic.pdm.battleship.DependenciesContainer
import isel.leic.pdm.battleship.activities.main.BattleshipActivity
import isel.leic.pdm.battleship.utils.CheckProblemJson
import isel.leic.pdm.battleship.utils.viewModelInit

class UserInfoActivity: ComponentActivity() {

    private val viewModel by viewModels<UserLoginViewModel> {
        viewModelInit {
            val app = (application as DependenciesContainer)
            UserLoginViewModel(app.userService)
        }
    }

    companion object {
        fun navigate(context: Context) {
            with(context) {
                val intent = Intent(this, UserInfoActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            UserInfoScreen(
                onSaveRequest = {
                    viewModel.tryCreation(userInfo = it)
                    finish()
                },
                onBackRequest = { finish() },
            )
            CheckProblemJson(error = viewModel.error)
        }
    }
}
