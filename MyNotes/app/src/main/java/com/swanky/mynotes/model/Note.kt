package com.swanky.mynotes.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Note(
    @ColumnInfo(name = "notesBody") val noteBody: String?,
    @ColumnInfo(name = "saveDate") val saveDate: String?
) {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
}