package com.github.rssreader.features.feedsubscription.data.repository.datasource.database

import com.github.rssreader.features.feedsubscription.data.entity.FeedSubscriptionEntity
import com.github.rssreader.features.feedsubscription.data.entity.FeedSubscriptionEntity_Table
import com.raizlabs.android.dbflow.sql.language.SQLite
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import junit.framework.Assert.assertEquals


@RunWith(RobolectricTestRunner::class)
class LocalFeedSubscriptionDataSourceTest {

    @Test
    fun `Feed subscription saved successfully`() {
        val localFeedSubscriptionDataSource = LocalFeedSubscriptionDataSource()
        localFeedSubscriptionDataSource.save(FeedSubscriptionEntity("myUrl"))

        val feedSubscriptionEntitySaved = SQLite.select()
                .from(FeedSubscriptionEntity::class.java)
                .where(FeedSubscriptionEntity_Table.url.eq("myUrl"))
                .querySingle()

        assertEquals("myUrl", feedSubscriptionEntitySaved?.url)
    }
}