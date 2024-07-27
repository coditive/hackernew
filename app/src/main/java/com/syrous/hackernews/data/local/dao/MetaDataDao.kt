package com.syrous.hackernews.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.syrous.hackernews.data.local.entities.Metadata
import com.syrous.hackernews.data.local.entities.Post

@Dao
interface MetaDataDao {

    @Query("SELECT lastUpdatedTime FROM metadata WHERE id = 1")
    fun getLastUpdatedTime(): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateMetaData(data: Metadata)
}