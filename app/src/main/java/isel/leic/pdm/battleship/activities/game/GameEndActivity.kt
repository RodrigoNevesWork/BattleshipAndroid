package isel.leic.pdm.battleship.activities.game

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class GameEndActivity: ComponentActivity() {
    companion object {
        const val IS_LOCAL_VICTORY_PARCELABLE = "has_local_player_won"
        fun navigate(context: Context, isLocalPlayerVictory: Boolean? = null) {
            with(context) {
                val intent = Intent(this, GameEndActivity::class.java)
                if(isLocalPlayerVictory != null) intent.putExtra(IS_LOCAL_VICTORY_PARCELABLE, isLocalPlayerVictory)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
}
