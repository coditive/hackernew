package com.syrous.hackernews.usecases.model

import com.syrous.hackernews.remote.model.StoryDetail
import kotlinx.coroutines.flow.StateFlow

interface RetrievePostAsPageUseCase {

    suspend fun retrievePage(page: Int): List<StoryDetail>
}