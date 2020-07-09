package com.jacob.portfolio.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.jacob.portfolio.models.User
import com.jacob.portfolio.models.UserItem
import com.jacob.portfolio.repositories.UsersRepository

class UsersViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG : String = "UsersViewModel"
    private val repository: UsersRepository = UsersRepository(application)
    private val users: LiveData<List<UserItem>>?
    private val progress: LiveData<Boolean>?

    init {
        users = repository.getUsers()
        progress = repository.getProgress()
    }

    fun getUsers() : LiveData<List<UserItem>>?{
        return users
    }

    fun setupUsers() {
        repository.setupUsers()
    }

    fun getUsersFromServer(){
        repository.getUsersFromServer()
    }

    fun getProgress() : LiveData<Boolean>?{
        return progress
    }

    override fun onCleared(){
        repository.clearDisposable()
    }
}