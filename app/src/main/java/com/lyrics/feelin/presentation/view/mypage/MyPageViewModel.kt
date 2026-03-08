package com.lyrics.feelin.presentation.view.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lyrics.feelin.core.data.repository.UserRepository
import com.lyrics.feelin.presentation.view.component.profile.ProfileType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
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

    private val _logoutSample = MyPageScreenState(
        status = MyPageScreenStatus.SUCCESS_LOAD,
        tabStatus = MyPageTabScreenStatus.SUCCESS_LOAD,
        user = null,
        filterArtists = emptyList(),
        notes = emptyList()
    )
}
