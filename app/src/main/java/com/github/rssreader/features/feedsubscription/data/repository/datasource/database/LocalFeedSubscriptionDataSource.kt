package com.github.rssreader.features.feedsubscription.data.repository.datasource.database

import com.github.rssreader.features.feedsubscription.data.entity.FeedSubscriptionEntity
import com.raizlabs.android.dbflow.kotlinextensions.save
import io.reactivex.Completable


class LocalFeedSubscriptionDataSource {

    fun save(feedSubscriptionEntity: FeedSubscriptionEntity): Completable {
        feedSubscriptionEntity.save()
        return Completable.complete()
    }

}