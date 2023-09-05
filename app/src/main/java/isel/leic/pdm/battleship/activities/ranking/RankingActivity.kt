package isel.leic.pdm.battleship.activities.ranking

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.lifecycleScope
import isel.leic.pdm.battleship.DependenciesContainer
import isel.leic.pdm.battleship.activities.main.BattleshipActivity
import isel.leic.pdm.battleship.activities.user.UserScreen
import isel.leic.pdm.battleship.utils.CheckProblemJson
import isel.leic.pdm.battleship.utils.viewModelInit
import kotlinx.coroutines.launch

class RankingActivity: ComponentActivity() {

    private val viewModel by viewModels<RankingViewModel> {
        viewModelInit {
            val app = (application as DependenciesContainer)
            RankingViewModel(app.userService)
        }
    }

    companion object {
        fun navigate(context: Context) {
            with(context) {
                val intent = Intent(this, RankingActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val rankingOutput by viewModel.userRanking.collectAsState()
            val searchOrMe by viewModel.userSearch.collectAsState()
            RankingScreen(
                state = RankingScreenState(rankingOutput),
                onBackRequest = { BattleshipActivity.navigate(this) },
                onMeClickRequest = {
                    viewModel.getMe()
                },
                onSearchRequest = {
                    viewModel.getRankingSearch(it)
                },
                search = RankingScreenSearchState(searchOrMe),
                clearSearch = { viewModel.setDefaultUserSearch() },
                onPersonClick = { person ->
                    setContent {
                        UserScreen(
                            user = person,
                            onBackRequest = { finish() },
                        )
                    }
                }
            )

            CheckProblemJson(error = viewModel.error)

        }

        lifecycleScope.launch {
            viewModel.getRanking()
        }
    }
}
