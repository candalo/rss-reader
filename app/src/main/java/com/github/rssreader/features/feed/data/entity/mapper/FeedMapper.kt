package com.github.rssreader.features.feed.data.entity.mapper

import com.github.rssreader.base.data.entity.mapper.Mapper
import com.github.rssreader.features.feed.data.entity.FeedChannelEntity
import com.github.rssreader.features.feed.data.entity.FeedEntity
import com.github.rssreader.features.feed.domain.models.Feed
import com.github.rssreader.features.feed.domain.models.FeedChannel
import io.reactivex.Observable


class FeedMapper(private val feedChannelMapper: Mapper<FeedChannel, FeedChannelEntity>) : Mapper<Feed, FeedEntity> {

    override fun transformToEntity(model: Feed): Observable<FeedEntity> =
            feedChannelMapper
                    .transformToEntity(model.channel)
                    .map { feedChannelEntity -> FeedEntity(feedChannelEntity) }

    override fun transformToDomainModel(entity: FeedEntity): Observable<Feed> =
            feedChannelMapper
                    .transformToDomainModel(entity.channel)
                    .map { feedChannel -> Feed(feedChannel) }
}