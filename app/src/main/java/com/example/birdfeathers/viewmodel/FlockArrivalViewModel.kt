package com.example.birdfeathers.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.birdfeathers.data.FlockArrival
import com.example.birdfeathers.entity.FlockArrivalEntity
import com.example.birdfeathers.domain.FlockArrivalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class FlockArrivalViewModel(private val repository: FlockArrivalRepository):
    ViewModel() {
        val allFlockArrivals: Flow<List<FlockArrivalEntity>> = repository.allFlockArrivals



    fun saveFlockArrival(arrival: FlockArrival) {
        viewModelScope.launch {
            repository.saveFlockArrival(arrival)
        }
    }

    // Expose all arrivals as Flow or LiveData
    //val flockArrivals = repository.getAllFlockArrivals()
}

class FlockArrivalViewModelFactory(private val repository: FlockArrivalRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FlockArrivalViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FlockArrivalViewModel(repository) as T
        } else
            throw IllegalArgumentException("Unknown ViewModel class")
    }
}

