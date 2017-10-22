package com.github.rssreader.features.feed.data.entity.mapper

import com.github.rssreader.base.data.entity.mapper.Mapper
import com.github.rssreader.base.domain.Thread
import com.github.rssreader.features.feed.data.entity.FeedChannelEntity
import com.github.rssreader.features.feed.data.entity.FeedImageEntity
import com.github.rssreader.features.feed.data.entity.FeedItemEntity
import com.github.rssreader.features.feed.domain.models.FeedChannel
import com.github.rssreader.features.feed.domain.models.FeedImage
import com.github.rssreader.features.feed.domain.models.FeedItem
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.util.concurrent.TimeUnit


class FeedMapperTest {

    private val firstTestObserver = TestObserver<FeedChannelEntity>()
    private val secondTestObserver = TestObserver<FeedChannel>()
    private val testScheduler = TestScheduler()

    @Mock private lateinit var thread: Thread
    @Mock private lateinit var feedItemMapper: FeedItemMapper
    @Mock private lateinit var feedImageMapper: Mapper<FeedImage, FeedImageEntity>

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)

        given { thread.get() }.willReturn(testScheduler)
    }

    @Test
    fun transformToEntity_completeDomainModel_willReturnEntityObservable() {
        val feedItem = FeedItem(
                "title",
                "link",
                "description",
                "authorEmail",
                listOf("category"),
                "pubDate"
        )
        val feedItemList = arrayListOf(feedItem)
        val feedItemEntity = FeedItemEntity(
                "title",
                "link",
                "description",
                "authorEmail",
                listOf("category"),
                "pubDate"
        )
        val feedItemEntityList = arrayListOf(feedItemEntity)

        given { feedItemMapper.transformToEntityList(feedItemList) }.willReturn(Observable.just(feedItemEntityList))

        val feedImage = FeedImage("url")
        val feedImageEntity = FeedImageEntity("url")

        given { feedImageMapper.transformToEntity(feedImage) }.willReturn(Observable.just(feedImageEntity))

        val feed = FeedChannel(
                "title",
                "link",
                "description",
                "language",
                "managingEditorEmail",
                "pubDate",
                "lastBuildDate",
                "category",
                feedImage,
                feedItemList
        )

        val mapper = FeedChannelMapper(feedItemMapper, feedImageMapper)
        val punchEntityObservable = mapper.transformToEntity(feed)

        punchEntityObservable
                .subscribeOn(thread.get())
                .observeOn(thread.get())
                .subscribeWith(firstTestObserver)

        val feedEntity = FeedChannelEntity(
                "title",
                listOf("link"),
                "description",
                "language",
                "managingEditorEmail",
                "pubDate",
                "lastBuildDate",
                "category",
                feedImageEntity,
                feedItemEntityList
        )

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)

        firstTestObserver.assertValueCount(1)
        firstTestObserver.assertValue(feedEntity)
        firstTestObserver.assertComplete()
        firstTestObserver.assertNoErrors()

        verify(feedItemMapper, times(1)).transformToEntityList(feedItemList)
        verify(feedImageMapper, times(1)).transformToEntity(feedImage)
    }

    @Test
    fun transformToEntity_onlyRequiredDataDomainModel_willReturnEntityObservable() {
        val feedItemList = ArrayList<FeedItem>()
        val feedItemEntityList = ArrayList<FeedItemEntity>()
        val feedImage = FeedImage()
        val feedImageEntity = FeedImageEntity()

        given { feedItemMapper.transformToEntityList(feedItemList) }.willReturn(Observable.just(feedItemEntityList))
        given { feedImageMapper.transformToEntity(feedImage) }.willReturn(Observable.just(feedImageEntity))

        val feed = FeedChannel(
                "title",
                "link",
                "description",
                "",
                "",
                "",
                "",
                "",
                feedImage,
                feedItemList
        )

        val mapper = FeedChannelMapper(feedItemMapper, feedImageMapper)
        val punchEntityObservable = mapper.transformToEntity(feed)

        punchEntityObservable
                .subscribeOn(thread.get())
                .observeOn(thread.get())
                .subscribeWith(firstTestObserver)

        val feedEntity = FeedChannelEntity(
                "title",
                listOf("link"),
                "description",
                "",
                "",
                "",
                "",
                "",
                feedImageEntity,
                feedItemEntityList
        )

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)

        firstTestObserver.assertValueCount(1)
        firstTestObserver.assertValue(feedEntity)
        firstTestObserver.assertComplete()
        firstTestObserver.assertNoErrors()

        verify(feedItemMapper, times(1)).transformToEntityList(feedItemList)
        verify(feedImageMapper, times(1)).transformToEntity(feedImage)
    }

    @Test
    fun transformToDomainModel_completeEntity_willReturnDomainModelObservable() {
        val feedItem = FeedItem(
                "title",
                "link",
                "description",
                "authorEmail",
                listOf("category"),
                "pubDate"
        )
        val feedItemList = arrayListOf(feedItem)
        val feedItemEntity = FeedItemEntity(
                "title",
                "link",
                "description",
                "authorEmail",
                listOf("category"),
                "pubDate"
        )
        val feedItemEntityList = arrayListOf(feedItemEntity)

        given { feedItemMapper.transformToDomainModelList(feedItemEntityList) }.willReturn(Observable.just(feedItemList))

        val feedImage = FeedImage("url")
        val feedImageEntity = FeedImageEntity("url")

        given { feedImageMapper.transformToDomainModel(feedImageEntity) }.willReturn(Observable.just(feedImage))

        val feedEntity = FeedChannelEntity(
                "title",
                listOf("link"),
                "description",
                "language",
                "managingEditorEmail",
                "pubDate",
                "lastBuildDate",
                "category",
                feedImageEntity,
                feedItemEntityList
        )

        val mapper = FeedChannelMapper(feedItemMapper, feedImageMapper)
        val punchEntityObservable = mapper.transformToDomainModel(feedEntity)

        punchEntityObservable
                .subscribeOn(thread.get())
                .observeOn(thread.get())
                .subscribeWith(secondTestObserver)

        val feed = FeedChannel(
                "title",
                "link",
                "description",
                "language",
                "managingEditorEmail",
                "pubDate",
                "lastBuildDate",
                "category",
                feedImage,
                feedItemList
        )

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)

        secondTestObserver.assertValueCount(1)
        secondTestObserver.assertValue(feed)
        secondTestObserver.assertComplete()
        secondTestObserver.assertNoErrors()

        verify(feedItemMapper, times(1)).transformToDomainModelList(feedItemEntityList)
        verify(feedImageMapper, times(1)).transformToDomainModel(feedImageEntity)
    }

    @Test
    fun transformToDomainModel_onlyRequiredEntity_willReturnDomainModelObservable() {
        val feedItemList = ArrayList<FeedItem>()
        val feedItemEntityList = ArrayList<FeedItemEntity>()

        given { feedItemMapper.transformToDomainModelList(feedItemEntityList) }.willReturn(Observable.just(feedItemList))

        val feedImage = FeedImage()
        val feedImageEntity = FeedImageEntity()

        given { feedImageMapper.transformToDomainModel(feedImageEntity) }.willReturn(Observable.just(feedImage))

        val feedEntity = FeedChannelEntity(
                "title",
                listOf("link"),
                "description",
                "",
                "",
                "",
                "",
                "",
                feedImageEntity,
                feedItemEntityList
        )

        val mapper = FeedChannelMapper(feedItemMapper, feedImageMapper)
        val punchEntityObservable = mapper.transformToDomainModel(feedEntity)

        punchEntityObservable
                .subscribeOn(thread.get())
                .observeOn(thread.get())
                .subscribeWith(secondTestObserver)

        val feed = FeedChannel(
                "title",
                "link",
                "description",
                "",
                "",
                "",
                "",
                "",
                feedImage,
                feedItemList)

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)

        secondTestObserver.assertValueCount(1)
        secondTestObserver.assertValue(feed)
        secondTestObserver.assertComplete()
        secondTestObserver.assertNoErrors()

        verify(feedItemMapper, times(1)).transformToDomainModelList(feedItemEntityList)
        verify(feedImageMapper, times(1)).transformToDomainModel(feedImageEntity)
    }
}