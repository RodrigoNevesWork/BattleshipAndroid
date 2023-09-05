package isel.leic.pdm.battleship.activities.user

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import isel.leic.pdm.battleship.http.model.UserOutputModel
import isel.leic.pdm.battleship.preferences.UserInfo
import isel.leic.pdm.battleship.services.UserServiceInterface
import isel.leic.pdm.battleship.utils.ProblemJson
import kotlinx.coroutines.launch

class UserLoginViewModel(
    private val userService: UserServiceInterface
): ViewModel() {

    private var _creationResult by mutableStateOf<Result<Boolean>?>(null)
    val creationResult: Result<Boolean>?
        get() = _creationResult

    private var _buttonEnabled by mutableStateOf(false)
    val buttonEnabled: Boolean
        get() = _buttonEnabled

    private var _error by mutableStateOf<ProblemJson?>(null)
    val error : ProblemJson?
        get() = _error


    fun tryCreation(userInfo: UserInfo) {
        viewModelScope.launch {
            _buttonEnabled = false
            _creationResult = try {
                userService.login(userInfo)
                _buttonEnabled = true
                Result.success(true)
            } catch (e: Exception) {
                if (e is ProblemJson) _error = e
                _buttonEnabled = true
                Result.failure(e)
            }
        }
    }
}