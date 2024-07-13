package com.syrous.hackernews.local

import androidx.room.Database
import androidx.room.DatabaseConfiguration
import androidx.room.InvalidationTracker
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.syrous.hackernews.local.dao.MetaDataDao
import com.syrous.hackernews.local.dao.PostDao
import com.syrous.hackernews.local.entities.Comment
import com.syrous.hackernews.local.entities.Metadata
import com.syrous.hackernews.local.entities.Post


@Database(entities = [Post::class, Metadata::class, Comment::class], version = 1)
abstract class HackerNewDB : RoomDatabase() {

    abstract fun providePostDao(): PostDao

    abstract fun provideMetadataDao(): MetaDataDao

    suspend fun getLastUpdatedTime(): Long {
        return provideMetadataDao().getLastUpdatedTime()
    }

}