package com.syrous.hackernews.usecases

import android.util.Log
import com.syrous.hackernews.remote.ApiService
import com.syrous.hackernews.remote.model.StoryDetail
import com.syrous.hackernews.usecases.model.RetrievePostAsPageUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext


// 1 page = 20 list items
const val PAGE_SIZE = 20
class RetrievePostAsPagesUseCaseImpl(private val apiService: ApiService) :
    RetrievePostAsPageUseCase {

    override suspend fun retrievePage(page: Int): List<StoryDetail> {
        val itemIdList = apiService.getAskHNItemList().drop(PAGE_SIZE * page - 1).take(PAGE_SIZE)
        return itemIdList.map { id ->
            apiService.getStoryFromItemId(id)
        }
    }
}