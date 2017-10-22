package com.github.rssreader.features.feedsubscription.data.repository.datasource.network

import com.github.rssreader.base.domain.Thread
import com.github.rssreader.features.feed.data.entity.FeedChannelEntity
import com.github.rssreader.features.feedsubscription.domain.models.FeedSubscription
import com.nhaarman.mockito_kotlin.given
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.TestScheduler
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import org.apache.commons.io.IOUtils
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.net.SocketTimeoutException
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit


@RunWith(Parameterized::class)
class CloudFeedSubscriptionDataStoreTest(private val input: String) {

    private val testObserver = TestObserver<FeedChannelEntity>()
    private val testScheduler = TestScheduler()
    private val mockWebServer = MockWebServer()

    @Mock private lateinit var thread: Thread

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Iterable<String> =
                listOf(
                        "feedsubscription_successfully_1.xml",
                        "feedsubscription_successfully_2.xml",
                        "feedsubscription_successfully_3.xml"
                )
    }

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)

        given { thread.get() }.willReturn(testScheduler)
    }

    @Test
    fun `create_withExistingFeedSubscription_willReturnFeedEntityObservable`() {
        mockWebServer.start()

        val baseUrl = mockWebServer.url("").toString()

        val retrofit = Retrofit.Builder()
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build()

        val inputStream =
                javaClass.classLoader.getResourceAsStream(input)
        val response = IOUtils.toString(inputStream, Charset.defaultCharset())

        mockWebServer.enqueue(MockResponse().setBody(response))

        val feedSubscriptionRestApi = retrofit.create(FeedSubscriptionRestApi::class.java)

        val cloudFeedSubscriptionDataStore = CloudFeedSubscriptionDataStore(feedSubscriptionRestApi)
        val feedChannelEntityObservable = cloudFeedSubscriptionDataStore.create(FeedSubscription(baseUrl))

        feedChannelEntityObservable
                .subscribeOn(thread.get())
                .observeOn(thread.get())
                .subscribeWith(testObserver)

        testScheduler.advanceTimeTo(1, TimeUnit.SECONDS)

        testObserver.assertComplete()
        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)

        mockWebServer.shutdown()
    }

    @Test
    fun `create_withTimeout_willReturnError`() {
        mockWebServer.start()

        val baseUrl = mockWebServer.url("").toString()

        val retrofit = Retrofit.Builder()
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build()

        val inputStream =
                javaClass.classLoader.getResourceAsStream(input)
        val responseXml = IOUtils.toString(inputStream, Charset.defaultCharset())

        val response = MockResponse()
                .setBody(responseXml)
                .setSocketPolicy(SocketPolicy.NO_RESPONSE)

        mockWebServer.enqueue(response)

        val feedSubscriptionRestApi = retrofit.create(FeedSubscriptionRestApi::class.java)

        val cloudFeedSubscriptionDataStore = CloudFeedSubscriptionDataStore(feedSubscriptionRestApi)
        val feedChannelEntityObservable = cloudFeedSubscriptionDataStore.create(FeedSubscription(baseUrl))

        feedChannelEntityObservable
                .subscribeOn(thread.get())
                .observeOn(thread.get())
                .subscribeWith(testObserver)

        testScheduler.advanceTimeTo(1, TimeUnit.SECONDS)

        testObserver.assertNotComplete()
        testObserver.assertError(SocketTimeoutException::class.java)

        mockWebServer.shutdown()
    }
}