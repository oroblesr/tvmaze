package com.example.tvmaze.ui.main


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tvmaze.model.MainScreenState
import com.example.tvmaze.repository.ShowRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainScreenViewModel(
    private val showRepository: ShowRepository
): ViewModel() {
    private val _uiState = MutableStateFlow<MainScreenState?>(MainScreenState())
    val uiState: StateFlow<MainScreenState?> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val result = showRepository.getSchedule()
            if (result?.isSuccessful == true) {
                result.body()?.let { _uiState.value = MainScreenState.from(it) }
            } else {

            }
        }
    }
}

class MainScreenViewModelFactory(
    private val showRepository: ShowRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainScreenViewModel(showRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
