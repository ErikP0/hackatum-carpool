package de.hackatum2018.sixtcarpool.viewmodels

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import de.hackatum2018.sixtcarpool.Repository
import de.hackatum2018.sixtcarpool.database.entities.CarRental
import de.hackatum2018.sixtcarpool.database.entities.CarpoolOffer
import io.reactivex.Completable
import io.reactivex.Single


class SearchCarpoolResultViewModel(repository: Repository) : ViewModel() {

    fun search(radiusInMinuted: Int): Single<List<Pair<CarpoolOffer, CarRental>>> =
        Completable.never().toSingle { emptyList<Pair<CarpoolOffer, CarRental>>() }

    companion object {
        fun getFactory(repository: Repository) = SearchCarpoolResultViewModelFactory(repository)
    }
}

class SearchCarpoolResultViewModelFactory(private val repository: Repository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SearchCarpoolResultViewModel(repository) as T
    }
}