package com.github.rssreader.features.feed.data.entity.mapper

import com.github.rssreader.features.feed.data.entity.FeedItemEntity
import com.github.rssreader.features.feed.domain.models.FeedItem
import io.reactivex.Observable


class FeedItemMapperImpl : FeedItemMapper {
    override fun transformToEntity(model: FeedItem): Observable<FeedItemEntity> {
        val feedItemEntity = FeedItemEntity(
                model.title,
                model.link,
                model.description,
                model.authorEmail,
                model.categories,
                model.pubDate
        )

        return Observable.just(feedItemEntity)
    }

    override fun transformToDomainModel(entity: FeedItemEntity): Observable<FeedItem> {
        val feedItem = FeedItem(
                entity.title,
                entity.link,
                entity.description,
                entity.authorEmail,
                entity.categories,
                entity.pubDate
        )

        return Observable.just(feedItem)
    }

    override fun transformToEntityList(feedItems: ArrayList<FeedItem>): Observable<ArrayList<FeedItemEntity>> {
        return Observable
                .just(feedItems)
                .flatMapIterable { feedItems1 -> feedItems1 }
                .flatMap { feedItem -> transformToEntity(feedItem) }
                .toList()
                .flatMapObservable { mutableFeedItemEntityList -> Observable.just(ArrayList(mutableFeedItemEntityList)) }
    }

    override fun transformToDomainModelList(feedItemEntities: ArrayList<FeedItemEntity>): Observable<ArrayList<FeedItem>> {
        return Observable
                .just(feedItemEntities)
                .flatMapIterable { feedItemEntities1 -> feedItemEntities1 }
                .flatMap { feedItemEntity -> transformToDomainModel(feedItemEntity) }
                .toList()
                .flatMapObservable { mutableFeedItemList -> Observable.just(ArrayList(mutableFeedItemList)) }
    }
}