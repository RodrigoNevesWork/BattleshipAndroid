package isel.leic.pdm.battleship.preferences

import android.content.Context
import isel.leic.pdm.battleship.R
import isel.leic.pdm.battleship.http.model.UserInfoOutputModel

class UserCredentialsEncryptedSharedPreferences(context: Context): BattleshipEncryptedSharedPreferences(context, R.string.user_shared_preferences_file) {
    private val idKey = "id"
    private val usernameKey = "username"
    private val tokenKey = "token"

    var userInfo: UserInfoOutputModel?
        get() {
            val savedId = preferences.getInt(idKey, 0)
            val savedUsername = preferences.getString(usernameKey, null)
            val savedToken = preferences.getString(tokenKey, null)
            return if(savedToken != null && savedUsername != null) UserInfoOutputModel(savedId, savedUsername, savedToken)
            else null
        }

        set(value) {
            if(value == null) {
                preferences.edit()
                    .remove(idKey)
                    .remove(tokenKey)
                    .remove(usernameKey)
                    .apply()
            } else {
                preferences.edit()
                    .putInt(idKey, value.id)
                    .putString(tokenKey, value.token)
                    .putString(usernameKey, value.username)
                    .apply()
            }
        }
}
