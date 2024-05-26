package com.bobbyesp.navigationbugreport.domain.model

import android.os.Parcelable
import androidx.compose.runtime.Stable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Stable
@Parcelize
data class Song(
    val id: Long,
    val title: String,
    val artist: String,
    val album: String,
    val artworkPath: String? = null,
    val duration: Double,
    val path: String,
    val fileName: String
) : Parcelable {
    companion object {
        val empty = Song(
            id = -1,
            title = "",
            artist = "",
            album = "",
            duration = 0.0,
            path = "",
            fileName = ""
        )
    }
}