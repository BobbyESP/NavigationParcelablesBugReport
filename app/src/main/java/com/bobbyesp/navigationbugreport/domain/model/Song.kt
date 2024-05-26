package com.bobbyesp.navigationbugreport.domain.model

import android.os.Parcelable
import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable

@Serializable
@Stable
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
    constructor(parcel: android.os.Parcel) : this(
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString(),
        parcel.readDouble(),
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: android.os.Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(title)
        parcel.writeString(artist)
        parcel.writeString(album)
        parcel.writeString(artworkPath)
        parcel.writeDouble(duration)
        parcel.writeString(path)
        parcel.writeString(fileName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Song> {
        override fun createFromParcel(parcel: android.os.Parcel): Song {
            return Song(parcel)
        }

        override fun newArray(size: Int): Array<Song?> {
            return arrayOfNulls(size)
        }

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