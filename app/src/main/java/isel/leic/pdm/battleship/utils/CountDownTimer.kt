package isel.leic.pdm.battleship.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay

@Composable
fun CountDownTimer(time: Int, subTime: () -> Unit, end: () -> Unit) {
    LaunchedEffect(key1 = time) {
        if(time > 0) {
            delay(1000)
            subTime()
        } else {
            end()
        }
    }
}
