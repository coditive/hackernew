package com.syrous.hackernews.usecases

import com.syrous.hackernews.remote.ApiService
import com.syrous.hackernews.remote.model.StoryDetail
import com.syrous.hackernews.remote.model.StoryType
import com.syrous.hackernews.usecases.model.RetrievePostAsPageUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext


// 1 page = 20 list items
class RetrievePostAsPagesUseCaseImpl(private val apiService: ApiService) : RetrievePostAsPageUseCase {
    private val postRetrieveDispatcher = Dispatchers.IO.limitedParallelism(10)
    override suspend fun retrieveStories(storyType: StoryType): List<StoryDetail> =
        withContext(postRetrieveDispatcher) {
            val stories = mutableListOf<StoryDetail>()
            when (storyType) {
                StoryType.ASK -> {
                    apiService.getAskHNItemList().forEach {
                        stories.add(async {
                            apiService.getStoryFromItemId(it)
                        }.await())
                    }
                }

                StoryType.SHOW -> TODO()
                StoryType.JOB -> TODO()
            }
            stories
        }
}