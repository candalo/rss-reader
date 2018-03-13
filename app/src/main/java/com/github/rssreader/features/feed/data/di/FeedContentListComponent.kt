package com.github.rssreader.features.feed.data.di

import com.github.rssreader.base.data.di.BaseComponent
import com.github.rssreader.base.data.di.scope.FragmentScope
import com.github.rssreader.features.feed.presentation.view.FeedContentListFragment
import com.github.rssreader.network.NetworkComponent
import dagger.Component

@FragmentScope
@Component(modules = [(FeedContentListModule::class)], dependencies = [(NetworkComponent::class), (BaseComponent::class)])
interface FeedContentListComponent {

    fun inject(feedContentListFragment: FeedContentListFragment)

}