package com.example.tvmaze.ui.main

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.tvmaze.R
import com.example.tvmaze.model.MainScreenState
import com.example.tvmaze.model.TvNetwork
import com.example.tvmaze.model.TvShow
import com.example.tvmaze.repository.ShowRepositoryImpl
import com.example.tvmaze.ui.theme.TvMazeTheme

@Composable
fun MainScreen(
    mainScreenViewModel: MainScreenViewModel = viewModel(factory = MainScreenViewModelFactory(ShowRepositoryImpl()))
) {
    val mainScreenState by mainScreenViewModel.uiState.collectAsState()
    Surface(modifier = Modifier.fillMaxSize(), color = Color.LightGray) {
        when {
            !isOnline(LocalContext.current) -> NoOnlineScreen()
            mainScreenState?.channelSchedule?.isNotEmpty() == true -> ScheduleTable(mainScreenState = mainScreenState)
            else -> LoadingScreen()
        }
    }
}

@Composable
fun NoOnlineScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = stringResource(R.string.no_online), textAlign = TextAlign.Center)
    }
}

@Composable
fun LoadingScreen() {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        CircularProgressIndicator()
        Text(text = stringResource(R.string.loading), modifier = Modifier.padding(8.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TvMazeTheme {
        MainScreen()
    }
}

@Composable
fun ScheduleTable(mainScreenState: MainScreenState?) {
    val chanelList = mainScreenState?.channelSchedule.orEmpty()
    LazyColumn {
        items(chanelList) { channel ->
            Row {
                Column {
                    NetworkCell(tvNetwork = channel.tvNetwork)
                }
                LazyRow {
                    items(channel.scheduledShows) { show ->
                        ShowCell(show = show)
                    }
                }
            }
        }
    }
}

@Composable
fun NetworkCell(tvNetwork: TvNetwork) {
    // Cell with the network name
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.Blue,
        ),
        modifier = Modifier
            .padding(8.dp)
            .height(200.dp)
            .width(100.dp)
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier
            .padding(8.dp)
            .fillMaxSize()) {
            Text(
                text = tvNetwork.name.orEmpty(),
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ShowCell(show: TvShow) {
    val openDialog = remember { mutableStateOf(Pair(false, TvShow())) }

    if (openDialog.value.first) {
        ShowDetails(show = openDialog.value.second) {
            openDialog.value = Pair(false, TvShow())
        }
    }

    Card(
        onClick = {
            openDialog.value = Pair(true, show)
        },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        modifier = Modifier
            .padding(8.dp)
            .height(200.dp)
            .width(300.dp)
    ) {
        Row(modifier = Modifier
            .padding(8.dp)
            .fillMaxSize()) {
            GlideImage(
                model = show.image?.medium,
                contentDescription = show.name.orEmpty(),
                modifier = Modifier.size(width = 140.dp, height = 184.dp)
            ) {
                it.transform(RoundedCorners(10))
            }
            Column {
                Text(
                    text = show.name.orEmpty(),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(8.dp)
                )
                Text(
                    text = show.schedule?.time.orEmpty(),
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun ShowDetails(show: TvShow, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss
    ) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.large
        ) {
            LazyColumn {
                item {
                    GlideImage(
                        model = show.image?.original ?: show.image?.medium,
                        contentDescription = show.name.orEmpty(),
                        modifier = Modifier.fillMaxSize()
                    )
                }
                item {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = show.name.orEmpty(),
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(8.dp)
                        )
                        Text(
                            text = stringResource(R.string.status),
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(8.dp)
                        )
                        Text(
                            text = if (show.status?.isNotEmpty() == true) show.status else stringResource(R.string.unknown),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(8.dp)
                        )
                        Text(
                            text = stringResource(R.string.type),
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(8.dp)
                        )
                        Text(
                            text = if (show.type?.isNotEmpty() == true) show.type else stringResource(R.string.unknown),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(8.dp)
                        )
                        Text(
                            text = stringResource(R.string.summary),
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(8.dp)
                        )
                        Text(
                            text = HtmlCompat.fromHtml(show.summary.orEmpty(), HtmlCompat.FROM_HTML_MODE_LEGACY).toString(),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}

fun isOnline(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    if (capabilities != null) {
        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            return true
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            return true
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
            return true
        }
    }
    return false
}