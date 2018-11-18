package de.hackatum2018.sixtcarpool.viewmodels

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import de.hackatum2018.sixtcarpool.Repository
import de.hackatum2018.sixtcarpool.database.entities.CarpoolOffer
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.subjects.BehaviorSubject
import java.util.*


class OpenCarpoolViewModel(val rentalId: Int, private val repository: Repository): ViewModel() {
    val rental = repository.getMyRentalById(rentalId)

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

    var startDateTime: Long = 0
        set(value) {
            field = value
            checkValidity()
        }

    var maxPassengers: Int = 0
        set(value) {
            field = value
            checkValidity()
        }

    var price: Int = 0
        set(value) {
            field = value
            checkValidity()
        }

    private val buttonEnabledPublisher = BehaviorSubject.createDefault(false)
    val buttonEnabled: Flowable<Boolean> = buttonEnabledPublisher.toFlowable(BackpressureStrategy.LATEST)

    private fun checkValidity(): Unit {
        rental.firstOrError().subscribe { carRental ->
            if (carRental == null) throw IllegalArgumentException("Car rental is null")
            var valid = true
            if (from.isEmpty() || to.isEmpty()) valid = false
            if (startDateTime < carRental.pickupTime) valid = false
            if (maxPassengers <= 0 || maxPassengers > carRental.maxPassengers - 1) valid = false
            if (price < 0) valid = false
            buttonEnabledPublisher.onNext(valid)

        }
    }

    fun openToCarpool(): Completable = repository.addCarpoolOfferAndUpdateLinks(CarpoolOffer(
        placeFrom = from,
        placeTo = to,
        carRentalId = rentalId,
        id = UUID.randomUUID().leastSignificantBits.toInt(),
        maxPassengers = maxPassengers,
        pricePerPassenger = price,
        startDate = startDateTime,
        startTime = startDateTime

    ))

    companion object {
        fun getFactory(id: Int, repository: Repository) = OpenCarpoolViewModelFactory(id, repository)
    }
}

class OpenCarpoolViewModelFactory(private val id: Int, private val repository: Repository): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return OpenCarpoolViewModel(id, repository) as T
    }
}