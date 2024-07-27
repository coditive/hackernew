package com.syrous.hackernews.data.remote.model

import com.syrous.hackernews.data.local.entities.Post

data class StoryDetail(
    val by: String?,
    val descendants: Int?,
    val id: Long,
    val kids: List<Long>?,
    val score: Int?,
    val time: Long,
    val title: String?,
    val url: String?,
) {

    fun toPost(): Post = Post(
        id = id,
        by = by,
        descendants = descendants,
        score = score,
        time = time,
        title = title,
        url = url,
    )
}


