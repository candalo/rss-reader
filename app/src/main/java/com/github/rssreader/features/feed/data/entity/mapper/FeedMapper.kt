package com.github.rssreader.features.feed.data.entity.mapper

import com.github.rssreader.base.data.entity.mapper.Mapper
import com.github.rssreader.features.feed.data.entity.FeedEntity
import com.github.rssreader.features.feed.data.entity.FeedImageEntity
import com.github.rssreader.features.feed.domain.models.Feed
import com.github.rssreader.features.feed.domain.models.FeedImage
import io.reactivex.Observable
import io.reactivex.rxkotlin.zipWith


class FeedMapper(private val feedItemMapper: FeedItemMapper,
                 private val feedImageMapper: Mapper<FeedImage, FeedImageEntity>
) : Mapper<Feed, FeedEntity> {
    override fun transformToEntity(model: Feed): Observable<FeedEntity> {

        val feedItemEntitiesObservable = feedItemMapper.transformToEntityList(model.items)
        val feedImageEntityObservable = feedImageMapper.transformToEntity(model.image)

        return feedItemEntitiesObservable
                .zipWith(feedImageEntityObservable)
                .map { (feedItemEntities, feedImageEntity) ->
                    FeedEntity(
                        model.title,
                        model.link,
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

    override fun transformToDomainModel(entity: FeedEntity): Observable<Feed> {
        val feedItemsObservable = feedItemMapper.transformToDomainModelList(entity.items)
        val feedImageObservable = feedImageMapper.transformToDomainModel(entity.image)

        return feedItemsObservable
                .zipWith(feedImageObservable)
                .map { (feedItems, feedImages) ->
                    Feed(
                        entity.title,
                        entity.link,
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