package com.example.budgetmanager.ui.displayData

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.budgetmanager.database.models.Token

class DisplayTokenViewModel: ViewModel() {
    val displayToken =  MutableLiveData<Token>()

    val tokenRetrieved = MutableLiveData<Boolean>(true)

    fun setDisplayToken(token: Token) {
        displayToken.value = token
    }
}