package com.example.soundrecorder.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Sound::class], version = 1)
abstract class SoundDatabase:RoomDatabase() {
    abstract fun soundDao():SoundDao

    companion object{
        @Volatile
        private var INSTANCE:SoundDatabase?=null

        fun getDatabase(context: Context):SoundDatabase{
            val tempInstance= INSTANCE
            if(tempInstance!=null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SoundDatabase::class.java,
                    "sounds_database"
                ).build()
                INSTANCE=instance
                return instance
            }
        }
    }
}