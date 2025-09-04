package com.example.birdfeathers.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FinancialViewModel : ViewModel()  {

    var isLoading by mutableStateOf(true)

    var numBirds by mutableStateOf(0)
    var mortalityRate by mutableStateOf(0f)
    var salePricePerBird by mutableStateOf(0f)

    var dayOldChicksCost by mutableStateOf(0f)
    var feedCost by mutableStateOf(0f)
    var waterCost by mutableStateOf(0f)
    var coopCost by mutableStateOf(0f)
    var drinkersCost by mutableStateOf(0f)
    var feedersCost by mutableStateOf(0f)
    var medsCost by mutableStateOf(0f)
    var shavingCost by mutableStateOf(0f)
    var labourCost by mutableStateOf(0f)
    var miscCost by mutableStateOf(0f)
    var transportCost by mutableStateOf(0f)


    //var salePricePerBird by mutableStateOf(45f)

    val totalCost: Float
        get() = dayOldChicksCost + feedCost + waterCost + coopCost + drinkersCost +
                feedersCost + medsCost + shavingCost + labourCost + miscCost + transportCost

    init {
        // Simulate DB load or Firebase fetch
        viewModelScope.launch {
            delay(1500)
            isLoading = false
        }
    }
}