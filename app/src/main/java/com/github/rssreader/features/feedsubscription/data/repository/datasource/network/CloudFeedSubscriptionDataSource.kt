package com.github.rssreader.features.feedsubscription.data.repository.datasource.network

import com.github.rssreader.features.feedsubscription.domain.InvalidFeedSubscriptionException
import com.github.rssreader.features.feedsubscription.domain.models.FeedSubscription
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response


class CloudFeedSubscriptionDataSource(private val restApi: FeedSubscriptionRestApi) {

    fun create(feedSubscription: FeedSubscription): Observable<Void> =
            restApi.subscribe(feedUrl = feedSubscription.url).flatMap { response -> handleResponse(response)}

    private fun handleResponse(response: Response<ResponseBody>): Observable<Void> {
        if (response.isSuccessful) {
            return Observable.empty<Void>()
        }
        return Observable.error(InvalidFeedSubscriptionException())
    }
}
