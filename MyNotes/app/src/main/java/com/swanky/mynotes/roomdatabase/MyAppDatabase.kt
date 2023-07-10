package com.swanky.mynotes.roomdatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.swanky.mynotes.model.Note

@Database(entities = [Note::class], version = 2)
abstract class MyAppDatabase : RoomDatabase() {
    abstract fun getDao(): MyDao

}