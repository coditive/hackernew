package com.syrous.hackernews.domain.usecases.model

import com.syrous.hackernews.data.remote.model.StoryDetail

interface RetrieveCommentUseCase {

    suspend fun retrieveComments(storyDetail: StoryDetail)
}