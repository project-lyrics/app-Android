package com.lyrics.feelin.presentation.view.notification

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class NotificationViewModel @Inject constructor() : ViewModel() {

    private val myNewsItems = NotificationUiState.populatedSample().items

    private val _uiState = MutableStateFlow(
        NotificationUiState(items = myNewsItems)
    )
    val uiState: StateFlow<NotificationUiState> = _uiState.asStateFlow()

    fun selectTab(tab: NotificationTab) {
        _uiState.value = _uiState.value.copy(
            selectedTab = tab,
            items = when (tab) {
                NotificationTab.MY_NEWS -> myNewsItems
                NotificationTab.ALL -> emptyList()
            }
        )
    }
}
