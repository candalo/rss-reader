package com.github.rssreader.features.feed.data.entity

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root


@Root(name = "channel", strict = false)
data class FeedChannelEntity(
        @field:Element(name = "title")
        var title: String = "",
        @field:ElementList(entry = "link", inline = true)
        var links: List<String> = ArrayList(),
        @field:Element(name = "description", required = false)
        var description: String = "",
        @field:Element(name = "language", required = false)
        var language: String = "",
        @field:Element(name = "managingEditor", required = false)
        var managingEditorEmail: String = "",
        @field:Element(name = "pubDate", required = false)
        var pubDate: String = "",
        @field:Element(name = "lastBuildDate", required = false)
        var lastBuildDate: String = "",
        @field:Element(name = "category", required = false)
        var category: String = "",
        @field:Element(name = "image", required = false)
        var image: FeedImageEntity = FeedImageEntity(),
        @field:ElementList(inline = true, name = "item", type = FeedItemEntity::class, required = false)
        var items: ArrayList<FeedItemEntity> = ArrayList()
)