package com.github.rssreader.features.feedsubscription.data.entity.mapper

import com.github.rssreader.base.data.entity.mapper.Mapper
import com.github.rssreader.features.feedsubscription.data.entity.FeedSubscriptionEntity
import com.github.rssreader.features.feedsubscription.domain.models.FeedSubscription
import io.reactivex.Observable


class FeedSubscriptionMapper : Mapper<FeedSubscription, FeedSubscriptionEntity> {

    override fun transformToEntity(model: FeedSubscription): Observable<FeedSubscriptionEntity>
            = Observable.just(FeedSubscriptionEntity(model.url))

    override fun transformToDomainModel(entity: FeedSubscriptionEntity): Observable<FeedSubscription>
            = Observable.just(FeedSubscription(entity.url))

}