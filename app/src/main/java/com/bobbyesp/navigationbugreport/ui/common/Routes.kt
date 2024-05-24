package com.bobbyesp.navigationbugreport.ui.common

import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.navigation.NavType
import com.bobbyesp.navigationbugreport.domain.model.Song
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
object MainHost

@Serializable
object MainNavigator

@Serializable
object Home

@Serializable
object UtilitiesNavigator

@Serializable
data class SongInformationPage(
    val song: Song
)

val ParcelableSongNavType = object : NavType<Song>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): Song? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, Song::class.java)
        } else {
            bundle.getParcelable(key)
        }
    }

    override fun put(bundle: Bundle, key: String, value: Song) {
        bundle.putParcelable(key, value)
    }

    override fun parseValue(value: String): Song {
        return Json.decodeFromString(Uri.decode(value))
    }
}