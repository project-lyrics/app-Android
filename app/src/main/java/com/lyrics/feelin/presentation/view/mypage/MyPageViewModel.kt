package com.lyrics.feelin.presentation.view.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lyrics.feelin.core.designsystem.component.FilterButtonData
import com.lyrics.feelin.presentation.view.component.note.NoteComponentData
import com.lyrics.feelin.presentation.view.component.profile.ProfileType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MyPageViewModel : ViewModel() {
    private val _myPageScreenStatus: MutableStateFlow<MyPageScreenState> = MutableStateFlow(MyPageScreenState.initial())
    val myPageScreenState: StateFlow<MyPageScreenState> = _myPageScreenStatus.asStateFlow()

    fun loadMyPageData() {
        viewModelScope.launch {
            @Suppress("MagicNumber")
            delay(1500L)
            _myPageScreenStatus.value = _loginWithNoteErrorSample
        }
    }

    private val _dataSample = MyPageScreenState(
        status = MyPageScreenStatus.SUCCESS_LOAD,
        tabStatus = MyPageTabScreenStatus.SUCCESS_LOAD,
        user = MyPageUserData(
            id = 1L,
            nickname = "실카실카",
            profileCharacterType = ProfileType.SHORT_HAIR,
        ),
        filterArtists = listOf(
            FilterButtonData(
                id = null,
                name = "전체",
                imageUrl = null,
            ),
            FilterButtonData(
                id = 1L,
                name = "쏜애플",
                imageUrl = "https://i.scdn.co/image/ab6761610000e5eb4506d70c02763753b03fd07b",
            ),
            FilterButtonData(
                id = 2L,
                name = "검정치마",
                imageUrl = "https://i.scdn.co/image/ab6761610000e5eb8609536d21beed6769d09d7f",
            ),
            FilterButtonData(
                id = 3L,
                name = "쏜애플",
                imageUrl = "https://i.scdn.co/image/ab6761610000e5eb4506d70c02763753b03fd07b",
            ),
            FilterButtonData(
                id = 4L,
                name = "검정치마",
                imageUrl = "https://i.scdn.co/image/ab6761610000e5eb8609536d21beed6769d09d7f",
            ),
            FilterButtonData(
                id = 3L,
                name = "쏜애플",
                imageUrl = "https://i.scdn.co/image/ab6761610000e5eb4506d70c02763753b03fd07b",
            ),
            FilterButtonData(
                id = 4L,
                name = "검정치마",
                imageUrl = "https://i.scdn.co/image/ab6761610000e5eb8609536d21beed6769d09d7f",
            ),
        ),
        notes = listOf(
            NoteComponentData.sampleNoLyrics(),
            NoteComponentData.sample(),
            NoteComponentData.sampleNoLyrics(),
            NoteComponentData.sample(),
        )
    )

    private val _logoutSample = MyPageScreenState(
        status = MyPageScreenStatus.SUCCESS_LOAD,
        tabStatus = MyPageTabScreenStatus.SUCCESS_LOAD,
        user = null,
        filterArtists = emptyList(),
        notes = emptyList()
    )

    private val _noNoteSample = MyPageScreenState(
        status = MyPageScreenStatus.SUCCESS_LOAD,
        tabStatus = MyPageTabScreenStatus.SUCCESS_LOAD,
        user = MyPageUserData.sample(),
        filterArtists = emptyList(),
        notes = emptyList()
    )

    private val _dataErrorSample = MyPageScreenState(
        status = MyPageScreenStatus.ERROR,
        tabStatus = MyPageTabScreenStatus.ERROR,
        user = MyPageUserData.sample(),
        filterArtists = emptyList(),
        notes = emptyList()
    )

    private val _loginWithNoteLoadingSample = MyPageScreenState(
        status = MyPageScreenStatus.SUCCESS_LOAD,
        tabStatus = MyPageTabScreenStatus.LOADING,
        user = MyPageUserData.sample(),
        filterArtists = emptyList(),
        notes = emptyList()
    )

    private val _loginWithNoteErrorSample = MyPageScreenState(
        status = MyPageScreenStatus.SUCCESS_LOAD,
        tabStatus = MyPageTabScreenStatus.ERROR,
        user = MyPageUserData.sample(),
        filterArtists = emptyList(),
        notes = emptyList()
    )
}
