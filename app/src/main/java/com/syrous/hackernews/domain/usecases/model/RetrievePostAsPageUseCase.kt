package com.syrous.hackernews.domain.usecases.model

import com.syrous.hackernews.data.remote.model.StoryDetail
import com.syrous.hackernews.data.remote.model.StoryType

interface RetrievePostAsPageUseCase {

    suspend fun retrieveStories(storyType: StoryType): List<StoryDetail>
}