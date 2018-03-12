package com.github.rssreader.features.feedsubscription.data.entity

import com.github.rssreader.storage.AppDatabase
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table

@Table(database = AppDatabase::class)
data class FeedSubscriptionEntity(@PrimaryKey var url: String = "")