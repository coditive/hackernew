package com.syrous.hackernews.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.syrous.hackernews.local.entities.Metadata
import com.syrous.hackernews.local.entities.Post


@Dao
interface PostDao : BaseDao<Post> {

    @Query("SELECT * FROM post WHERE id = :itemId")
    suspend fun getPostByItemId(itemId: Long): List<Post>

    @Query("SELECT * FROM post WHERE id >= :itemId ORDER BY id LIMIT :pageSize")
    suspend fun getPageSizePostListFromItemId(itemId: Long, pageSize: Int): List<Post>

    @Transaction
    suspend fun insertAndUpdateMetaData(post: Post, metaDataDao: MetaDataDao) {
        insert(post)
        val metaData = Metadata(id = 1, lastUpdatedTime = System.currentTimeMillis())
        metaDataDao.insertOrUpdateMetaData(metaData)
    }

    @Transaction
    suspend fun insertAllAndUpdateMetaData(postList: List<Post>, metaDataDao: MetaDataDao) {
        insertAll(postList)
        val metaData = Metadata(id = 1, lastUpdatedTime = System.currentTimeMillis())
        metaDataDao.insertOrUpdateMetaData(metaData)
    }

}