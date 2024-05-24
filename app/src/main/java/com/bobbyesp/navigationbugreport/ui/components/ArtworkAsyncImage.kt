package com.bobbyesp.navigationbugreport.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter

@Composable
fun ArtworkAsyncImage(
    modifier: Modifier = Modifier,
    imageModifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.small,
    artworkPath: Any? = null
) {
    var showArtwork by remember { mutableStateOf(true) }

    val model by remember(artworkPath) {
        mutableStateOf(artworkPath)
    }

    LaunchedEffect(model) {
        showArtwork = model != null
    }

    if (model != null && showArtwork) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center,
        ) {
            AsyncImage(
                modifier = imageModifier
                    .fillMaxSize()
                    .clip(shape),
                model = model!!,
                onState = { state ->
                    //if it was successful, don't show the placeholder, else show it
                    showArtwork =
                        state !is AsyncImagePainter.State.Error && state !is AsyncImagePainter.State.Empty
                },
                contentDescription = "Song cover",
                contentScale = ContentScale.Crop,
            )
        }
    } else {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center,
        ) {
            PlaceholderCreator(
                modifier = imageModifier
                    .fillMaxSize()
                    .clip(shape),
                icon = Icons.Default.Refresh,
                colorful = false,
                contentDescription = "Song cover placeholder"
            )
        }
    }
}

