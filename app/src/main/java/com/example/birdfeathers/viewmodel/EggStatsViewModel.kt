package com.example.birdfeathers.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.birdfeathers.data.EggStatsEntry
import com.example.birdfeathers.domain.EggRepository
import com.example.birdfeathers.entity.EggCollectionEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate


class EggStatsViewModel(private val repository: EggRepository) : ViewModel() {

    //raw list from RM DB
    val allEggs: StateFlow<List<EggCollectionEntity>> =
        repository.allEggCollections .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    //getting Data for the week and plotting
    fun getWeeklyEggStats() {
        val _chartData = MutableStateFlow<List<EggStatsEntry>>(emptyList())
        val chartData: StateFlow<List<EggStatsEntry>> = _chartData

        viewModelScope.launch {
            val today = LocalDate.now()
            val last7Days = (0..6).map { today.minusDays(it.toLong()) }.reversed()
            val rawStats = repository.getEggStatsBetweenDates(
                start = last7Days.first().toString(),
                end = last7Days.last().toString()
            )

            val statsMap = rawStats.associateBy { it.date }

            val filledStats = last7Days.map { date ->
                statsMap[date.toString()] ?: EggStatsEntry(date = date.toString(), eggCount = 0, targetLay = 90.0f)
            }

            _chartData.value = filledStats
        }


    }

    //getting Data for the month  and plotting
    fun getMonthlyEggStats() {
        val _chartData = MutableStateFlow<List<EggStatsEntry>>(emptyList())
        val chartData: StateFlow<List<EggStatsEntry>> = _chartData

        viewModelScope.launch {
            val today = LocalDate.now()
            val last30Days = (0..29).map { today.minusDays(it.toLong()) }.reversed()
            val rawStats = repository.getEggStatsBetweenDates(
                start = last30Days.first().toString(),
                end = last30Days.last().toString()
            )

            val statsMap = rawStats.associateBy { it.date }

            val filledStats = last30Days.map { date ->
                statsMap[date.toString()] ?: EggStatsEntry(date = date.toString(), eggCount = 0, targetLay = 90.0f)
            }

            _chartData.value = filledStats
        }


    }


    //transformed list for charting
    val chartData: StateFlow<List<EggStatsEntry>> = allEggs
        .map { eggList ->
            eggList
                .groupBy { it.date }
                .map { (date, entries) ->
                    EggStatsEntry(
                        date = date,
                        eggCount = entries.sumOf { it.eggCount },
                        targetLay = 90f // or make this dynamic later
                    )
                }.sortedBy { it.date }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())



    class EggStatsViewModelFactory(
        private val repository: EggRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(EggStatsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return EggStatsViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
    }

