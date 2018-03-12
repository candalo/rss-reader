package com.github.rssreader.features.feedsubscription.data.di

import android.content.Context
import com.github.rssreader.base.data.di.BaseModule
import com.github.rssreader.base.data.di.scope.ActivityScope
import com.github.rssreader.base.data.entity.mapper.Mapper
import com.github.rssreader.base.domain.CompletableUseCase
import com.github.rssreader.base.domain.Thread
import com.github.rssreader.base.presentation.presenter.Presenter
import com.github.rssreader.base.presentation.view.ErrorMessageHandler
import com.github.rssreader.features.feedsubscription.data.entity.FeedSubscriptionEntity
import com.github.rssreader.features.feedsubscription.data.entity.mapper.FeedSubscriptionMapper
import com.github.rssreader.features.feedsubscription.data.repository.FeedSubscriptionRepositoryImpl
import com.github.rssreader.features.feedsubscription.data.repository.datasource.database.LocalFeedSubscriptionDataSource
import com.github.rssreader.features.feedsubscription.data.repository.datasource.network.CloudFeedSubscriptionDataSource
import com.github.rssreader.features.feedsubscription.data.repository.datasource.network.FeedSubscriptionRestApi
import com.github.rssreader.features.feedsubscription.domain.models.FeedSubscription
import com.github.rssreader.features.feedsubscription.domain.repository.FeedSubscriptionRepository
import com.github.rssreader.features.feedsubscription.domain.usecases.Subscribe
import com.github.rssreader.features.feedsubscription.presentation.presenter.FeedSubscriptionPresenter
import com.github.rssreader.features.feedsubscription.presentation.view.FeedSubscriptionErrorMessageHandler
import com.github.rssreader.features.feedsubscription.presentation.view.FeedSubscriptionView
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Named

@Module
class FeedSubscriptionModule(private val context: Context) {

    @Provides
    @ActivityScope
    fun provideRestApi(retrofit: Retrofit): FeedSubscriptionRestApi =
            retrofit.create(FeedSubscriptionRestApi::class.java)

    @Provides
    @ActivityScope
    fun provideCloudDataSource(restApi: FeedSubscriptionRestApi): CloudFeedSubscriptionDataSource =
            CloudFeedSubscriptionDataSource(restApi)

    @Provides
    @ActivityScope
    fun provideLocalDataSource(): LocalFeedSubscriptionDataSource =
            LocalFeedSubscriptionDataSource()

    @Provides
    @ActivityScope
    fun provideFeedSubscriptionMapper(): Mapper<FeedSubscription, FeedSubscriptionEntity> =
            FeedSubscriptionMapper()

    @Provides
    @ActivityScope
    fun provideRepository(cloudDataSource: CloudFeedSubscriptionDataSource,
                          localDataSource: LocalFeedSubscriptionDataSource,
                          mapper: Mapper<FeedSubscription, FeedSubscriptionEntity>): FeedSubscriptionRepository =
            FeedSubscriptionRepositoryImpl(cloudDataSource, localDataSource, mapper)

    @Provides
    @ActivityScope
    fun provideUseCase(@Named(BaseModule.NEW_THREAD_INJECTION_ID) subscriberThread: Thread,
                       @Named(BaseModule.MAIN_THREAD_INJECTION_ID) observerThread: Thread,
                       repository: FeedSubscriptionRepository): CompletableUseCase<FeedSubscription> =
            Subscribe(subscriberThread, observerThread, repository)

    @Provides
    @ActivityScope
    fun provideErrorMessageHandler(): ErrorMessageHandler =
            FeedSubscriptionErrorMessageHandler(context)

    @Provides
    @ActivityScope
    fun providePresenter(feedSubscriptionUseCase: CompletableUseCase<FeedSubscription>,
                         errorMessageHandler: ErrorMessageHandler): Presenter<FeedSubscriptionView> =
            FeedSubscriptionPresenter(feedSubscriptionUseCase, errorMessageHandler)
}
