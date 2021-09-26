package com.example.budgetmanager.database.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import java.util.*

@Entity
@Parcelize

data class Token(
   var dateLong: Long,
   var type: Int,
   var amount: Double,
   var tags: MutableList<String>?,
   var title: String?,
   var description: String?,
): Parcelable {
   @PrimaryKey(autoGenerate = true)
   var uid: Long = 0

   var incomeAmount: Double = 0.0
   var expenseAmount: Double = 0.0

   var day: Int = 0
   var week: Int = 0
   var month: Int = 0
   var year: Int = 0
   init {

      if(type == 1) {
         incomeAmount = amount
      }
      else {
         expenseAmount = amount
      }

      val calendar = Calendar.getInstance()
      calendar.timeInMillis = dateLong
      day = calendar.get(Calendar.DAY_OF_MONTH)
      week = calendar.get(Calendar.WEEK_OF_YEAR) - 1
      month = calendar.get(Calendar.MONTH) + 1
      year = calendar.get(Calendar.YEAR)
   }
}


class Converters {
   @TypeConverter
   fun fromList(value : MutableList<String>) = Json.encodeToString(value)

   @TypeConverter
   fun toList(value: String) = Json.decodeFromString<MutableList<String>>(value)
}