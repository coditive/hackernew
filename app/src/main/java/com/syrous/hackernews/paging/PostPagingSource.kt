package com.syrous.hackernews.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.syrous.hackernews.local.dao.PostDao
import com.syrous.hackernews.local.entities.Post
import com.syrous.hackernews.remote.model.StoryDetail
import com.syrous.hackernews.usecases.model.RetrievePostAsPageUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class PostPagingSource(private val postDao: PostDao) : PagingSource<Long, Post>() {

    private var pageTopItem: Post? = null

    override fun getRefreshKey(state: PagingState<Long, Post>): Long? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, Post> =
        withContext(Dispatchers.IO) {
            try {
                val page = params.key ?: 1
                val pageSize = params.loadSize

                val storyDetailList = when {
                    pageTopItem == null -> postDao.getPageSizePostListFromItemId(0, pageSize)

                    else -> postDao.getPageSizePostListFromItemId(pageTopItem?.id!!, pageSize)
                }

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