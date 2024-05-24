package com.bobbyesp.navigationbugreport.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Stable
@Composable
fun PlaceholderCreator(
    modifier: Modifier = Modifier,
    icon: ImageVector?,
    colorful: Boolean,
    contentDescription: String? = null
) {
    Surface(
        tonalElevation = if (colorful) 0.dp else 8.dp,
        color = if (colorful) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
        modifier = modifier
    ) {
        icon?.let {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Icon(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    imageVector = icon,
                    contentDescription = contentDescription,
                    tint = if (colorful) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}
