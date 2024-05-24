package com.bobbyesp.navigationbugreport

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.bobbyesp.navigationbugreport.domain.model.Song
import com.bobbyesp.navigationbugreport.ui.common.Home
import com.bobbyesp.navigationbugreport.ui.common.MainNavigator
import com.bobbyesp.navigationbugreport.ui.common.ParcelableSongNavType
import com.bobbyesp.navigationbugreport.ui.common.SongInformationPage
import com.bobbyesp.navigationbugreport.ui.common.UtilitiesNavigator
import com.bobbyesp.navigationbugreport.ui.components.ArtworkAsyncImage
import com.bobbyesp.navigationbugreport.ui.components.permissions.PermissionRequestHandler
import com.bobbyesp.navigationbugreport.ui.theme.NavigationBugReportTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.reflect.typeOf

class MainActivity : ComponentActivity() {
    val mainViewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val currentApiVersion = Build.VERSION.SDK_INT
            val readAudioFiles = when {
                currentApiVersion < Build.VERSION_CODES.TIRAMISU -> Manifest.permission.READ_EXTERNAL_STORAGE

                else -> Manifest.permission.READ_MEDIA_AUDIO
            }

            val storagePermissionState = rememberPermissionState(permission = readAudioFiles)

            val navController = rememberNavController()
            NavigationBugReportTheme {
                val lazyListState = rememberLazyListState()
                val pageViewState = mainViewModel.pageViewState.collectAsStateWithLifecycle()
                val songs = pageViewState.value.songs
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        navController = navController,
                        startDestination = MainNavigator,
                    ) {
                        navigation<MainNavigator>(
                            startDestination = Home,
                        ) {
                            composable<Home> {
                                PermissionRequestHandler(
                                    permissionState = storagePermissionState,
                                    deniedContent = { shouldShowRationale ->
                                        AlertDialog(
                                            onDismissRequest = {
                                                if (shouldShowRationale) {
                                                    storagePermissionState.launchPermissionRequest()
                                                } else {
                                                    storagePermissionState.launchPermissionRequest()
                                                }
                                            },
                                            title = {
                                                Text(text = "Permission Required")
                                            },
                                            text = {
                                                Text(text = "This permission is required to access your audio files.")
                                            },
                                            confirmButton = {
                                                Text(text = "OK")
                                            }
                                        )
                                    }
                                ) {
                                    SongsList(
                                        songs = songs,
                                        lazyListState = lazyListState,
                                        onItemClicked = { song ->
                                            navController.navigate(SongInformationPage(song))
                                        }
                                    )
                                }
                            }
                        }
                        navigation<UtilitiesNavigator>(
                            startDestination = SongInformationPage,
                        ) {
                            composable<SongInformationPage>(
                                typeMap = mapOf(typeOf<Song>() to ParcelableSongNavType)
                            ) {
                                val song = it.toRoute<SongInformationPage>().song
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = song.title,
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp
                                    )
                                    Text(
                                        text = song.artist,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontSize = 16.sp
                                    )
                                }
                            }
                        }
                    }

                }
            }
        }
    }
}

class MainViewModel : ViewModel() {
    private val mutablePageViewState = MutableStateFlow(ViewState())
    val pageViewState = mutablePageViewState.asStateFlow()

    data class ViewState(
        val songs: List<Song> = emptyList()
    )
}

@Composable
private fun SongsList(
    songs: List<Song>,
    lazyListState: LazyListState,
    onItemClicked: (Song) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        state = lazyListState,
    ) {
        items(count = songs.size,
            key = { index -> songs[index].id },
            contentType = { index -> songs[index].id.toString() }) { index ->
            val song = songs[index]
            HorizontalSongCard(song = song, modifier = Modifier.animateItem(
                fadeInSpec = null, fadeOutSpec = null
            ), onClick = {
                onItemClicked(song)
            })
        }
    }
}

@Composable
private fun HorizontalSongCard(
    modifier: Modifier = Modifier,
    song: Song,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            ArtworkAsyncImage(
                modifier = Modifier
                    .size(64.dp)
                    .padding(4.dp),
                artworkPath = song.artworkPath
            )
            Column(
                horizontalAlignment = Alignment.Start, modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 6.dp)
                    .weight(1f)
            ) {
                Text(
                    text = song.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Text(
                    text = song.artist,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    ),
                    fontSize = 12.sp
                )
            }
        }
    }
}