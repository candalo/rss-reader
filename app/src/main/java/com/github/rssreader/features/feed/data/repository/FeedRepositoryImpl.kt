package com.github.rssreader.features.feed.data.repository

import com.github.rssreader.base.data.entity.mapper.Mapper
import com.github.rssreader.features.feed.data.entity.FeedEntity
import com.github.rssreader.features.feed.data.repository.datasource.CloudFeedDataSource
import com.github.rssreader.features.feed.domain.models.Feed
import com.github.rssreader.features.feed.domain.repository.FeedRepository
import io.reactivex.Observable
import javax.inject.Inject


class FeedRepositoryImpl @Inject constructor(
        private val cloudFeedDataSource: CloudFeedDataSource,
        private val feedMapper: Mapper<Feed, FeedEntity>
) : FeedRepository {

    override fun find(feedUrl: String): Observable<Feed> =
            cloudFeedDataSource
                    .find(feedUrl)
                    .flatMap { feedEntity -> feedMapper.transformToDomainModel(feedEntity) }

}