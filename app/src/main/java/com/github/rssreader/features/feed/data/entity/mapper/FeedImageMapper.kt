package com.github.rssreader.features.feed.data.entity.mapper

import com.github.rssreader.base.data.entity.mapper.Mapper
import com.github.rssreader.features.feed.data.entity.FeedImageEntity
import com.github.rssreader.features.feed.domain.models.FeedImage
import io.reactivex.Observable


class FeedImageMapper : Mapper<FeedImage, FeedImageEntity> {
    override fun transformToEntity(model: FeedImage): Observable<FeedImageEntity> =
            Observable.just(FeedImageEntity(model.url))

    override fun transformToDomainModel(entity: FeedImageEntity): Observable<FeedImage> =
            Observable.just(FeedImage(entity.url))
}