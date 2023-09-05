package isel.leic.pdm.battleship.activities.rule.preferences

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import isel.leic.pdm.battleship.domain.GameRules
import isel.leic.pdm.battleship.services.GameServiceInterface
import isel.leic.pdm.battleship.utils.ProblemJson
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class GameRuleViewModel(
    private val gameService: GameServiceInterface
): ViewModel() {

    private var _allGameRules = MutableStateFlow(GameRules(emptyList()))
    val allGameRules
        get() = _allGameRules

    private var _error by mutableStateOf<ProblemJson?>(null)
    val error : ProblemJson?
        get() = _error

    fun getAllGameRules(): Job =
        viewModelScope.launch {
            try {
                gameService.getAllGameRules().collect { event ->
                    _allGameRules.value = event
                }
            } catch (e: Exception) {
                if (e is ProblemJson) _error = e
            }
        }
}
