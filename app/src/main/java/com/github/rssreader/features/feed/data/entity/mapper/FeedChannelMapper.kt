package com.github.rssreader.features.feed.data.entity.mapper

import com.github.rssreader.base.data.entity.mapper.Mapper
import com.github.rssreader.features.feed.data.entity.FeedChannelEntity
import com.github.rssreader.features.feed.data.entity.FeedImageEntity
import com.github.rssreader.features.feed.domain.models.FeedChannel
import com.github.rssreader.features.feed.domain.models.FeedImage
import io.reactivex.Observable
import io.reactivex.rxkotlin.zipWith


class FeedChannelMapper(private val feedItemMapper: FeedItemMapper,
                        private val feedImageMapper: Mapper<FeedImage, FeedImageEntity>
) : Mapper<FeedChannel, FeedChannelEntity> {
    override fun transformToEntity(model: FeedChannel): Observable<FeedChannelEntity> {

        val feedItemEntitiesObservable = feedItemMapper.transformToEntityList(model.items)
        val feedImageEntityObservable = feedImageMapper.transformToEntity(model.image)

        return feedItemEntitiesObservable
                .zipWith(feedImageEntityObservable)
                .map { (feedItemEntities, feedImageEntity) ->
                    FeedChannelEntity(
                        model.title,
                        listOf(model.link),
                        model.description,
                        model.language,
                        model.managingEditorEmail,
                        model.pubDate,
                        model.lastBuildDate,
                        model.category,
                        feedImageEntity,
                        feedItemEntities
                )}
    }

    override fun transformToDomainModel(entity: FeedChannelEntity): Observable<FeedChannel> {
        val feedItemsObservable = feedItemMapper.transformToDomainModelList(entity.items)
        val feedImageObservable = feedImageMapper.transformToDomainModel(entity.image)

        return feedItemsObservable
                .zipWith(feedImageObservable)
                .map { (feedItems, feedImages) ->
                    FeedChannel(
                        entity.title,
                        entity.links.first(),
                        entity.description,
                        entity.language,
                        entity.managingEditorEmail,
                        entity.pubDate,
                        entity.lastBuildDate,
                        entity.category,
                        feedImages,
                        feedItems
                )}
    }
}