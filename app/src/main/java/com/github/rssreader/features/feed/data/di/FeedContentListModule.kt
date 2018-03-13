package com.github.rssreader.features.feed.data.di

import com.github.rssreader.base.data.di.BaseModule
import com.github.rssreader.base.data.di.scope.FragmentScope
import com.github.rssreader.base.data.entity.mapper.Mapper
import com.github.rssreader.base.domain.Thread
import com.github.rssreader.base.domain.UseCase
import com.github.rssreader.base.presentation.presenter.Presenter
import com.github.rssreader.features.feed.data.entity.FeedChannelEntity
import com.github.rssreader.features.feed.data.entity.FeedEntity
import com.github.rssreader.features.feed.data.entity.FeedImageEntity
import com.github.rssreader.features.feed.data.entity.mapper.*
import com.github.rssreader.features.feed.data.repository.FeedRepositoryImpl
import com.github.rssreader.features.feed.data.repository.datasource.CloudFeedDataSource
import com.github.rssreader.features.feed.data.repository.datasource.FeedRestApi
import com.github.rssreader.features.feed.domain.models.Feed
import com.github.rssreader.features.feed.domain.models.FeedChannel
import com.github.rssreader.features.feed.domain.models.FeedImage
import com.github.rssreader.features.feed.domain.repository.FeedRepository
import com.github.rssreader.features.feed.domain.usecases.GetFeed
import com.github.rssreader.features.feed.presentation.presenter.FeedContentListPresenter
import com.github.rssreader.features.feed.presentation.view.FeedContentListView
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Named

@Module
class FeedContentListModule {

    @Provides
    @FragmentScope
    fun provideRestApi(retrofit: Retrofit): FeedRestApi =
            retrofit.create(FeedRestApi::class.java)

    @Provides
    @FragmentScope
    fun provideCloudDataSource(restApi: FeedRestApi): CloudFeedDataSource =
            CloudFeedDataSource(restApi)

    @Provides
    @FragmentScope
    fun provideFeedItemMapper(): FeedItemMapper =
            FeedItemMapperImpl()

    @Provides
    @FragmentScope
    fun provideFeedImageMapper(): Mapper<FeedImage, FeedImageEntity> =
            FeedImageMapper()

    @Provides
    @FragmentScope
    fun provideFeedChannelMapper(feedItemMapper: FeedItemMapper,
                                 feedImageMapper: Mapper<FeedImage, FeedImageEntity>): Mapper<FeedChannel, FeedChannelEntity> =
            FeedChannelMapper(feedItemMapper, feedImageMapper)

    @Provides
    @FragmentScope
    fun provideFeedMapper(feedChannelMapper: Mapper<FeedChannel, FeedChannelEntity>): Mapper<Feed, FeedEntity> =
            FeedMapper(feedChannelMapper)

    @Provides
    @FragmentScope
    fun provideRepository(cloudDataSource: CloudFeedDataSource,
                          feedMapper: Mapper<Feed, FeedEntity>): FeedRepository =
            FeedRepositoryImpl(cloudDataSource, feedMapper)

    @Provides
    @FragmentScope
    fun provideUseCase(@Named(BaseModule.NEW_THREAD_INJECTION_ID) subscriberThread: Thread,
                       @Named(BaseModule.MAIN_THREAD_INJECTION_ID) observerThread: Thread,
                       repository: FeedRepository): UseCase<Feed, String> =
            GetFeed(subscriberThread, observerThread, repository)

    @Provides
    @FragmentScope
    fun providePresenter(getFeedUseCase: UseCase<Feed, String>): Presenter<FeedContentListView> =
            FeedContentListPresenter(getFeedUseCase)

}