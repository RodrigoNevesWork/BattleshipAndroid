package isel.leic.pdm.battleship.activities.game

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import isel.leic.pdm.battleship.DependenciesContainer
import isel.leic.pdm.battleship.domain.Game
import isel.leic.pdm.battleship.http.model.ForfeitInputModel
import isel.leic.pdm.battleship.utils.viewModelInit

class GameEndActivity: ComponentActivity() {

    private val viewModel by viewModels<GameViewModel> {
        viewModelInit {
            val app = (application as DependenciesContainer)
            GameViewModel(app.gameService)
        }
    }

    companion object {
        const val IS_LOCAL_VICTORY_PARCELABLE = "has_local_player_won"
        const val PLAYER_ID = "player_id"

        fun navigate(context: Context, isLocalPlayerVictory: Boolean? = null, playerId: Int? = null) {
            with(context) {
                val intent = Intent(this, GameEndActivity::class.java)
                if(isLocalPlayerVictory != null) intent.putExtra(IS_LOCAL_VICTORY_PARCELABLE, isLocalPlayerVictory)
                intent.putExtra(GameActivity.PLAYER_ID_PARCELABLE, playerId)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.forfeit(
            ForfeitInputModel(setPlayerId)
        )

        setContent {
            GameEndScreen(
                isLocalPlayerVictorious = isLocalVictorious,
                onEndRequested = { finish() }
            )

            //CheckProblemJson(error = )
        }
    }

    private val isLocalVictorious: Boolean
        get() = intent.getBooleanExtra(IS_LOCAL_VICTORY_PARCELABLE, true)
    private val setPlayerId: Int
        get() = intent.getIntExtra(PLAYER_ID, 0)
}
