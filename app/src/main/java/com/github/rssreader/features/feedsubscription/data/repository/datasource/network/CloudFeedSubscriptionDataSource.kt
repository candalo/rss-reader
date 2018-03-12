package com.github.rssreader.features.feedsubscription.data.repository.datasource.network

import com.github.rssreader.features.feedsubscription.domain.InvalidFeedSubscriptionException
import com.github.rssreader.features.feedsubscription.domain.models.FeedSubscription
import io.reactivex.Completable
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject


class CloudFeedSubscriptionDataSource @Inject constructor(private val restApi: FeedSubscriptionRestApi) {

    fun create(feedSubscription: FeedSubscription): Completable =
            restApi.subscribe(feedUrl = feedSubscription.url).flatMapCompletable { response -> handleResponse(response)}

    private fun handleResponse(response: Response<ResponseBody>): Completable {
        if (response.isSuccessful) {
            return Completable.complete()
        }
        return Completable.error(InvalidFeedSubscriptionException())
    }
}
