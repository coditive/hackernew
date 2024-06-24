package com.syrous.hackernews.remote

import com.syrous.hackernews.remote.model.CommentDetail
import com.syrous.hackernews.remote.model.StoryDetail
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("v0/askstories.json")
    suspend fun getAskHNItemList(): List<Long>

    @GET("v0/item/{itemId}.json")
    suspend fun getStoryFromItemId(@Path("itemId") id: Long): StoryDetail

    @GET("v0/item/{itemId}.json")
    suspend fun getCommentFromId(@Path("itemId") id: Long): CommentDetail
}