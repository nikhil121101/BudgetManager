package com.example.budgetmanager.database

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import com.example.budgetmanager.database.models.MonthlyResult
import com.example.budgetmanager.database.models.Tag
import com.example.budgetmanager.database.models.Token

@Dao
interface Dao {
    @Query("Select value from Tag")
    fun getTags(): LiveData<List<String>>

    @Insert
    suspend fun addTag(tag: Tag)

    @Delete
    suspend fun removeTag(tag: Tag)

    @Query("Select * from Token order by dateLong desc")
    fun getAllTokens(): LiveData<List<Token>>

    @Query("Select * from Token WHERE day = strftime('%d', 'now') order by dateLong desc")
    fun getTodayTokens(): LiveData<List<Token>>

    @Query("Select * from Token WHERE week = strftime('%W', 'now') order by dateLong desc")
    fun getThisWeekTokens(): LiveData<List<Token>>

    @Query("Select * from Token WHERE month = strftime('%m', 'now') order by dateLong desc")
    fun getThisMonthTokens(): LiveData<List<Token>>

    @Query("Select dateLong, SUM(incomeAmount) as incomeSum, SUM(expenseAmount) as expenseSum from Token Group by month, year order by year desc, month desc")
    fun getMonthlyResult(): LiveData<List<MonthlyResult>>

    @Query("Select * from Token where month = (:month+1) and year = :year")
    fun getSpecificMonthTokens(month: Int, year: Int): LiveData<List<Token>>

    @Insert
    suspend fun addToken(token: Token)

    @Delete
    suspend fun removeToken(token: Token)

}
