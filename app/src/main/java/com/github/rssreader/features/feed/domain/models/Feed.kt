package com.github.rssreader.features.feed.domain.models

import java.text.SimpleDateFormat


data class Feed(val title: String, val link: String, val description: String, val language: String,
                val managingEditorEmail: String, val pubDate: SimpleDateFormat,
                val lastBuildDate: SimpleDateFormat, val category: String, val image: FeedImage,
                val items: ArrayList<FeedItem>)