package isel.leic.pdm.battleship.preferences

data class UserInfo(val username: String, val password: String) {
    init {
        require(value = validateUserInfoParts(username = username, password = password))
    }
}

fun userInfoOrNull(username: String, password: String): UserInfo? =
    if (validateUserInfoParts(username, password))
        UserInfo(username = username, password = password)
    else
        null

fun validateUserInfoParts(username: String, password: String) =
    username.isNotBlank() && password.isNotBlank()
