package com.syrous.hackernews

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.syrous.hackernews.domain.paging.RemotePresentationState
import com.syrous.hackernews.domain.paging.asRemotePresentationState
import com.syrous.hackernews.ui.theme.HackernewsReaderTheme

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel> { MainViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HackernewsReaderTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    ) {
                        val postList = viewModel.post.collectAsLazyPagingItems()
                        val pagerState = postList.loadState.asRemotePresentationState()
                        val listState = rememberLazyListState()
                        val shouldLoadMore by remember {
                            derivedStateOf {
                                val lastVisibleItemIndex =
                                    listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                                val totalItemCount = listState.layoutInfo.totalItemsCount
                                // Adjust the threshold value as needed
                                val threshold = 5
                                lastVisibleItemIndex != null && lastVisibleItemIndex >= totalItemCount - threshold
                            }
                        }
                        LazyColumn(modifier = Modifier.fillMaxSize(), state = listState) {
                            when (pagerState) {
                                RemotePresentationState.INITIAL,
                                RemotePresentationState.REMOTE_LOADING,
                                RemotePresentationState.SOURCE_LOADING -> {
                                    item {
                                        Box(modifier = Modifier.fillParentMaxSize()) {
                                            CircularProgressIndicator(
                                                modifier = Modifier.align(
                                                    Alignment.Center
                                                )
                                            )
                                        }
                                    }
                                }

                                RemotePresentationState.PRESENTED -> {
                                    if(postList.itemCount == 0){
                                        postList.refresh()
                                    }

                                    items(
                                        count = postList.itemCount,
                                        key = postList.itemKey(),
                                        contentType = postList.itemContentType()
                                    ) {
                                        Text(text = "${postList[it]}")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HackernewsReaderTheme {
        Greeting("Android")
    }
}