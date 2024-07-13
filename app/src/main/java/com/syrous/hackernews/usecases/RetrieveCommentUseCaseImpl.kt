package com.syrous.hackernews.usecases

import com.syrous.hackernews.remote.model.StoryDetail
import com.syrous.hackernews.usecases.model.RetrieveCommentUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrieveCommentUseCaseImpl(): RetrieveCommentUseCase {

    override suspend fun retrieveComments(storyDetail: StoryDetail) {

    }
}