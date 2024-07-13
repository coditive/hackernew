package com.syrous.hackernews.usecases.model

import com.syrous.hackernews.remote.model.StoryDetail

interface RetrieveCommentUseCase {

    suspend fun retrieveComments(storyDetail: StoryDetail)
}