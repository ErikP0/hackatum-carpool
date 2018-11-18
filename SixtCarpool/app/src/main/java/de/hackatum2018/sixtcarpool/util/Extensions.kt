package de.hackatum2018.sixtcarpool.util

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Aleksandr Kim on 17 Nov, 2018 4:27 PM for SixtCarpool
 */

fun <T> Flowable<T>.commonSchedulers(): Flowable<T> {
    return this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}
fun Completable.commonSchedulers(): Completable {
    return this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}