package com.github.rssreader.features.feedsubscription.data.di

import com.github.rssreader.base.data.di.BaseComponent
import com.github.rssreader.base.data.di.scope.ActivityScope
import com.github.rssreader.base.data.di.scope.FragmentScope
import com.github.rssreader.features.feedsubscription.presentation.view.FeedSubscriptionFragment
import com.github.rssreader.network.NetworkComponent
import dagger.Component

@FragmentScope
@Component(modules = [(FeedSubscriptionModule::class)], dependencies = [(NetworkComponent::class), (BaseComponent::class)])
interface FeedSubscriptionComponent {

    fun inject(feedSubscriptionFragment: FeedSubscriptionFragment)

}
