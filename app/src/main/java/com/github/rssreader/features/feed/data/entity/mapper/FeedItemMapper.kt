package com.github.rssreader.features.feed.data.entity.mapper

import com.github.rssreader.base.data.entity.mapper.Mapper
import com.github.rssreader.features.feed.data.entity.FeedItemEntity
import com.github.rssreader.features.feed.domain.models.FeedItem
import io.reactivex.Observable


interface FeedItemMapper : Mapper<FeedItem, FeedItemEntity> {

    fun transformToEntityList(feedItems: ArrayList<FeedItem>) : Observable<ArrayList<FeedItemEntity>>

    fun transformToDomainModelList(feedItemEntities: ArrayList<FeedItemEntity>): Observable<ArrayList<FeedItem>>
}