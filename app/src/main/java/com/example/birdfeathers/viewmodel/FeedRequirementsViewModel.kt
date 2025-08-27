package com.example.birdfeathers.viewmodel

import FeedRequirementsRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FeedRequirementsViewModel(
    private val repository: FeedRequirementsRepository
) : ViewModel() {

    private val _selectedBirdType = MutableStateFlow("Broiler") // Default to "Broiler"
    val selectedBirdType: StateFlow<String> = _selectedBirdType.asStateFlow()

    private val _starter = MutableStateFlow("")
    val starter: StateFlow<String> = _starter.asStateFlow()

    private val _grower = MutableStateFlow("")
    val grower: StateFlow<String> = _grower.asStateFlow()

    private val _finisher = MutableStateFlow("")
    val finisher: StateFlow<String> = _finisher.asStateFlow()


    // Load feed requirements for selected bird type
    fun loadFeedRequirements(birdType: String){
         _selectedBirdType.value = birdType
        viewModelScope.launch {
            val feedList = repository.getFeedByBirdType(birdType)
            _starter.value = feedList.find { it.nameStage.equals("Starter", ignoreCase = true) }?.kilosPerPhase?.toString() ?: ""
            _grower.value = feedList.find { it.nameStage.equals("Grower", ignoreCase = true) }?.kilosPerPhase?.toString() ?: ""
            _finisher.value = feedList.find { it.nameStage.equals("Finisher", ignoreCase = true) }?.kilosPerPhase?.toString() ?: ""
        }
    }

    // Number of birds input

    private val _numBirds = MutableStateFlow(0)
    val numBirds: StateFlow<Int> = _numBirds.asStateFlow()

    fun setNumBirds(count: Int) {
        _numBirds.value = count
    }

    val totalFeed = combine(starter, grower, finisher, numBirds) { s, g, f, n ->
        val starterKg = s.toDoubleOrNull() ?: 0.0
        val growerKg = g.toDoubleOrNull() ?: 0.0
        val finisherKg = f.toDoubleOrNull() ?: 0.0
        val birds = n
        (starterKg + growerKg + finisherKg) * birds
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        0.0

    )

    //growth stage totals

    val starterTotalFeed = combine(starter, numBirds) { s, n ->
        (s.toDoubleOrNull() ?: 0.0) * n
    }.stateIn(viewModelScope, SharingStarted.Eagerly, 0.0)

    val growerTotalFeed = combine(grower, numBirds) { g, n ->
        (g.toDoubleOrNull() ?: 0.0) * n
    }.stateIn(viewModelScope, SharingStarted.Eagerly, 0.0)

    val finisherTotalFeed = combine(finisher, numBirds) { f, n ->
        (f.toDoubleOrNull() ?: 0.0) * n
    }.stateIn(viewModelScope, SharingStarted.Eagerly, 0.0)

    private val _unitMode = MutableStateFlow("kg") // or "bags"
    val unitMode: StateFlow<String> = _unitMode.asStateFlow()

    private val _bagSize = MutableStateFlow(50) // default to 50kg bags
    val bagSize: StateFlow<Int> = _bagSize.asStateFlow()

    fun setUnitMode(mode: String) {
        _unitMode.value = mode
    }

    fun setBagSize(size: Int) {
        _bagSize.value = size
    }

    fun bagsAndRemainder(totalKg: Double, bagSize: Int): Pair<Int, Double> {
        val bags = (totalKg / bagSize).toInt()
        val remainder = totalKg - (bags * bagSize)
        return bags to remainder
    }



    /*
    fun setNumBirds(count: Int) {
        _numBirds.value = count
    }

    fun setFeedPrice(phase: String, price: Double) {
        _feedPrices.value = _feedPrices.value.toMutableMap().apply { put(phase, price) }
    }



    // Total feed required for all birds
    val totalFeedKg: Double
        get() = feedRequirements.value.sumOf { it.kilosPerPhase } * numBirds.value

    // Total feed cost for all birds
    val totalFeedCost: Double
        get() = feedRequirements.value.sumOf {
            val price = feedPrices.value[it.nameStage] ?: 0.0
            it.kilosPerPhase * numBirds.value * price
        }

    // Breakdown per phase for display in UI
    fun feedBreakdown(): List<Triple<String, Double, Double>> {
        return feedRequirements.value.map {
            val price = feedPrices.value[it.nameStage] ?: 0.0
            val totalKg = it.kilosPerPhase * numBirds.value
            val totalCost = totalKg * price
            Triple(it.nameStage, totalKg, totalCost)
        }
    }

    // Optionally, sync from Firebase to Room (for admin/update button)
    fun syncFeedRequirementsFromFirebase(onComplete: (() -> Unit)? = null) {
        repository.syncFromFirebaseToRoom(onComplete)
    }

 */
}

class FeedRequirementsViewModelFactory(private val repository: FeedRequirementsRepository): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FeedRequirementsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return FeedRequirementsViewModel(repository) as T
            } else
                throw IllegalArgumentException("Unknown ViewModel class")
        }
    }


