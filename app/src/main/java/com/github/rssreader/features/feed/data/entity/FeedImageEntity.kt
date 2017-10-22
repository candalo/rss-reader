package com.github.rssreader.features.feed.data.entity

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root


@Root(name = "image", strict = false)
data class FeedImageEntity(@field:Element(name = "url") var url: String = "")