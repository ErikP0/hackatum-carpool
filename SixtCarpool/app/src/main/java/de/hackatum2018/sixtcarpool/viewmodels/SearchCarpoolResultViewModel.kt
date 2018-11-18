package de.hackatum2018.sixtcarpool.viewmodels

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import de.hackatum2018.sixtcarpool.Repository
import de.hackatum2018.sixtcarpool.database.entities.CarRental
import de.hackatum2018.sixtcarpool.database.entities.CarpoolOffer
import io.reactivex.BackpressureStrategy
import io.reactivex.Single


class SearchCarpoolResultViewModel(private val repository: Repository) : ViewModel() {

    fun search(
        from: String, to: String, time: Long, radiusInMinutes: Int
    ): Single<List<Pair<CarpoolOffer, CarRental>>> =
        repository.getPairsOfRentalsAndOffers(false).toFlowable(BackpressureStrategy.LATEST)
            .map { list ->
                list.filter { (rental, offer) ->
                    offer!!.placeFrom.equals(from, true) && offer.placeTo.equals(to, true)
                }.map { (a, b) -> Pair(b!!, a) }
            }.firstOrError()

    companion object {
        fun getFactory(repository: Repository) = SearchCarpoolResultViewModelFactory(repository)
    }
}

class SearchCarpoolResultViewModelFactory(private val repository: Repository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SearchCarpoolResultViewModel(repository) as T
    }
}