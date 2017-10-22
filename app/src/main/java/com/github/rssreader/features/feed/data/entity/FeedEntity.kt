package com.github.rssreader.features.feed.data.entity

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root


@Root(name = "rss", strict = false)
data class FeedEntity(
        @field:Element(name = "channel") var channel: FeedChannelEntity = FeedChannelEntity()
)