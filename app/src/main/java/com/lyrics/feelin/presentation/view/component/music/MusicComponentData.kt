package com.lyrics.feelin.presentation.view.component.music

import androidx.compose.runtime.Immutable

/**
 * 곡 정보를 포함하는 상태들의 공통 인터페이스
 */
private interface MusicData {
    val imageUrl: String
    val songName: String
    val artistName: String
}

/**
 * [MusicComponent]의 상태를 나타내는 sealed interface.
 * 각 상태에 따라 필요한 데이터만 포함합니다.
 */
@Immutable
sealed interface MusicComponentData {
    /**
     * 노트 작성 중 곡을 추가하지 않았을 때입니다.
     * 아무 데이터도 필요하지 않습니다.
     */
    data object NoteWriteEmpty : MusicComponentData

    /**
     * 노트 작성 중 곡을 추가했을 때입니다.
     */
    data class NoteWriteMusicExist(
        override val imageUrl: String,
        override val songName: String,
        override val artistName: String,
    ) : MusicComponentData, MusicData

    /**
     * 곡에 대한 노트를 검색할 때입니다.
     * noteCount를 포함한 모든 프로퍼티가 필요합니다.
     */
    data class SearchNoteByMusic(
        override val imageUrl: String,
        override val songName: String,
        override val artistName: String,
        val noteCount: Int,
    ) : MusicComponentData, MusicData

    /**
     * 노트 작성 화면에서 곡 검색 화면으로 넘어가 곡을 찾을 때입니다.
     */
    data class SearchList(
        override val imageUrl: String,
        override val songName: String,
        override val artistName: String,
    ) : MusicComponentData, MusicData

    /**
     * [com.lyrics.feelin.presentation.view.component.note.NoteComponent]와 같이 사용해 곡을 보여줄 때입니다.
     */
    data class NoteComponent(
        override val imageUrl: String,
        override val songName: String,
        override val artistName: String,
    ) : MusicComponentData, MusicData
}
