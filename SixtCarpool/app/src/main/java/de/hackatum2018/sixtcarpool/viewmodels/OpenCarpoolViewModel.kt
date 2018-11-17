package de.hackatum2018.sixtcarpool.viewmodels

import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.BehaviorSubject


class OpenCarpoolViewModel(val rentalId: String) {
    var from: String = ""
    set(value) {
        field = value
        checkValidity()
    }

    var to: String = ""
        set(value) {
            field = value
            checkValidity()
        }

//    var startTime: Long =
//        set(value) {
//            field = value
//            checkValidity()
//        }

    private val buttonEnabledPublisher = BehaviorSubject.createDefault(false)
    val buttonEnabled: Flowable<Boolean> = buttonEnabledPublisher.toFlowable(BackpressureStrategy.LATEST)

    private fun checkValidity() {

    }
}