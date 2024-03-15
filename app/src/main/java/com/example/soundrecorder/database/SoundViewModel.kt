package com.example.soundrecorder.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SoundViewModel(application: Application):AndroidViewModel(application) {
    val readAllData:LiveData<List<Sound>>
    private val repository:SoundRepository

    private val _user = MutableLiveData<Sound>()
    val sound: LiveData<Sound> get() = _user

    init {
        val soundDao=SoundDatabase.getDatabase(application).soundDao()
        repository= SoundRepository(soundDao)
        readAllData=repository.readAllData
    }

    fun addSound(sound:Sound){
        viewModelScope.launch(Dispatchers.IO){
            repository.addSound(sound)
        }
    }

    fun readSound(audio:String){
        viewModelScope.launch(Dispatchers.IO){
            _user.postValue(repository.readSound(audio))
        }
    }

    fun updateSound(sound: Sound){
        viewModelScope.launch(Dispatchers.IO){
            repository.updateSound(sound)
        }
    }

    fun nukeTable(){
        viewModelScope.launch(Dispatchers.IO){
            repository.nukeTable()
        }
    }

    fun deleteSound(sound: Sound){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteUser(sound)
        }
    }
}