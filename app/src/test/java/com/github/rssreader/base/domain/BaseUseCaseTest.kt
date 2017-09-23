package com.github.rssreader.base.domain

import com.natpryce.hamkrest.equalTo
import com.nhaarman.mockito_kotlin.given
import io.reactivex.Observable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations


class BaseUseCaseTest {

    private lateinit var useCase: UseCaseTestClass
    private lateinit var testObserver: TestDisposableObserver<Any>

    @Mock
    private lateinit var threadSelector: Thread

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
        given { threadSelector.get() }.willReturn(TestScheduler())
        useCase = UseCaseTestClass(threadSelector, threadSelector)
        testObserver = TestDisposableObserver()
    }

    @Test
    fun execute_useCaseObservableBuilding_willReturnCorrectResult() {
        useCase.execute(testObserver, Params.EMPTY)
        com.natpryce.hamkrest.assertion.assert.that(testObserver.valuesCount, equalTo(0))
    }

    @Test
    fun dispose_whenDispose_isDisposedWillReturnTrue() {
        useCase.execute(testObserver, Params.EMPTY)
        useCase.dispose()

        com.natpryce.hamkrest.assertion.assert.that(testObserver.isDisposed, equalTo(true))
    }

    private class UseCaseTestClass(subscriberThread: Thread,
                                   observerThread: Thread) :
            BaseUseCase<Any, Params.Companion>(subscriberThread, observerThread) {

        override fun getObservable(params: Params.Companion): Observable<Any> = Observable.empty()

    }

    private class TestDisposableObserver<T> : DisposableObserver<T>() {
        var valuesCount = 0

        override fun onNext(t: T) {
            valuesCount++
        }

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