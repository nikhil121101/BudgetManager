package com.example.budgetmanager.ui.home

import androidx.lifecycle.*
import com.example.budgetmanager.database.Dao
import com.example.budgetmanager.database.models.Token
import com.example.budgetmanager.ui.createData.CreateDataViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(private val dao: Dao) : ViewModel() {
    val selectionType = MutableLiveData<Int>(0)

    val incomeSum = MutableLiveData<Double>(0.0)

    val expenseSum = MutableLiveData<Double>(0.0)

    var tokensList: LiveData<List<Token>> = Transformations.switchMap(selectionType) {
        when(it) {
            1 -> dao.getTodayTokens()
            2 -> dao.getThisWeekTokens()
            3 -> dao.getThisMonthTokens()
            4 -> dao.getAllTokens()
            else -> dao.getTodayTokens()
        }
    }

    fun calculateSum() {
        var income = 0.0;
        var expense = 0.0;
        for(token: Token in tokensList.value!!) {
            if(token.type == 1) {
                income += token.amount;
            }
            else {
                expense += token.amount
            }
        }
        incomeSum.value = income
        expenseSum.value = expense
    }

    fun setAllTokens() {

        tokensList = dao.getAllTokens()
    }

    fun setTodayTokens() {
        tokensList = dao.getTodayTokens()
    }

    fun setThisWeekTokens() {
        tokensList = dao.getThisWeekTokens()
    }

    fun setThisMonthTokens() {
        tokensList = dao.getThisMonthTokens()
    }

    fun deleteToken(token: Token) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.removeToken(token)
        }
    }
    fun addToken(token: Token) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.addToken(token)
        }
    }
}

class HomeViewModelFactory(private val dao: Dao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}