package isel.leic.pdm.battleship.activities.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.gson.Gson
import isel.leic.pdm.battleship.activities.about.AboutActivity
import isel.leic.pdm.battleship.activities.lobby.LobbyActivity
import isel.leic.pdm.battleship.activities.ranking.RankingActivity
import isel.leic.pdm.battleship.activities.rule.preferences.GameRuleSelectActivity
import isel.leic.pdm.battleship.activities.user.UserInfoActivity
import isel.leic.pdm.battleship.preferences.UserCredentialsEncryptedSharedPreferences
import isel.leic.pdm.battleship.preferences.UserInfo
import isel.leic.pdm.battleship.services.sse.SseService
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class BattleshipActivity : ComponentActivity() {
    companion object {
        fun navigate(context: Context) {
            with(context) {
                val intent = Intent(this, BattleshipActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        UserCredentialsEncryptedSharedPreferences(this).userInfo?.let {
            SseService(
                OkHttpClient.Builder()
                    .connectTimeout(0, TimeUnit.MINUTES)
                    .writeTimeout(0, TimeUnit.MINUTES)
                    .readTimeout(0, TimeUnit.MINUTES)
                    .build(),
                Gson()
            ).start(it.token)
        }

        setContent {
            BattleshipScreen(
                onAboutRequest = ::showAbout,
                onPlay = ::showLobby,
                onRanking = ::showRanking,
                onGameRules = ::showGameRule,
                onUserInfo = ::showUserInfo,
            )
        }
    }

    private fun showAbout() {
        AboutActivity.navigate(context = this)
    }

    private fun showRanking() {
        RankingActivity.navigate(context = this)
    }

    private fun showUserInfo() {
        UserInfoActivity.navigate(context = this)
    }

    private fun showGameRule() {
        GameRuleSelectActivity.navigate(context = this)
    }

    private fun showLobby() {
        LobbyActivity.navigate(context = this)
    }
}
