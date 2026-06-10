package com.lyrics.feelin.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.lyrics.feelin.presentation.view.component.music.MusicComponentData
import com.lyrics.feelin.presentation.view.note.form.NoteFormRoute
import com.lyrics.feelin.presentation.view.note.form.NoteFormViewModel
import com.lyrics.feelin.presentation.view.note.form.searchsong.SearchSongRoute
import kotlinx.coroutines.flow.StateFlow

private const val SELECTED_SONG_IMAGE_URL_KEY = "selectedSongImageUrl"
private const val SELECTED_SONG_NAME_KEY = "selectedSongName"
private const val SELECTED_SONG_ARTIST_NAME_KEY = "selectedSongArtistName"

fun NavGraphBuilder.noteFormNavGraph(navController: NavHostController) {
    noteFormCreateDestination(navController)

    noteFormEditDestination(navController)

    noteFormSearchSongDestination(navController)
}

private fun NavGraphBuilder.noteFormCreateDestination(navController: NavHostController) {
    composable(FeelinDestination.NoteFormCreate.route) { backStackEntry ->
        NoteFormDestination(
            navController = navController,
            selectedSongImageUrlFlow = backStackEntry.savedStateHandle.getStateFlow(
                SELECTED_SONG_IMAGE_URL_KEY,
                null,
            ),
            selectedSongNameFlow = backStackEntry.savedStateHandle.getStateFlow(SELECTED_SONG_NAME_KEY, null),
            selectedSongArtistNameFlow = backStackEntry.savedStateHandle.getStateFlow(
                SELECTED_SONG_ARTIST_NAME_KEY,
                null,
            ),
            clearSelectedSongResult = {
                backStackEntry.savedStateHandle.remove<String>(SELECTED_SONG_IMAGE_URL_KEY)
                backStackEntry.savedStateHandle.remove<String>(SELECTED_SONG_NAME_KEY)
                backStackEntry.savedStateHandle.remove<String>(SELECTED_SONG_ARTIST_NAME_KEY)
            },
        )
    }
}

private fun NavGraphBuilder.noteFormEditDestination(navController: NavHostController) {
    composable(
        route = FeelinDestination.NoteFormEdit.route,
        arguments = listOf(navArgument(FeelinDestination.NoteFormEdit.NoteIdArgument) { type = NavType.LongType }),
    ) { backStackEntry ->
        NoteFormDestination(
            navController = navController,
            selectedSongImageUrlFlow = backStackEntry.savedStateHandle.getStateFlow(
                SELECTED_SONG_IMAGE_URL_KEY,
                null,
            ),
            selectedSongNameFlow = backStackEntry.savedStateHandle.getStateFlow(SELECTED_SONG_NAME_KEY, null),
            selectedSongArtistNameFlow = backStackEntry.savedStateHandle.getStateFlow(
                SELECTED_SONG_ARTIST_NAME_KEY,
                null,
            ),
            clearSelectedSongResult = {
                backStackEntry.savedStateHandle.remove<String>(SELECTED_SONG_IMAGE_URL_KEY)
                backStackEntry.savedStateHandle.remove<String>(SELECTED_SONG_NAME_KEY)
                backStackEntry.savedStateHandle.remove<String>(SELECTED_SONG_ARTIST_NAME_KEY)
            },
        )
    }
}

private fun NavGraphBuilder.noteFormSearchSongDestination(navController: NavHostController) {
    composable(
        route = FeelinDestination.NoteFormSearchSong.route,
        arguments = listOf(
            navArgument(FeelinDestination.NoteFormSearchSong.ArtistIdArgument) { type = NavType.LongType }
        ),
    ) {
        SearchSongRoute(
            onBackClick = { navController.popBackStack() },
            onSongSelect = { song ->
                navController.previousBackStackEntry?.savedStateHandle?.apply {
                    set(SELECTED_SONG_IMAGE_URL_KEY, song.imageUrl)
                    set(SELECTED_SONG_NAME_KEY, song.songName)
                    set(SELECTED_SONG_ARTIST_NAME_KEY, song.artistName)
                }
                navController.popBackStack()
            },
        )
    }
}

@Composable
private fun NoteFormDestination(
    navController: NavHostController,
    selectedSongImageUrlFlow: StateFlow<String?>,
    selectedSongNameFlow: StateFlow<String?>,
    selectedSongArtistNameFlow: StateFlow<String?>,
    clearSelectedSongResult: () -> Unit,
    viewModel: NoteFormViewModel = hiltViewModel(),
) {
    val selectedSongImageUrl by selectedSongImageUrlFlow.collectAsState()
    val selectedSongName by selectedSongNameFlow.collectAsState()
    val selectedSongArtistName by selectedSongArtistNameFlow.collectAsState()
    val currentClearSelectedSongResult by rememberUpdatedState(clearSelectedSongResult)

    LaunchedEffect(selectedSongImageUrl, selectedSongName, selectedSongArtistName) {
        val imageUrl = selectedSongImageUrl ?: return@LaunchedEffect
        val songName = selectedSongName ?: return@LaunchedEffect
        val artistName = selectedSongArtistName ?: return@LaunchedEffect

        viewModel.setSong(
            MusicComponentData.NoteWriteMusicExist(
                imageUrl = imageUrl,
                songName = songName,
                artistName = artistName,
            )
        )
        currentClearSelectedSongResult()
    }

    NoteFormRoute(
        onCloseClick = { navController.popBackStack() },
        onNavigateToSearchSong = {
            // REST API 연동 전까지 검색 화면에 넘기는 artistId는 임시값입니다.
            navController.navigate(FeelinDestination.NoteFormSearchSong.createRoute(artistId = 0L))
        },
    )
}
