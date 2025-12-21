package com.lyrics.feelin.presentation.view.component.music

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.lyrics.feelin.R
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTypography
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors

@Composable
fun MusicComponent(data: MusicComponentData, modifier: Modifier = Modifier) {
    val feelinColors = LocalFeelinColors.current

    HorizontalDivider(color = feelinColors.gray01, thickness = 1.dp)
    Row(
        modifier = Modifier.fillMaxWidth().height(64.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = data.imageUrl,
                modifier = Modifier.size(40.dp).clip(shape = RoundedCornerShape(4.dp)),
                contentDescription = "${data.songName}'s album art",
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = data.songName,
                    style = FeelinTypography.body2.copy(color = feelinColors.gray08)
                )
                Text(
                    text = data.artistName,
                    style = FeelinTypography.caption1.copy(color = feelinColors.gray04),
                )
            }
        }
        Image(
            painter = painterResource(R.drawable.play),
            contentDescription = "${data.songName} play",
            modifier = Modifier.size(24.dp),
            colorFilter = ColorFilter.tint(feelinColors.gray03),
        )
    }
    HorizontalDivider(color = feelinColors.gray01, thickness = 1.dp)
}
