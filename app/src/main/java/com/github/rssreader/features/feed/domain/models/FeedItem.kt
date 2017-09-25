package com.github.rssreader.features.feed.domain.models

import java.text.SimpleDateFormat


data class FeedItem(val title: String, val link: String, val description: String,
                    val authorEmail: String, val category: String, val commentsLink: String,
                    val pubDate: SimpleDateFormat)