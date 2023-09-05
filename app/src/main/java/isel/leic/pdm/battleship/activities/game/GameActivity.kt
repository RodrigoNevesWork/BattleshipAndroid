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
import isel.leic.pdm.battleship.domain.Game
import isel.leic.pdm.battleship.http.model.ForfeitInputModel
import isel.leic.pdm.battleship.preferences.UserCredentialsEncryptedSharedPreferences
import isel.leic.pdm.battleship.utils.CheckProblemJson
import isel.leic.pdm.battleship.utils.viewModelInit
import kotlinx.coroutines.launch

class GameActivity: ComponentActivity() {

    private val viewModel by viewModels<GameViewModel> {
        viewModelInit {
            val app = (application as DependenciesContainer)
            GameViewModel(app.gameService)
        }
    }

    companion object {
        const val GAME_ID_PARCELABLE = "game_id_parcelable"
        const val PLAYER_ID_PARCELABLE = "player_id_parcelable"
        fun navigate(context: Context, playerId: Int, gameId: Int) {
            with(context) {
                val intent = Intent(this, GameActivity::class.java)
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
            val userInfo = UserCredentialsEncryptedSharedPreferences(context = this).userInfo!!

            GameScreen(
                userInfo = userInfo,
                game = GameScreenState(currentGame.value),
                onBackRequested = { finish() },
                onShot = { gameId, shot -> viewModel.shotShip(gameId = gameId, shot = shot) },
                onForfeitRequested = {
                    viewModel.forfeit(ForfeitInputModel(if(userInfo.id == currentGame.value!!.playerA.userId) currentGame.value!!.playerA.id else currentGame.value!!.playerB.id))
                    finish()
                },
                toEndActivity = {
                    GameEndActivity.navigate(context = this, isLocalPlayerVictory = it)
                    finish()
                }
            )
            CheckProblemJson(error = viewModel.error)

            if (currentGame.value == null || currentGame.value!!.state == Game.State.GAME_SETUP)
                viewModel.getGame(setGameId)

            if (currentGame.value != null)
                if(
                    (userInfo.id == currentGame.value!!.playerA.userId && currentGame.value!!.state == Game.State.PLAYER_B_TURN)
                        ||
                    (userInfo.id == currentGame.value!!.playerB.userId && currentGame.value!!.state == Game.State.PLAYER_A_TURN))
                        viewModel.getGame(setGameId)
        }

        lifecycleScope.launch {
            if (setGameId != 0) viewModel.getGame(setGameId)
            try {
                viewModel.onGoingGame.collect {
                    if ((setPlayerId == it!!.playerA.id && it.state != Game.State.PLAYER_A_TURN)
                        ||
                        (setPlayerId == it.playerB.userId && it.state != Game.State.PLAYER_A_TURN))
                    {
                        navigate(this@GameActivity, setPlayerId, setGameId)
                        finish()
                    }
                }
            } catch(_: Exception) { }
        }

    }

    private val setPlayerId: Int
        get() = intent.getIntExtra(GameSetupActivity.PLAYER_ID_PARCELABLE, 0)
    private val setGameId: Int
        get() = intent.getIntExtra(GameSetupActivity.GAME_ID_PARCELABLE, 0)

}