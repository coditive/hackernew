package com.syrous.hackernews.usecases.model

import com.syrous.hackernews.remote.model.StoryDetail
import com.syrous.hackernews.remote.model.StoryType
import kotlinx.coroutines.flow.StateFlow

interface RetrievePostAsPageUseCase {

    suspend fun retrieveStories(storyType: StoryType): List<StoryDetail>
}