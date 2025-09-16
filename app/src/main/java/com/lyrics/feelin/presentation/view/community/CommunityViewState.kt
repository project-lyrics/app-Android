package com.lyrics.feelin.presentation.view.community

import com.lyrics.feelin.presentation.view.component.note.NoteComponentData

enum class CommunityViewStatus {
    INITIAL,
    LOADING,
    SUCCESS_LOAD,
    ERROR,
}

/**
 * 커뮤니티 뷰의 상태를 관리하는 데이터 클래스
 *
 * @property status 로딩 상태
 * @property artist 아티스트 정보
 * @property isViewNoteOnlyLyrics 가사가 있는 노트만 보여줄 지 필터링 여부
 * @property noteState 노트 상태 정보
 * @property errorMessage 에러 메시지 (오류 발생 시에만 값 존재)
 */
data class CommunityViewState(
    val status: CommunityViewStatus,
    val artist: CommunityViewArtistData,
    val isViewNoteOnlyLyrics: Boolean,
    val noteState: CommunityViewNoteState,
    val errorMessage: String? = null,
) {
    companion object {
        fun initial(): CommunityViewState {
            return CommunityViewState(
                status = CommunityViewStatus.INITIAL,
                artist = CommunityViewArtistData.empty(),
                isViewNoteOnlyLyrics = false,
                noteState = CommunityViewNoteState.initial(),
                errorMessage = null,
            )
        }

        fun loading(): CommunityViewState {
            return CommunityViewState(
                status = CommunityViewStatus.LOADING,
                artist = CommunityViewArtistData.empty(),
                isViewNoteOnlyLyrics = false,
                noteState = CommunityViewNoteState.initial(),
                errorMessage = null,
            )
        }

        fun success(
            artistData: CommunityViewArtistData = CommunityViewArtistData.sample(),
            noteData: CommunityViewNoteState = CommunityViewNoteState.sample(),
        ): CommunityViewState {
            return CommunityViewState(
                status = CommunityViewStatus.SUCCESS_LOAD,
                artist = artistData,
                isViewNoteOnlyLyrics = false,
                noteState = noteData,
                errorMessage = null,
            )
        }

        fun error(message: String = "샘플 오류가 발생했습니다."): CommunityViewState {
            return CommunityViewState(
                status = CommunityViewStatus.ERROR,
                artist = CommunityViewArtistData.empty(),
                isViewNoteOnlyLyrics = false,
                noteState = CommunityViewNoteState.initial(),
                errorMessage = message,
            )
        }
    }
}

/**
 * 커뮤니티 뷰에서 표시되는 아티스트 정보를 담는 데이터 클래스
 *
 * @property id 서버에서 받은 아티스트 id
 * @property name 아티스트 이름
 * @property imageUrl 아티스트 이미지 URL
 * @property isLike 사용자가 좋아요 했는 지 여부
 */
data class CommunityViewArtistData(
    val id: Long,
    val name: String,
    val imageUrl: String,
    val isLike: Boolean,
) {
    companion object {
        fun empty(): CommunityViewArtistData {
            return CommunityViewArtistData(id = -1L, name = "", imageUrl = "", isLike = false)
        }

        fun sample(): CommunityViewArtistData {
            return CommunityViewArtistData(
                id = 15,
                name = "쏜애플",
                imageUrl = "https://i.scdn.co/image/ab6761610000e5eb4506d70c02763753b03fd07b",
                isLike = false,
            )
        }
    }
}

enum class CommunityViewNoteStatus {
    INITIAL,
    NEW_PAGE_LOADING,
    SUCCESS_LOAD,
    ERROR,
}

/**
 * 커뮤니티 뷰에서 노트 목록의 상태를 관리하는 데이터 클래스
 *
 * @property status 로딩 상태
 * @property notes 노트들
 */
data class CommunityViewNoteState(
    val status: CommunityViewNoteStatus,
    val notes: List<NoteComponentData>,
) {
    companion object {
        fun initial(): CommunityViewNoteState {
            return CommunityViewNoteState(
                status = CommunityViewNoteStatus.INITIAL,
                notes = emptyList(),
            )
        }

        @Suppress("MagicNumber")
        fun sample(): CommunityViewNoteState {
            return CommunityViewNoteState(
                status = CommunityViewNoteStatus.SUCCESS_LOAD,
                notes =
                List(10) {
                    if (it % 2 == 0) {
                        NoteComponentData.sample()
                    } else {
                        NoteComponentData.sampleNoLyrics()
                    }
                },
            )
        }

        @Suppress("MagicNumber")
        fun pagingSample(): CommunityViewNoteState {
            return CommunityViewNoteState(
                status = CommunityViewNoteStatus.NEW_PAGE_LOADING,
                notes =
                List(19) {
                    if (it % 2 == 0) {
                        NoteComponentData.sample()
                    } else {
                        NoteComponentData.sampleNoLyrics()
                    }
                },
            )
        }

        fun error(): CommunityViewNoteState {
            return CommunityViewNoteState(
                status = CommunityViewNoteStatus.ERROR,
                notes = emptyList(),
            )
        }
    }
}
