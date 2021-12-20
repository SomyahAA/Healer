package com.example.healer.ui.fragments.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.healer.models.Psychologist
import com.example.healer.repository.Repository
import kotlinx.coroutines.launch


class HomeViewModel :ViewModel(){

    private val repo = Repository.getInstace()

    fun psyLiveData() :LiveData<List<Psychologist>>{
        val liveDataList = liveData {
            emit(repo.getAllPsys())
        }
        return liveDataList
    }

    fun userTypeIsUser() :Boolean{
        var state = false
        viewModelScope.launch {
            state = repo.userTypeIsUser()
        }.invokeOnCompletion {
            state  //return repo state ?
        }
        return false
    }

    fun readPsyDataFromFirestore (): LiveData<Psychologist> {
        return repo.readPsyDataFromFirestore()
    }


}