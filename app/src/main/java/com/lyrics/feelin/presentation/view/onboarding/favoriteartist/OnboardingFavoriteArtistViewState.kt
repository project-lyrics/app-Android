package com.lyrics.feelin.presentation.view.onboarding.favoriteartist

enum class OnboardingFavoriteArtistStatus {
    INITIAL,
    LOADING,
    SUCCESS,
    ERROR,
}

/**
 * 온보딩 선호 아티스트 화면의 상태를 관리하는 데이터 클래스
 *
 * @property status 로딩 상태
 * @property artists 아티스트 목록 (선택 상태 포함)
 */
data class OnboardingFavoriteArtistViewState(
    val status: OnboardingFavoriteArtistStatus,
    val artists: List<FavoriteArtistData>,
) {
    /** 1명 이상의 아티스트가 선택되었으면 완료 버튼 활성화 */
    val isEnableComplete: Boolean
        get() = artists.any { it.isSelected }

    companion object {
        fun initial(): OnboardingFavoriteArtistViewState {
            return OnboardingFavoriteArtistViewState(
                status = OnboardingFavoriteArtistStatus.INITIAL,
                artists = emptyList(),
            )
        }

        fun loading(): OnboardingFavoriteArtistViewState {
            return OnboardingFavoriteArtistViewState(
                status = OnboardingFavoriteArtistStatus.LOADING,
                artists = emptyList(),
            )
        }

        fun success(artists: List<FavoriteArtistData>): OnboardingFavoriteArtistViewState {
            return OnboardingFavoriteArtistViewState(
                status = OnboardingFavoriteArtistStatus.SUCCESS,
                artists = artists,
            )
        }

        fun error(): OnboardingFavoriteArtistViewState {
            return OnboardingFavoriteArtistViewState(
                status = OnboardingFavoriteArtistStatus.ERROR,
                artists = emptyList(),
            )
        }
    }
}

/**
 * 선호 아티스트 화면 표시용 데이터
 *
 * @property id 아티스트 고유 ID
 * @property name 아티스트 이름
 * @property imageUrl 아티스트 이미지 URL
 * @property isSelected 사용자가 선택했는지 여부
 */
data class FavoriteArtistData(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val isSelected: Boolean = false,
)
