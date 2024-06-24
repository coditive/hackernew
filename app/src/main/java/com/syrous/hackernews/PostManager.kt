package com.syrous.hackernews

import com.syrous.hackernews.remote.model.CommentDetail
import com.syrous.hackernews.remote.model.StoryDetail
import kotlinx.coroutines.flow.StateFlow

interface PostManager {

    val postList: StateFlow<List<StoryDetail>>

    val commentList: StateFlow<List<CommentDetail>>

    fun getAskStoriesPost()

    fun getCommentForStory(storyDetail: StoryDetail)

}