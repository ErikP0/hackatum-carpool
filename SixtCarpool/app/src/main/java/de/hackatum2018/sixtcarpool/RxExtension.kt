package de.hackatum2018.sixtcarpool

import android.arch.lifecycle.DefaultLifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function3
import io.reactivex.functions.Function4
import io.reactivex.schedulers.Schedulers

private fun throwOnError(throwable: Throwable): Nothing = throw throwable

fun <T1, T2, T3, T4, R> function4(f: (T1, T2, T3, T4) -> R): Function4<T1, T2, T3, T4, R> = Function4<T1, T2, T3, T4, R> { t1, t2, t3, t4 -> f(t1, t2, t3, t4) }

fun <T1, T2, T3, R> function3(f: (T1, T2, T3) -> R): Function3<T1, T2, T3, R> = Function3 { t1, t2, t3 -> f(t1, t2, t3) }

fun <T1, T2, R> function2(f: (T1, T2) -> R): BiFunction<T1, T2, R> = BiFunction { t1, t2 -> f(t1, t2) }

fun <T> Flowable<T>.subscribeInLifecycle(owner: LifecycleOwner, onNext: (T) -> Unit, onError: (Throwable) -> Unit = ::throwOnError): Disposable {
    val observer = FlowableLifecycleObserver(this, onNext, onError)
    owner.lifecycle.addObserver(observer)
    return observer.disposable
}

fun <T> Single<T>.subscribeInLifecycle(owner: LifecycleOwner, onSuccess: (T) -> Unit, onError: (Throwable) -> Unit = ::throwOnError) {
    val observer = SingleLifecycleObserver(this, onSuccess, onError)
    owner.lifecycle.addObserver(observer)
}

fun <T> Maybe<T>.subscribeInLifecycle(owner: LifecycleOwner, onSuccess: (T) -> Unit, onError: (Throwable) -> Unit = ::throwOnError, onComplete: () -> Unit = {}) {
    val observer = MaybeLifecycleObserver(this, onSuccess, onError, onComplete)
    owner.lifecycle.addObserver(observer)
}

fun Completable.subscribeInLifecycle(owner: LifecycleOwner, onComplete: () -> Unit, onError: (Throwable) -> Unit = ::throwOnError) {
    val observer = CompletableLifecycleObserver(this, onComplete, onError)
    owner.lifecycle.addObserver(observer)
}

private abstract class DisposableLifecycleObserver : DefaultLifecycleObserver {
    private var backingDisposable: Disposable? = null
    private var owner: LifecycleOwner? = null

    val disposable: Disposable by lazy { DisposableImpl() }

    abstract fun createDisposable(): Disposable

    override fun onStart(owner: LifecycleOwner) {
        this.owner = owner
        backingDisposable = createDisposable()
    }

    override fun onStop(owner: LifecycleOwner) {
        backingDisposable?.dispose()
        backingDisposable = null
    }

    override fun onDestroy(owner: LifecycleOwner) {
        remove()
    }

    protected fun remove() {
        backingDisposable?.dispose()
        backingDisposable = null
        owner?.lifecycle?.removeObserver(this)
        owner = null
    }

    private inner class DisposableImpl : Disposable {
        override fun dispose() {
            remove()
        }

        override fun isDisposed(): Boolean = backingDisposable == null
    }
}

private class FlowableLifecycleObserver<T>(private val flowable: Flowable<T>, private val onNext: (T) -> Unit, private val onError: (Throwable) -> Unit) : DisposableLifecycleObserver() {
    override fun createDisposable(): Disposable =
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(onNext, onError)
}

private class SingleLifecycleObserver<T>(private val single: Single<T>, private val onSuccess: (T) -> Unit, private val onError: (Throwable) -> Unit) : DisposableLifecycleObserver() {
    override fun createDisposable(): Disposable = single
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { remove() }
            .subscribe(onSuccess, onError)
}

private class MaybeLifecycleObserver<T>(private val maybe: Maybe<T>, private val onSuccess: (T) -> Unit, private val onError: (Throwable) -> Unit, private val onComplete: () -> Unit) : DisposableLifecycleObserver() {
    override fun createDisposable(): Disposable = maybe
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { remove() }
            .subscribe(onSuccess, onError, onComplete)
}

private class CompletableLifecycleObserver(private val completable: Completable, private val onComplete: () -> Unit, private val onError: (Throwable) -> Unit) : DisposableLifecycleObserver() {
    override fun createDisposable(): Disposable = completable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { remove() }
            .subscribe(onComplete, onError)
}