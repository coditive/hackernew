package com.syrous.hackernews.domain.paging

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.scan

enum class RemotePresentationState {
    INITIAL, REMOTE_LOADING, SOURCE_LOADING, PRESENTED
}


fun CombinedLoadStates.asRemotePresentationState(): RemotePresentationState {
    return when {
        mediator?.refresh is LoadState.Loading -> RemotePresentationState.REMOTE_LOADING
        source.refresh is LoadState.Loading -> RemotePresentationState.SOURCE_LOADING
        source.refresh is LoadState.NotLoading -> RemotePresentationState.PRESENTED
        else -> RemotePresentationState.INITIAL
    }
}
