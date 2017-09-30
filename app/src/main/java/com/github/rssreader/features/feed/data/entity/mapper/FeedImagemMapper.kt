package com.github.rssreader.features.feed.data.entity.mapper

import com.github.rssreader.base.data.entity.mapper.Mapper
import com.github.rssreader.features.feed.data.entity.FeedEntity
import com.github.rssreader.features.feed.domain.models.Feed
import io.reactivex.Observable


class FeedImagemMapper : Mapper<Feed, FeedEntity> {
    override fun transformToEntity(model: Feed): Observable<FeedEntity> {
        TODO("not implemented")
    }

    override fun transformToDomainModel(entity: FeedEntity): Observable<Feed> {
        TODO("not implemented")
    }
}