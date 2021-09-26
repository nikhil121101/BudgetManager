package com.example.budgetmanager.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.budgetmanager.database.models.Converters
import com.example.budgetmanager.database.models.Tag
import com.example.budgetmanager.database.models.Token

@Database(entities = [Tag::class, Token::class], version = 1)
@TypeConverters(Converters::class)
abstract class LocalDatabase: RoomDatabase() {

    abstract fun dao() : Dao

    companion object {
        var INSTANCE : LocalDatabase? = null
        fun getDatabase(context: Context): LocalDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocalDatabase::class.java,
                    "tag_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

}