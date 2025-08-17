package com.lyrics.feelin.presentation.view.component.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ProfileComponent(type: ProfileType, size: Int, modifier: Modifier = Modifier) {
    val profileData = remember { mutableStateOf(ProfileComponentData.fromProfileType(type)) }

    Image(
        painter = painterResource(profileData.value.enableAsset),
        contentDescription = "User Profile Image",
        modifier = Modifier.size(size.dp),
    )
}

@Preview
@Composable
private fun ProfileComponentPreview() {
    ProfileComponent(type = ProfileType.POOP_HAIR, size = 32)
}
