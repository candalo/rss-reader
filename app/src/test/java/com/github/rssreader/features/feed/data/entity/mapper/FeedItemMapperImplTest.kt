package com.github.rssreader.features.feed.data.entity.mapper

import com.github.rssreader.base.domain.Thread
import com.github.rssreader.features.feed.data.entity.FeedItemEntity
import com.github.rssreader.features.feed.domain.models.FeedItem
import com.nhaarman.mockito_kotlin.given
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.util.concurrent.TimeUnit


class FeedItemMapperImplTest {

    private val firstTestObserver = TestObserver<FeedItemEntity>()
    private val secondTestObserver = TestObserver<FeedItem>()
    private val thirdTestObserver = TestObserver<ArrayList<FeedItemEntity>>()
    private val fourthTestObserver = TestObserver<ArrayList<FeedItem>>()
    private val testScheduler = TestScheduler()

    @Mock private lateinit var thread: Thread

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

        val mapper = FeedItemMapperImpl()
        val feedItemEntityObservable = mapper.transformToEntity(feedItem)

        feedItemEntityObservable
                .subscribeOn(thread.get())
                .observeOn(thread.get())
                .subscribeWith(firstTestObserver)

        val feedItemEntity = FeedItemEntity(
                "title",
                "link",
                "description",
                "authorEmail",
                listOf("category"),
                "pubDate"
        )

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)

        firstTestObserver.assertValueCount(1)
        firstTestObserver.assertValue(feedItemEntity)
        firstTestObserver.assertComplete()
        firstTestObserver.assertNoErrors()
    }

    @Test
    fun transformToEntity_onlyRequiredDomainModel_willReturnEntityObservable() {
        val feedItem = FeedItem("title")

        val mapper = FeedItemMapperImpl()
        val feedItemEntityObservable = mapper.transformToEntity(feedItem)

        feedItemEntityObservable
                .subscribeOn(thread.get())
                .observeOn(thread.get())
                .subscribeWith(firstTestObserver)

        val feedItemEntity = FeedItemEntity("title")

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)

        firstTestObserver.assertValueCount(1)
        firstTestObserver.assertValue(feedItemEntity)
        firstTestObserver.assertComplete()
        firstTestObserver.assertNoErrors()
    }

    @Test
    fun transformToDomainModel_completeEntityModel_willReturnDomainModelObservable() {
        val feedItemEntity = FeedItemEntity(
                "title",
                "link",
                "description",
                "authorEmail",
                listOf("category"),
                "pubDate"
        )

        val mapper = FeedItemMapperImpl()
        val feedItemEntityObservable = mapper.transformToDomainModel(feedItemEntity)

        feedItemEntityObservable
                .subscribeOn(thread.get())
                .observeOn(thread.get())
                .subscribeWith(secondTestObserver)

        val feedItem = FeedItem(
                "title",
                "link",
                "description",
                "authorEmail",
                listOf("category"),
                "pubDate"
        )

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)

        secondTestObserver.assertValueCount(1)
        secondTestObserver.assertValue(feedItem)
        secondTestObserver.assertComplete()
        secondTestObserver.assertNoErrors()
    }

    @Test
    fun transformToDomainModel_onlyRequiredEntityModel_willReturnDomainModelObservable() {
        val feedItemEntity = FeedItemEntity("title")

        val mapper = FeedItemMapperImpl()
        val feedItemEntityObservable = mapper.transformToDomainModel(feedItemEntity)

        feedItemEntityObservable
                .subscribeOn(thread.get())
                .observeOn(thread.get())
                .subscribeWith(secondTestObserver)

        val feedItem = FeedItem("title")

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)

        secondTestObserver.assertValueCount(1)
        secondTestObserver.assertValue(feedItem)
        secondTestObserver.assertComplete()
        secondTestObserver.assertNoErrors()
    }

    @Test
    fun transformToEntityList_completeDomainModelList_willReturnEntityListObservable() {
        val feedItem = FeedItem(
                "title",
                "link",
                "description",
                "authorEmail",
                listOf("category"),
                "pubDate"
        )
        val feedItemList = arrayListOf(feedItem)

        val mapper = FeedItemMapperImpl()
        val feedItemEntitiesObservable = mapper.transformToEntityList(feedItemList)

        feedItemEntitiesObservable
                .subscribeOn(thread.get())
                .observeOn(thread.get())
                .subscribeWith(thirdTestObserver)

        val feedItemEntity = FeedItemEntity(
                "title",
                "link",
                "description",
                "authorEmail",
                listOf("category"),
                "pubDate"
        )
        val feedItemEntityList = arrayListOf(feedItemEntity)

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)

        thirdTestObserver.assertValueCount(1)
        thirdTestObserver.assertValue(feedItemEntityList)
        thirdTestObserver.assertComplete()
        thirdTestObserver.assertNoErrors()
    }

    @Test
    fun transformToEntityList_onlyRequiredDomainModelList_willReturnEntityListObservable() {
        val feedItem = FeedItem("title")
        val feedItemList = arrayListOf(feedItem)

        val mapper = FeedItemMapperImpl()
        val feedItemEntitiesObservable = mapper.transformToEntityList(feedItemList)

        feedItemEntitiesObservable
                .subscribeOn(thread.get())
                .observeOn(thread.get())
                .subscribeWith(thirdTestObserver)

        val feedItemEntity = FeedItemEntity("title")
        val feedItemEntityList = arrayListOf(feedItemEntity)

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)

        thirdTestObserver.assertValueCount(1)
        thirdTestObserver.assertValue(feedItemEntityList)
        thirdTestObserver.assertComplete()
        thirdTestObserver.assertNoErrors()
    }

    @Test
    fun transformToEntityList_domainModelEmptyList_willReturnEntityEmptyListObservable() {
        val feedItemEmptyList = ArrayList<FeedItem>()
        val feedItemEntityEmptyList = ArrayList<FeedItemEntity>()

        val mapper = FeedItemMapperImpl()
        val feedItemEntitiesObservable = mapper.transformToEntityList(feedItemEmptyList)

        feedItemEntitiesObservable
                .subscribeOn(thread.get())
                .observeOn(thread.get())
                .subscribeWith(thirdTestObserver)

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)

        thirdTestObserver.assertValueCount(1)
        thirdTestObserver.assertValue(feedItemEntityEmptyList)
        thirdTestObserver.assertComplete()
        thirdTestObserver.assertNoErrors()
    }

    @Test
    fun transformToDomainModelList_completeDomainModelList_willReturnEntityListObservable() {
        val feedItemEntity = FeedItemEntity(
                "title",
                "link",
                "description",
                "authorEmail",
                listOf("category"),
                "pubDate"
        )
        val feedItemEntityList = arrayListOf(feedItemEntity)

        val mapper = FeedItemMapperImpl()
        val feedItemEntitiesObservable = mapper.transformToDomainModelList(feedItemEntityList)

        feedItemEntitiesObservable
                .subscribeOn(thread.get())
                .observeOn(thread.get())
                .subscribeWith(fourthTestObserver)

        val feedItem = FeedItem(
                "title",
                "link",
                "description",
                "authorEmail",
                listOf("category"),
                "pubDate"
        )
        val feedItemList = arrayListOf(feedItem)

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)

        fourthTestObserver.assertValueCount(1)
        fourthTestObserver.assertValue(feedItemList)
        fourthTestObserver.assertComplete()
        fourthTestObserver.assertNoErrors()
    }

    @Test
    fun transformToDomainModelList_onlyRequiredDomainModelList_willReturnEntityListObservable() {
        val feedItemEntity = FeedItemEntity("title")
        val feedItemEntityList = arrayListOf(feedItemEntity)

        val mapper = FeedItemMapperImpl()
        val feedItemEntitiesObservable = mapper.transformToDomainModelList(feedItemEntityList)

        feedItemEntitiesObservable
                .subscribeOn(thread.get())
                .observeOn(thread.get())
                .subscribeWith(fourthTestObserver)

        val feedItem = FeedItem("title")
        val feedItemList = arrayListOf(feedItem)

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)

        fourthTestObserver.assertValueCount(1)
        fourthTestObserver.assertValue(feedItemList)
        fourthTestObserver.assertComplete()
        fourthTestObserver.assertNoErrors()
    }

    @Test
    fun transformToDomainModelList_entityEmptyList_willReturnDomainModelEmptyListObservable() {
        val feedItemList = ArrayList<FeedItem>()
        val feedItemEntityList = ArrayList<FeedItemEntity>()

        val mapper = FeedItemMapperImpl()
        val feedItemEntitiesObservable = mapper.transformToDomainModelList(feedItemEntityList)

        feedItemEntitiesObservable
                .subscribeOn(thread.get())
                .observeOn(thread.get())
                .subscribeWith(fourthTestObserver)

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)

        fourthTestObserver.assertValueCount(1)
        fourthTestObserver.assertValue(feedItemList)
        fourthTestObserver.assertComplete()
        fourthTestObserver.assertNoErrors()
    }
}