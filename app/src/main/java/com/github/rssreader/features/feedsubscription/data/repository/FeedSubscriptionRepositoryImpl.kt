package com.github.rssreader.features.feedsubscription.data.repository

import com.github.rssreader.base.data.entity.mapper.Mapper
import com.github.rssreader.features.feedsubscription.data.entity.FeedSubscriptionEntity
import com.github.rssreader.features.feedsubscription.data.repository.datasource.database.LocalFeedSubscriptionDataSource
import com.github.rssreader.features.feedsubscription.data.repository.datasource.network.CloudFeedSubscriptionDataSource
import com.github.rssreader.features.feedsubscription.domain.models.FeedSubscription
import com.github.rssreader.features.feedsubscription.domain.repository.FeedSubscriptionRepository
import io.reactivex.Completable
import javax.inject.Inject


class FeedSubscriptionRepositoryImpl
@Inject constructor(private val cloudFeedSubscriptionDataSource: CloudFeedSubscriptionDataSource,
                    private val localFeedSubscriptionDataSource: LocalFeedSubscriptionDataSource,
                    private val feedSubscriptionMapper: Mapper<FeedSubscription, FeedSubscriptionEntity>
) : FeedSubscriptionRepository {

    override fun create(feedSubscription: FeedSubscription): Completable {
        val saveFeedSubscriptionCompletable = feedSubscriptionMapper
                .transformToEntity(feedSubscription)
                .flatMapCompletable { entity -> localFeedSubscriptionDataSource.save(entity) }

        return cloudFeedSubscriptionDataSource
                .create(feedSubscription)
                .andThen(saveFeedSubscriptionCompletable)
    }

}
