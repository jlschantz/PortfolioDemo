package com.jacob.portfolio.repositories

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jacob.portfolio.R
import com.jacob.portfolio.database.AppDatabase
import com.jacob.portfolio.http.RetrofitBuilder
import com.jacob.portfolio.models.User
import com.jacob.portfolio.models.UserItem
import com.jacob.portfolio.utilities.CommonUtilities
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class UsersRepository (application: Application) {

    private val TAG : String = "UsersRepository"
    private var compositeDisposable: CompositeDisposable? = null
    private var prefs : SharedPreferences
    val lastUpdateTimeUsers : String = "lastUpdateTimeUsers"
    private var context: Context = application.applicationContext
    private var users: LiveData<List<UserItem>>
    private var progress: MutableLiveData<Boolean>
    private var db: AppDatabase
    private val commonUtilities : CommonUtilities = CommonUtilities()

    init {
        prefs = this.context.getSharedPreferences("com.jacob.portfolio", Context.MODE_PRIVATE)
        db = AppDatabase(this.context)
        compositeDisposable = CompositeDisposable()
        progress = MutableLiveData()
        progress.value = false
        users = db.usersDAO().getAllItems()
        setupUsers()
    }

    fun getUsers() : LiveData<List<UserItem>>?{
        return users
    }

    fun getProgress() : LiveData<Boolean>?{
        return progress
    }

    fun setupUsers() {
        Log.v(TAG,"setupUsers")
        progress.value = true
        val currentTime = Date(System.currentTimeMillis())
        val savedTime = Date(prefs.getLong(lastUpdateTimeUsers, 0))
        if (commonUtilities.getMinutesDifference(currentTime,savedTime)>=context.resources.getInteger(
                R.integer.time_interval)){
            getUsersFromServer()
        }else{
            getUsersFromDatabase()
        }
    }

    fun getUsersFromServer() {
        Log.v(TAG,"getUsersFromServer")
        progress.value = true
        compositeDisposable?.add(
            RetrofitBuilder.apiService.getUsers()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponseUsers, this::handleErrorFromServer))
    }

    private fun handleResponseUsers(usersList: List<User>) {
        Log.v(TAG, "handleResponseUsers")
        saveUsersToDatabase(usersList)
    }

    private fun handleErrorFromServer(throwable: Throwable) {
        Log.v(TAG, "handleErrorFromServer ${throwable.message}")
        getUsersFromDatabase()
    }

    private fun saveUsersToDatabase(usersList: List<User>) {
        Log.v(TAG, "saveUsersToDatabase")
        val db = AppDatabase(this.context)

        Completable.fromAction { db.usersDAO().updateTable(usersList) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable?.add(d)
                }

                override fun onComplete() {
                    val date = Date(System.currentTimeMillis())
                    prefs.edit().putLong(lastUpdateTimeUsers, date.getTime()).apply()
                    getUsersFromDatabase()
                }

                override fun onError(throwable: Throwable) {
                    Log.v(TAG, "saveUsersToDatabase onError ${throwable.message}")
                }
            })
    }

    private fun getUsersFromDatabase() {
        Log.v(TAG,"getUsersFromDatabase")
        //LiveData takes care of everything but shutting off the progress spinner
        Handler().postDelayed({ progress.value = false }, 100)
    }

    fun clearDisposable() {
        Log.v(TAG,"clearDisposable")
        compositeDisposable?.clear()
    }
}