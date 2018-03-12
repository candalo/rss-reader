package com.github.rssreader.base.domain

import com.natpryce.hamkrest.equalTo
import com.nhaarman.mockito_kotlin.given
import io.reactivex.Completable
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class BaseCompletableUseCaseTest {

    private lateinit var useCase: UseCaseTestClass
    private lateinit var testObserver: TestDisposableCompletableObserver

    @Mock
    private lateinit var threadSelector: Thread

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
        given { threadSelector.get() }.willReturn(TestScheduler())
        useCase = UseCaseTestClass(threadSelector, threadSelector)
        testObserver = TestDisposableCompletableObserver()
    }

    @Test
    fun dispose_whenDispose_isDisposedWillReturnTrue() {
        useCase.execute(testObserver, Params.EMPTY)
        useCase.dispose()

        com.natpryce.hamkrest.assertion.assert.that(testObserver.isDisposed, equalTo(true))
    }

    private class UseCaseTestClass(subscriberThread: Thread,
                                   observerThread: Thread) :
            BaseCompletableUseCase<Params.Companion>(subscriberThread, observerThread) {

        override fun getCompletable(params: Params.Companion): Completable = Completable.complete()

    }

    private class TestDisposableCompletableObserver : DisposableCompletableObserver() {
        override fun onError(e: Throwable) {
        }

        override fun onComplete() {
        }
    }

    private class Params private constructor() {
        companion object {
            val EMPTY = Params
        }
    }
}