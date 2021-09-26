package com.example.budgetmanager.ui.createData

import androidx.lifecycle.*
import com.example.budgetmanager.database.Dao
import com.example.budgetmanager.database.models.Tag
import com.example.budgetmanager.database.models.Token
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class CreateDataViewModel(private val dao: Dao) : ViewModel() {

    val selectedDate = MutableLiveData<Date>()

    val selectedType = MutableLiveData<Int>(1)

    private val _availableTags = dao.getTags()
    val availableTags: LiveData<List<String>> = _availableTags


    private val _selectedTags = MutableLiveData<MutableList<String>>(ArrayList())
    val selectedTags: LiveData<MutableList<String>> = _selectedTags

    fun clearSelectedTags() {
        _selectedTags.value!!.clear()
    }

    fun setSelectedTag(list: MutableList<String>) {
        _selectedTags.value = list
    }

    fun addAvailableTag(s: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.addTag(Tag(s))
        }
    }

    fun removeAvailableTag(s: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.removeTag(Tag(s))
        }
    }

    fun addToken(amount: String, title: String, description: String): Token {
        val token = Token(
            dateLong = selectedDate.value!!.time, type = selectedType.value!!, amount = amount.toDouble(),
            tags = selectedTags.value!!, title = title, description = description)
        viewModelScope.launch(Dispatchers.IO) {
            dao.addToken(token)
        }
        return token
    }

    fun deleteToken(token: Token) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.removeToken(token)
        }
    }

}

class CreateDataModelFactory(private val dao: Dao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateDataViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CreateDataViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}