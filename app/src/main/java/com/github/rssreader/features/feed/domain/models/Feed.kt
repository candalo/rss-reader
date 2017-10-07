package com.github.rssreader.features.feed.domain.models

import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


data class Feed(
        val title: String,
        val link: String,
        val description: String,
        val language: String = "",
        val managingEditorEmail: String = "",
        val pubDate: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()),
        val lastBuildDate: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()),
        val category: String = "",
        val image: FeedImage = FeedImage(),
        val items: ArrayList<FeedItem> = ArrayList()
)