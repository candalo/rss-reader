package com.github.rssreader.features.feedsubscription.data.repository.datasource.network

import com.github.rssreader.base.domain.Thread
import com.github.rssreader.features.feedsubscription.domain.InvalidFeedSubscriptionException
import com.github.rssreader.features.feedsubscription.domain.models.FeedSubscription
import com.nhaarman.mockito_kotlin.given
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.TestScheduler
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.apache.commons.io.IOUtils
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit


class CloudFeedSubscriptionDataSourceTest {

    companion object {
        const val OK_RESPONSE = "feedsubscription_successfully.xml"
        const val ERROR_RESPONSE = "feedsubscription_error.xml"
    }

    private val testObserver = TestObserver<Void>()
    private val testScheduler = TestScheduler()
    private val mockWebServer = MockWebServer()

    @Mock private lateinit var thread: Thread

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)

        given { thread.get() }.willReturn(testScheduler)
    }

    @Test
    fun `create_withExistingFeedSubscription_willReturnSuccess`() {
        mockWebServer.start()

        setupResponseForMockWebServer(mockWebServer, OK_RESPONSE, 200)
        setupFeedChannelEntityObservable(mockWebServer)

        testScheduler.advanceTimeTo(1, TimeUnit.SECONDS)

        testObserver.assertComplete()
        testObserver.assertNoErrors()

        mockWebServer.shutdown()
    }

    @Test
    fun `create_withNonexistentFeedSubscription_willReturnError`() {
        mockWebServer.start()

        setupResponseForMockWebServer(mockWebServer, ERROR_RESPONSE, 500)
        setupFeedChannelEntityObservable(mockWebServer)

        testScheduler.advanceTimeTo(1, TimeUnit.SECONDS)

        testObserver.assertNotComplete()
        testObserver.assertError(InvalidFeedSubscriptionException::class.java)

        mockWebServer.shutdown()
    }

    private fun setupResponseForMockWebServer(mockWebServer: MockWebServer,
                                              resourceName: String,
                                              responseCode: Int) {
        val inputStream = javaClass.classLoader.getResourceAsStream(resourceName)
        val responseXml = IOUtils.toString(inputStream, Charset.defaultCharset())
        mockWebServer.enqueue(MockResponse().setBody(responseXml).setResponseCode(responseCode))
    }

    private fun setupFeedChannelEntityObservable(mockWebServer: MockWebServer) {
        val restApi = setupRetrofit(mockWebServer)

        val cloudFeedSubscriptionDataStore = CloudFeedSubscriptionDataSource(restApi)
        val feedChannelEntityObservable = cloudFeedSubscriptionDataStore.create(FeedSubscription(""))

        feedChannelEntityObservable
                .subscribeOn(thread.get())
                .observeOn(thread.get())
                .subscribeWith(testObserver)
    }

    private fun setupRetrofit(mockWebServer: MockWebServer): FeedSubscriptionRestApi {
        val baseUrl = mockWebServer.url("").toString()

        val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build()

        return retrofit.create(FeedSubscriptionRestApi::class.java)
    }
}
