package com.syrous.hackernews.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Metadata(
    @PrimaryKey
    val id: Int,
    val lastUpdatedTime: Long
)