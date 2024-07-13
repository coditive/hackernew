package com.syrous.hackernews.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.Database
import com.syrous.hackernews.local.HackerNewDB
import com.syrous.hackernews.local.dao.PostDao
import com.syrous.hackernews.local.entities.Post
import com.syrous.hackernews.remote.model.StoryDetail
import com.syrous.hackernews.remote.model.StoryType
import com.syrous.hackernews.usecases.model.RetrievePostAsPageUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator(
    private val db: HackerNewDB,
    private val useCase: RetrievePostAsPageUseCase
) : RemoteMediator<Long, Post>() {

    override suspend fun initialize(): InitializeAction = withContext(Dispatchers.IO) {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)
        if (System.currentTimeMillis() - db.getLastUpdatedTime() >= cacheTimeout) {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        } else {
            InitializeAction.SKIP_INITIAL_REFRESH
        }

    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Long, Post>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND ->
                    return MediatorResult.Success(endOfPaginationReached = true)

                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if (lastItem == null) {
                        MediatorResult.Success(endOfPaginationReached = true)
                    }
                    lastItem?.id
                }
            }

            val response = useCase.retrieveStories(storyType = StoryType.ASK)

            db.providePostDao()
                .insertAllAndUpdateMetaData(response.map { it.toPost() }, db.provideMetadataDao())

            MediatorResult.Success(endOfPaginationReached = true)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}