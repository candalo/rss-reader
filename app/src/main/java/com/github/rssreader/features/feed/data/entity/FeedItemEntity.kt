package com.github.rssreader.features.feed.data.entity

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root


@Root(name = "item", strict = false)
data class FeedItemEntity(
        @field:Element(name = "title", required = false)
        var title: String = "",
        @field:Element(name = "link", required = false)
        var link: String = "",
        @field:Element(name = "description", required = false)
        var description: String = "",
        @field:Element(name = "author", required = false)
        var authorEmail: String = "",
        @field:ElementList(entry = "category", inline = true, required = false)
        var categories: List<String> = ArrayList(),
        @field:Element(name = "pubDate", required = false)
        var pubDate: String = ""
)