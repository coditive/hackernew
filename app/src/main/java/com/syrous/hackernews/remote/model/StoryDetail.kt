package com.syrous.hackernews.remote.model

data class StoryDetail(
    val by: String?,
    val descendants: Int?,
    val id: Int,
    val kids: List<Long>?,
    val score: Int?,
    val time: Long,
    val title: String?,
    val url: String?,
)
