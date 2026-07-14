package com.lyrics.feelin.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.lyrics.feelin.presentation.view.home.HomeScreen

@Composable
fun HomeRoute(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    HomeScreen(
        modifier = modifier,
        onBannerClick = { linkUrl ->
            // FIXME(@이대근): 외부 브라우저로 표시 필요 2026.06.17.
            navController.navigate(FeelinDestination.InternalWebView.createRoute(linkUrl))
        },
        onArtistClick = { artistId ->
            navController.navigate(FeelinDestination.ArtistRecord.createRoute(artistId))
        },
        onNoteClick = { noteId ->
            navController.navigate(FeelinDestination.NoteDetail.createRoute(noteId))
        },
        onNoteReportClick = { noteId ->
            navController.navigate(FeelinDestination.NoteReport.createRoute(noteId))
        },
        onNotificationClick = {
            navController.navigate(FeelinDestination.Notification.route)
        }
    )
}
