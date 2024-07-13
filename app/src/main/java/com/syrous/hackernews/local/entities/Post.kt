package com.syrous.hackernews.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Post(
    @PrimaryKey
    val id: Long,
    val by: String?,
    val descendants: Int?,
    val score: Int?,
    val time: Long,
    val title: String?,
    val url: String?,
)