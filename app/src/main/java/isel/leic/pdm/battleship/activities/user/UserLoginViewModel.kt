package isel.leic.pdm.battleship.activities.user

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import isel.leic.pdm.battleship.http.model.UserInfoOutputModel
import isel.leic.pdm.battleship.http.model.UserOutputModel
import isel.leic.pdm.battleship.preferences.UserCredentialsEncryptedSharedPreferences
import isel.leic.pdm.battleship.preferences.UserInfo
import isel.leic.pdm.battleship.services.UserServiceInterface
import isel.leic.pdm.battleship.services.sse.EventBus
import isel.leic.pdm.battleship.services.sse.SseEventListener
import isel.leic.pdm.battleship.services.sse.SseService
import isel.leic.pdm.battleship.services.sse.models.SseEvent
import isel.leic.pdm.battleship.utils.ProblemJson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserLoginViewModel(
    private val sseService: SseService,
    private val preferences: UserCredentialsEncryptedSharedPreferences
): ViewModel(), SseEventListener {

    private var _creationResult = MutableStateFlow(false)
    val creationResult
        get() = _creationResult.asStateFlow()

    private var _buttonEnabled by mutableStateOf(false)
    val buttonEnabled: Boolean
        get() = _buttonEnabled

    private var _error by mutableStateOf<ProblemJson?>(null)
    val error : ProblemJson?
        get() = _error


    fun tryCreation(userInfo: UserInfo) {
        viewModelScope.launch {
            _buttonEnabled = false
            try {
                sseService.login(userInfo)
                _buttonEnabled = true
            } catch (e: Exception) {
                if (e is ProblemJson) _error = e
                _buttonEnabled = true
            }
        }
    }

    init {
        EventBus.registerListener(this)
    }

    override fun onCleared() {
        EventBus.unregisterListener(this)
        super.onCleared()
    }

    override fun onEventReceived(eventData: SseEvent) {
        _creationResult.value = true
        if (eventData is UserInfoOutputModel)
            preferences.userInfo = eventData
    }
}