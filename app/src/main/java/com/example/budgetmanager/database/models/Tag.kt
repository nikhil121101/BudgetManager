package com.example.budgetmanager.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Tag(@PrimaryKey val value:String)
