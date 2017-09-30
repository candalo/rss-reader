package com.github.rssreader.features.feed.data.entity

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root


@Root(name = "image") data class FeedImageEntity(@Element val url: String)