package com.syrous.hackernews.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Comment(
    @PrimaryKey
    val id: Int,
    val by: String,
    val parentId: Int,
    val postId: Long,
    val text: String,
    val time: Long,
)