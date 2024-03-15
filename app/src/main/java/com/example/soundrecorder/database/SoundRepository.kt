package com.example.soundrecorder.database

import androidx.lifecycle.LiveData

class SoundRepository(private val soundDao: SoundDao) {
    val readAllData: LiveData<List<Sound>> = soundDao.readAllData()

    suspend fun addSound(sound: Sound){
        soundDao.addSound(sound)
    }

    fun readSound(audio:String):Sound?{
        return soundDao.readSound(audio)
    }

    suspend fun updateSound(sound: Sound){
        soundDao.updateSound(sound)
    }
    suspend fun nukeTable(){
        soundDao.resetAutoIncrement()
        soundDao.nukeTable()
    }
    suspend fun deleteUser(sound: Sound){
        soundDao.deleteSound(sound)
    }
}