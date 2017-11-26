package com.github.rssreader.features.feed.domain.models


data class FeedChannel(
        val title: String,
        val link: String,
        val description: String,
        val language: String = "",
        val managingEditorEmail: String = "",
        val pubDate: String = "",
        val lastBuildDate: String = "",
        val category: String = "",
        val image: FeedImage = FeedImage(),
        val items: ArrayList<FeedItem> = ArrayList()
)