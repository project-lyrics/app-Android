package com.lyrics.feelin.presentation.view.community

import com.lyrics.feelin.presentation.view.component.note.NoteComponentData

enum class CommunityViewStatus {
    INITIAL,
    LOADING,
    SUCCESS_LOAD,
    ERROR,
}

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

        fun sample(): CommunityViewNoteState {
            return CommunityViewNoteState(
                status = CommunityViewNoteStatus.SUCCESS_LOAD,
                notes =
                    List(10) {
                        if (it % 2 == 0) NoteComponentData.sample()
                        else NoteComponentData.sampleNoLyrics()
                    },
            )
        }

        fun pagingSample(): CommunityViewNoteState {
            return CommunityViewNoteState(
                status = CommunityViewNoteStatus.NEW_PAGE_LOADING,
                notes =
                    List(19) {
                        if (it % 2 == 0) NoteComponentData.sample()
                        else NoteComponentData.sampleNoLyrics()
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
