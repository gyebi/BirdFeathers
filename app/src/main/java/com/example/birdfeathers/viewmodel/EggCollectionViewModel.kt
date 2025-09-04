package com.example.birdfeathers.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.birdfeathers.domain.EggRepository
import com.example.birdfeathers.entity.EggCollection
import com.example.birdfeathers.entity.EggCollectionEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EggCollectionViewModel (
    private val repository: EggRepository,
    ): ViewModel() {

    val allEggs: Flow<List<EggCollectionEntity>> = repository.allEggCollections
    private val _todayTotal = MutableStateFlow(0)
    val todayTotal: StateFlow<Int> = _todayTotal

    fun observeTotalEggsForDate(date: String) = viewModelScope.launch {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        _todayTotal.value = repository.getMorningTotal(today) + repository.getEveningTotal(today)
    }

    //eggs collected for the day
    val todayEggTotal: Flow<Int> = repository.observeTotalEggsForDate(
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    )

    //hens counted for the day eggs were collected
    val todayLayingHens: Flow<Int> = repository.observeTotalHensForDate(
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    )






    fun saveEggCollection(
        entry: EggCollection
    ) =
        viewModelScope.launch {
            // Save to Room
            repository.saveEggCollection(entry)

        }
    }




class EggCollectionViewModelFactory(
    private val repository: EggRepository)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EggCollectionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EggCollectionViewModel(repository) as T
        } else
            throw IllegalArgumentException("Unknown ViewModel class")
    }
}

