package com.example.budgetmanager.ui.history

import androidx.lifecycle.*
import com.example.budgetmanager.database.Dao
import com.example.budgetmanager.database.models.MonthlyResult
import com.example.budgetmanager.database.models.Token

class MonthlyHistoryViewModel(val dao: Dao) : ViewModel() {

    val monthlyList: LiveData<List<MonthlyResult>> = dao.getMonthlyResult()

    lateinit var specificMonthTokens: LiveData<List<Token>>
    fun setSpecificMonthToken(month: Int, year: Int) {
        specificMonthTokens = dao.getSpecificMonthTokens(month, year)
    }

    val specificDateLong = MutableLiveData<Long>(0)

    val incomeSum = MutableLiveData<Double>(0.0)

    val expenseSum = MutableLiveData<Double>(0.0)

    fun calculateSum() {
        var income = 0.0;
        var expense = 0.0;
        for(token: Token in specificMonthTokens.value!!) {
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

}

class MonthlyHistoryModelFactory(private val dao: Dao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MonthlyHistoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MonthlyHistoryViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}