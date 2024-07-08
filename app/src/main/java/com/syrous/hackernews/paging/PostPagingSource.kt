package com.syrous.hackernews.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.syrous.hackernews.remote.model.StoryDetail
import com.syrous.hackernews.usecases.model.RetrievePostAsPageUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class PostPagingSource(private val useCase: RetrievePostAsPageUseCase) :
    PagingSource<Long, StoryDetail>() {

    override fun getRefreshKey(state: PagingState<Long, StoryDetail>): Long? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, StoryDetail> {
        return try {
            val page = params.key ?: 1
            val pageSize = params.loadSize
            Log.d("PostPagingSource", "load: $page, pageSize: $pageSize")
            val storyDetailList =  useCase.retrievePage(page.toInt())
            LoadResult.Page(
                data = storyDetailList,
                prevKey = if (page.toInt() == 1) null else page - 1,
                nextKey = if (storyDetailList.size < pageSize) null else page + 1
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}