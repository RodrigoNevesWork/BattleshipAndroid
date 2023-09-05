package isel.leic.pdm.battleship.preferences

import android.content.Context
import isel.leic.pdm.battleship.R

class GameRuleEncryptedSharedPreferences(private val context: Context): BattleshipEncryptedSharedPreferences(context, R.string.gamerule_shared_preferences_file) {
    private val gameRuleKey = "game_rule"

    var gameRule: Int
        get() = preferences.getInt(gameRuleKey, 0)

        set(value) {
            if(value == 0) {
                preferences.edit()
                    .remove(gameRuleKey)
                    .apply()
            } else {
                preferences.edit()
                    .putInt(gameRuleKey, value)
                    .apply()
            }
        }
}
