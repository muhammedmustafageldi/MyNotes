package com.swanky.mynotes.roomdatabase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.swanky.mynotes.model.Note
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable


@Dao
interface MyDao {

    @Query("SELECT * FROM Note")
    fun getAll(): Flowable<List<Note>>

    @Insert
    fun insert(note: Note): Completable

    @Delete
    fun delete(note: Note): Completable
}