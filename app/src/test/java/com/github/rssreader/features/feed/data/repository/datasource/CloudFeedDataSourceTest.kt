package com.github.rssreader.features.feed.data.repository.datasource

import com.github.rssreader.base.domain.Thread
import com.github.rssreader.features.feed.data.entity.FeedEntity
import com.nhaarman.mockito_kotlin.given
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.TestScheduler
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.apache.commons.io.IOUtils
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit


class CloudFeedDataSourceTest {

    companion object {
        const val OK_RESPONSE = "feed_successfully.xml"
        const val ERROR_RESPONSE = "feed_error.xml"
    }

    private val testObserver = TestObserver<FeedEntity>()
    private val testScheduler = TestScheduler()
    private val mockWebServer = MockWebServer()

    @Mock
    private lateinit var thread: Thread

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
        given { thread.get() }.willReturn(testScheduler)
        mockWebServer.start()
    }

    @After
    fun after() {
        mockWebServer.shutdown()
    }

    @Test
    fun `Feed Load Successfully`() {
        setupResponseForMockWebServer(mockWebServer, OK_RESPONSE, 200)
        setupFeedEntityObservable(mockWebServer)

        testScheduler.advanceTimeTo(1, TimeUnit.SECONDS)

        testObserver.assertComplete()
        testObserver.assertNoErrors()
    }

    @Test
    fun `Feed Load Error`() {
        setupResponseForMockWebServer(mockWebServer, ERROR_RESPONSE, 500)
        setupFeedEntityObservable(mockWebServer)

        testScheduler.advanceTimeTo(1, TimeUnit.SECONDS)

        testObserver.assertNotComplete()
        testObserver.assertError(Exception::class.java)
    }

    private fun setupResponseForMockWebServer(mockWebServer: MockWebServer,
                                              resourceName: String,
                                              responseCode: Int) {
        val inputStream = javaClass.classLoader.getResourceAsStream(resourceName)
        val responseXml = IOUtils.toString(inputStream, Charset.defaultCharset())
        mockWebServer.enqueue(MockResponse().setBody(responseXml).setResponseCode(responseCode))
    }

    private fun setupFeedEntityObservable(mockWebServer: MockWebServer) {
        val restApi = setupRetrofit(mockWebServer)

        val feedDataSource = CloudFeedDataSource(restApi)
        val feedEntityObservable = feedDataSource.find("")

        feedEntityObservable
                .subscribeOn(thread.get())
                .observeOn(thread.get())
                .subscribeWith(testObserver)
    }

    private fun setupRetrofit(mockWebServer: MockWebServer): FeedRestApi {
        val baseUrl = mockWebServer.url("").toString()

        val retrofit = Retrofit.Builder()
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build()

        return retrofit.create(FeedRestApi::class.java)
    }
}