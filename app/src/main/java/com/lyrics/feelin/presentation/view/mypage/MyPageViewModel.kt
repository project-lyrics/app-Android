package com.lyrics.feelin.presentation.view.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lyrics.feelin.core.data.repository.UserRepository
import com.lyrics.feelin.core.designsystem.component.FilterButtonData
import com.lyrics.feelin.presentation.view.component.note.NoteComponentData
import com.lyrics.feelin.presentation.view.component.profile.ProfileType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Suppress("UnusedPrivateProperty") // TODO(@이대근): 실제 기능 구현시 제거할 것 2025.11.21.
@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _myPageScreenStatus: MutableStateFlow<MyPageScreenState> = MutableStateFlow(MyPageScreenState.initial())
    val myPageScreenState: StateFlow<MyPageScreenState> = _myPageScreenStatus.asStateFlow()

    fun loadMyPageData() {
        viewModelScope.launch {
            val userData = userRepository.userData.first()

            if (userData.isLoggedIn && userData.nickname != null) {
                // 로그인 상태: DataStore에서 사용자 정보 가져오기
                val profileType = profileIndexToProfileType(userData.profileIndex)
                _myPageScreenStatus.value = MyPageScreenState(
                    status = MyPageScreenStatus.SUCCESS_LOAD,
                    tabStatus = MyPageTabScreenStatus.SUCCESS_LOAD,
                    user = MyPageUserData(
                        id = 0L, // 로컬 임시 데이터이므로 0
                        nickname = userData.nickname,
                        profileCharacterType = profileType
                    ),
                    filterArtists = emptyList(),
                    notes = emptyList()
                )
            } else {
                // 비로그인 상태
                _myPageScreenStatus.value = _logoutSample
            }
        }
    }

    private fun profileIndexToProfileType(profileIndex: Int?): ProfileType {
        return ProfileType.entries.getOrNull(profileIndex ?: 0) ?: ProfileType.SHORT_HAIR
    }

    // TODO: 테스트용 DataStore 초기화 함수 - 추후 제거할 것
    fun clearDataStore() {
        viewModelScope.launch {
            userRepository.logout()
            _myPageScreenStatus.value = _logoutSample
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
