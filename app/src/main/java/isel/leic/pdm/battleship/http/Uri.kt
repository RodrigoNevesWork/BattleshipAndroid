package isel.leic.pdm.battleship.http

object Uri {
    const val RANKING = "/ranking"

    const val USER = "/user"
    const val GAME = "/game"

    const val SUBSCRIBE = "/subscribe"

    // ----------- USER -----------
    const val LOGIN = "$USER/login"
    const val USER_RANKING = "$USER/ranking"
    const val USER_BY_ID = "$USER/{id}"
    const val ME = "$USER_RANKING/me"


    // ----------- GAME -----------
    const val START_GAME = "$GAME/"
    const val LOBBY = "$GAME/lobby"
    const val GAME_RULES = "$GAME/rules"

    const val GAME_BY_PLAYER = "$GAME/player"
    const val GAME_BY_ID = "$GAME/{gameId}"
    const val MY_BOARD = "$GAME/{gameId}/ownBoard"
    const val ENEMY_BOARD = "$GAME/{gameId}/enemyBoard"
    const val FORFEIT = "$GAME/forfeit"

}
