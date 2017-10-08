package com.github.rssreader.features.feedsubscription.data.repository

import com.github.rssreader.base.data.entity.mapper.Mapper
import com.github.rssreader.features.feed.data.entity.FeedEntity
import com.github.rssreader.features.feed.domain.models.Feed
import com.github.rssreader.features.feedsubscription.data.repository.datasource.FeedSubscriptionDataStoreFactory
import com.github.rssreader.features.feedsubscription.domain.models.FeedSubscription
import com.github.rssreader.features.feedsubscription.domain.repository.FeedSubscriptionRepository
import io.reactivex.Observable


class FeedSubscriptionRepositoryImpl(private val dataStoreFactory: FeedSubscriptionDataStoreFactory,
                                     private val dataMapper: Mapper<Feed, FeedEntity>
) : FeedSubscriptionRepository {

    override fun create(feedSubscription: FeedSubscription): Observable<Feed> {
        return dataStoreFactory
                .create()
                .flatMap {
                    dataStore ->
                    dataStore
                            .create(feedSubscription)
                            .flatMap { feedEntity -> dataMapper.transformToDomainModel(feedEntity) }
                }
    }
}