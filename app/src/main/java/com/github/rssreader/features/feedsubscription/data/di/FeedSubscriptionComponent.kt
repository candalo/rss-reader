package com.github.rssreader.features.feedsubscription.data.di

import com.github.rssreader.base.data.di.BaseComponent
import com.github.rssreader.base.data.di.scope.ActivityScope
import com.github.rssreader.features.feedsubscription.presentation.view.FeedSubscriptionActivity
import com.github.rssreader.network.NetworkComponent
import dagger.Component

@ActivityScope
@Component(modules = arrayOf(FeedSubscriptionModule::class), dependencies = arrayOf(NetworkComponent::class, BaseComponent::class))
interface FeedSubscriptionComponent {

    fun inject(feedSubscriptionActivity: FeedSubscriptionActivity)

}
