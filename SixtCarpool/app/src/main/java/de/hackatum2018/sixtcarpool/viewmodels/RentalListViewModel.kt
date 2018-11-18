package de.hackatum2018.sixtcarpool.viewmodels

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import de.hackatum2018.sixtcarpool.Repository


class RentalListViewModel(repository: Repository) : ViewModel() {

    val rentalList = repository.getMyRentalsAll()
    val pairsList = repository.getPairsOfRentalsAndOffers(true)

    companion object {
        fun getFactory(repository: Repository) = RentalListViewModelFactory(repository)
    }
}

class RentalListViewModelFactory(private val repository: Repository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RentalListViewModel(repository) as T
    }
}