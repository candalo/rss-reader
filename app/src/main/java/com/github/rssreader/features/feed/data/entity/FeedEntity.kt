package com.github.rssreader.features.feed.data.entity

import org.simpleframework.xml.*
import java.text.SimpleDateFormat

@Root(name = "channel")
data class FeedEntity(
        @Element
        val title: String,
        @Element
        val link: String,
        @Element
        val description: String,
        @Element
        val language: String,
        @Element(name = "managingEditor")
        val managingEditorEmail: String,
        @Element
        val pubDate: SimpleDateFormat,
        @Element
        val lastBuildDate: SimpleDateFormat,
        @Element
        val category: String,
        @ElementUnion(
                Element(name = "url", type = String::class)
        )
        val image: FeedImageEntity,
        @ElementListUnion(
                ElementList(entry = "title", type = String::class, inline = true),
                ElementList(entry = "link", type = String::class, inline = true),
                ElementList(entry = "description", type = String::class, inline = true),
                ElementList(entry = "authorEmail", type = String::class, inline = true),
                ElementList(entry = "category", type = String::class, inline = true),
                ElementList(entry = "commentsLink", type = String::class, inline = true),
                ElementList(entry = "pubDate", type = SimpleDateFormat::class, inline = true)
        )
        val items: ArrayList<FeedItemEntity>
)