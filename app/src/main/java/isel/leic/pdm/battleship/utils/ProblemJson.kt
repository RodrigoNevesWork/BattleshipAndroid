package isel.leic.pdm.battleship.utils

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import isel.leic.pdm.battleship.domain.PlayError
import isel.leic.pdm.battleship.ui.ErrorAlert
import okhttp3.MediaType.Companion.toMediaType
import java.net.URI

open class ProblemJson(
    open val title: String,
    open val status: Int,
    val type: URI = title.getURI()
): Exception() {
    companion object{
        val MEDIA_TYPE = "application/problem+json".toMediaType()
    }
}

fun String.getURI(): URI {
    val uri = replace(" ", "-").lowercase()
    return URI("/$uri")
}

@Composable
fun ComponentActivity.CheckProblemJson(error: ProblemJson?) {
    if (error != null) {
        ErrorAlert(
            title = error.title,
            message = "Status ${error.status}",
            buttonText ="OK",
            onDismiss = { handleProblemJson(error) }
        )
    }
}

    private fun ComponentActivity.handleProblemJson(error: ProblemJson) {
    return when (error.title) {
        UserNotFound().message -> { finish() }
        else -> { finish() }
    }
}

fun PlayError.toProblemJson(): ProblemJson =
    when(this) {
        is PlayError.InvalidTurn -> ProblemJson(error,409)
        is PlayError.SquareNotToShoot -> ProblemJson(error,409)
        is PlayError.NoShip -> ProblemJson(error,404)
        is PlayError.ShipSunk -> ProblemJson(error,409)
        is PlayError.InvalidCoordinates -> ProblemJson(error,400)
        is PlayError.CantPlace -> ProblemJson(error,400)
        is PlayError.GameOver -> ProblemJson(error,409)
        is PlayError.GameNotStarted -> ProblemJson(error, 400)
        is PlayError.ExceededShips -> ProblemJson(error,409)
        else -> ProblemJson(PlayError.Unexpected().error,500)
    }
