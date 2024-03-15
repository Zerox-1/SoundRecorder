package com.example.soundrecorder.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface SoundDao {
    @Insert
    suspend fun addSound(sound: Sound)

    @Query("select * from sounds_table")
    fun readAllData():LiveData<List<Sound>>

    @Query("select * from sounds_table where audio=:audio")
    fun readSound(audio:String):Sound?

    @Update
    suspend fun updateSound(sound: Sound)

    @Query("delete from sounds_table")
    suspend fun nukeTable()

    @Query("delete from sqlite_sequence where name='sounds_table_id'")
    suspend fun resetAutoIncrement()

    @Delete
    suspend fun deleteSound(sound: Sound)
}