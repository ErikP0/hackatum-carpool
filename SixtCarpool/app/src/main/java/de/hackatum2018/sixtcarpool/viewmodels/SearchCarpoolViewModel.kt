package de.hackatum2018.sixtcarpool.viewmodels

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import de.hackatum2018.sixtcarpool.Repository
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.subjects.BehaviorSubject


class SearchCarpoolViewModel(repository: Repository) : ViewModel() {
    private val enableSearchButtonPublisher = BehaviorSubject.createDefault(false)
    val enableSearchButton = enableSearchButtonPublisher.toFlowable(BackpressureStrategy.LATEST)

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

    private fun checkValidity() {
        var valid = true
        if (from.isEmpty() || to.isEmpty()) valid = false
        if (startDateTime < System.currentTimeMillis() / 1000) valid = false
        enableSearchButtonPublisher.onNext(valid)
    }

    companion object {
        fun getFactory(repository: Repository) = SearchCarpoolViewModelFactory(repository)
    }
}

class SearchCarpoolViewModelFactory(private val repository: Repository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SearchCarpoolViewModel(repository) as T
    }
}