package de.hackatum2018.sixtcarpool.viewmodels

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import de.hackatum2018.sixtcarpool.database.entities.MyCarRentalEntity
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.BehaviorSubject


class RentalListViewModel : ViewModel() {
    private val testPublisher = BehaviorSubject.createDefault(listOf(
            MyCarRentalEntity("42828-4232", "BMW X5", 4, "http://www.sixtblog.com/wp-content/uploads/2015/01/bmw_x3-sixt-rental-car_d31f5b.png",
                    234, "Munich Airport", 1544618400, 254, "Berlin Central Station", 1547307000, 50, null),
            MyCarRentalEntity("42828-4211", "Chevrolet", 4, "http://visitroo.com/img/mx/a/1420573073-1-SIXT_Car_Rental.jpg",
                    254, "Berlin Central Station", 1555063200, 234, "Munich Airport", 1557673200, 50, null)
    ))
    val rentalList: Flowable<List<MyCarRentalEntity>> = testPublisher.toFlowable(BackpressureStrategy.LATEST)

    companion object {
        fun getFactory() = RentalListViewModelFactory()
    }
}

class RentalListViewModelFactory() : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RentalListViewModel() as T
    }
}