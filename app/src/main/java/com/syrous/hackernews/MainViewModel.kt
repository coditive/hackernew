package com.syrous.hackernews

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.room.Room
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.syrous.hackernews.local.HackerNewDB
import com.syrous.hackernews.paging.PagingConfig.PAGE_SIZE
import com.syrous.hackernews.paging.PostPagingSource
import com.syrous.hackernews.paging.StoryRemoteMediator
import com.syrous.hackernews.remote.ApiService
import com.syrous.hackernews.remote.model.CommentDetail
import com.syrous.hackernews.remote.model.StoryDetail
import com.syrous.hackernews.usecases.RetrievePostAsPagesUseCaseImpl
import com.syrous.hackernews.usecases.model.RetrievePostAsPageUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MainViewModel(private val application: Application) : AndroidViewModel(application),
    MainModel, PostManager {

    init {
        provideMoshi()
        provideRetrofit()
    }

    //private variables
    private lateinit var apiClient: ApiService
    private lateinit var moshi: Moshi

    // public variables
    override val postList: MutableStateFlow<List<StoryDetail>> = MutableStateFlow(listOf())
    override val commentList: MutableStateFlow<List<CommentDetail>> = MutableStateFlow(listOf())
    private val itemList = mutableListOf<Long>()
    private val retrieveUseCase: RetrievePostAsPageUseCase =
        RetrievePostAsPagesUseCaseImpl(apiClient)

    private val database: HackerNewDB = Room.databaseBuilder(
        application,
        HackerNewDB::class.java, "hacker_news_db"
    ).build()

    @OptIn(ExperimentalPagingApi::class)
    val post = Pager(
        config = PagingConfig(
            PAGE_SIZE,
            enablePlaceholders = false,
            initialLoadSize = PAGE_SIZE // Initially load 3 pages of PAGE_SIZE bcz of multiplier in library
        ),
        remoteMediator = StoryRemoteMediator(database, useCase = retrieveUseCase),
        pagingSourceFactory = { PostPagingSource(database.providePostDao()) })
        .flow.cachedIn(viewModelScope)

    private fun provideRetrofit() {
        apiClient = Retrofit.Builder().client(OkHttpClient.Builder().also {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BODY
            it.addInterceptor(logger)
        }.build()).addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
            .baseUrl("https://hacker-news.firebaseio.com/").build().create(ApiService::class.java)
    }

    private fun provideMoshi() {
        moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
    }

    override fun getAskStoriesPost() {
        viewModelScope.launch(Dispatchers.IO) {
            itemList.addAll(async { apiClient.getAskHNItemList() }.await())
            val posts = itemList.map { id ->
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

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                MainViewModel(this[APPLICATION_KEY] as Application)
            }
        }
    }

}