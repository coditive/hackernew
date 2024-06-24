package com.syrous.hackernews.remote.model

data class CommentDetail (
    val by: String,
    val id: Int,
    val kids: List<Long>?,
    val parent: Long,
    val text: String,
    val time: Long,
    val type: String
)