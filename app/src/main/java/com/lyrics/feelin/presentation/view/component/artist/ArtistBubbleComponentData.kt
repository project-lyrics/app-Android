package com.lyrics.feelin.presentation.view.component.artist

import androidx.compose.runtime.Immutable

@Immutable
sealed interface ArtistBubbleComponentData {
    val name: String
    /** 첫 선호 아티스트 선택 화면 */
    data class InitialSelectArtistType(
        override val name: String,
        val imageUrl: String,
        val isSelected: Boolean
    ) : ArtistBubbleComponentData

    /** 홈 화면 아티스트 표시 */
    data class HomeFavoriteArtistType(
        override val name: String,
        val imageUrl: String,
        val id: Long? = null,
    ) : ArtistBubbleComponentData

    /** 홈 화면 아티스트 선택 버튼*/
    data class HomeFavoriteSearchType(
        override val name: String,
    ) : ArtistBubbleComponentData

    /** 관심 아티스트 찾기, 관심 아티스트 전체보기 화면 */
    data class FavoriteArtistFindType(
        override val name: String,
        val imageUrl: String,
    ) : ArtistBubbleComponentData
}
