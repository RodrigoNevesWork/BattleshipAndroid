package isel.leic.pdm.battleship.activities.ranking

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import isel.leic.pdm.battleship.http.model.RankingOutputModel
import isel.leic.pdm.battleship.http.model.SearchOutputModel
import isel.leic.pdm.battleship.services.UserServiceInterface
import isel.leic.pdm.battleship.utils.ProblemJson
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RankingViewModel(
    private val userService: UserServiceInterface
): ViewModel() {

    private val _userRanking = MutableStateFlow(RankingOutputModel(0, 0, emptyList()))
    val userRanking = _userRanking.asStateFlow()

    private val _userSearch = MutableStateFlow(SearchOutputModel(emptyList()))
    val userSearch = _userSearch.asStateFlow()

    private var _error by mutableStateOf<ProblemJson?>(null)
    val error : ProblemJson?
        get() = _error

    fun getRanking(): Job? {
        val lastPage = userRanking.value.lastPage
        val currentPage = userRanking.value.currentPage
        return if(lastPage != 0 && lastPage == currentPage) {
            null
        } else {
            viewModelScope.launch {
                try {
                    userService.getRanking(currentPage).collect { event ->
                        _userRanking.value = event
                    }
                } catch (e: Exception) {
                    if (e is ProblemJson) _error = e
                }
            }
        }
    }

    fun getRankingSearch(username: String): Job? {
        return if(username.isBlank()) {
            null
        } else {
            viewModelScope.launch {
                try {
                    userService.getUserRanking(username).collect { event ->
                        _userSearch.value = event
                    }
                } catch (e: Exception) {
                    if (e is ProblemJson) _error = e
                }
            }
        }
    }

    fun getMe(): Job {
        return viewModelScope.launch {
            try {
                userService.getMe().collect { event ->
                    _userSearch.value = event
                }
            } catch (e: Exception) {
                if (e is ProblemJson) _error = e
            }
        }
    }

    fun setDefaultUserSearch(): Job {
        return viewModelScope.launch {
            try {
                _userSearch.value = SearchOutputModel(emptyList())
            } catch (e: Exception) {
                if (e is ProblemJson) _error = e
            }
        }
    }
}
