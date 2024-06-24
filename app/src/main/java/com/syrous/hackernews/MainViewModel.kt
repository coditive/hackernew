package com.syrous.hackernews

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.syrous.hackernews.remote.ApiService
import com.syrous.hackernews.remote.model.CommentDetail
import com.syrous.hackernews.remote.model.StoryDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MainViewModel : ViewModel(), MainModel, PostManager {

    //private variables
    private lateinit var apiClient: ApiService
    private lateinit var moshi: Moshi

    // public variables
    override val postList: MutableStateFlow<List<StoryDetail>> = MutableStateFlow(listOf())
    override val commentList: MutableStateFlow<List<CommentDetail>> = MutableStateFlow(listOf())

    init {
        provideMoshi()
        provideRetrofit()
    }

    private fun provideRetrofit() {
        apiClient = Retrofit.Builder().client(OkHttpClient.Builder().also {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BASIC
            it.addInterceptor(logger)
        }.build())
            .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
            .baseUrl("https://hacker-news.firebaseio.com/").build()
            .create(ApiService::class.java)
    }

    private fun provideMoshi() {
        moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
    }

    override fun getAskStoriesPost() {
        viewModelScope.launch(Dispatchers.IO) {
            val itemList = async { apiClient.getAskHNItemList() }
            val posts = itemList.await().map { id ->
                apiClient.getStoryFromItemId(id)
            }
            postList.emit(posts)
        }
    }

    override fun getCommentForStory(storyDetail: StoryDetail) {
        viewModelScope.launch(Dispatchers.IO) {
            val comments = async {
                storyDetail.kids?.map { id ->
                    apiClient.getCommentFromId(id)
                }
            }.await()

            Log.d("MainViewModel", "Comments -> $comments")

            commentList.emit(comments ?: emptyList())
        }
    }
}