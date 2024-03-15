package com.example.soundrecorder.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sounds_table")
data class Sound(
    @PrimaryKey(autoGenerate = true)
    var id: Int?,
    var author:String,
    var audio:String,
    var date_cr:String
)
