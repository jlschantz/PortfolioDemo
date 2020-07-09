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
import com.jacob.portfolio.models.Album
import com.jacob.portfolio.models.AlbumItem
import com.jacob.portfolio.utilities.CommonUtilities
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class AlbumsRepository (application: Application) {

    private val TAG : String = "AlbumsRepository"
    private var compositeDisposable: CompositeDisposable? = null
    private var prefs : SharedPreferences
    val lastUpdateTimeAlbums : String = "lastUpdateTimeAlbums"
    private var context: Context = application.applicationContext
    private var albums: LiveData<List<AlbumItem>>
    private var progress: MutableLiveData<Boolean>
    private var db: AppDatabase
    private val commonUtilities : CommonUtilities = CommonUtilities()

    init {
        prefs = this.context.getSharedPreferences("com.jacob.portfolio", Context.MODE_PRIVATE)
        db = AppDatabase(this.context)
        compositeDisposable = CompositeDisposable()
        progress = MutableLiveData()
        progress.value = false
        albums = db.albumsDAO().getAllItems()
        setupAlbums()
    }

    fun getAlbums() : LiveData<List<AlbumItem>>?{
        return albums
    }

    fun getProgress() : LiveData<Boolean>?{
        return progress
    }

    fun setupAlbums() {
        Log.v(TAG,"setupAlbums")
        progress.value = true
        val currentTime = Date(System.currentTimeMillis())
        val savedTime = Date(prefs.getLong(lastUpdateTimeAlbums, 0))
        if (commonUtilities.getMinutesDifference(currentTime,savedTime)>=context.resources.getInteger(
                R.integer.time_interval)){
            getAlbumsFromServer()
        }else{
            getAlbumsFromDatabase()
        }
    }

    fun getAlbumsFromServer() {
        Log.v(TAG,"getAlbumsFromServer")
        progress.value = true
        compositeDisposable?.add(
            RetrofitBuilder.apiService.getAlbums()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponseAlbums, this::handleErrorFromServer))
    }

    private fun handleResponseAlbums(albumsList: List<Album>) {
        Log.v(TAG, "handleResponseAlbums")
        saveAlbumsToDatabase(albumsList)
    }

    private fun handleErrorFromServer(throwable: Throwable) {
        Log.v(TAG, "handleErrorFromServer ${throwable.message}")
        getAlbumsFromDatabase()
    }

    private fun saveAlbumsToDatabase(albumsList: List<Album>) {
        Log.v(TAG, "saveAlbumsToDatabase")
        val db = AppDatabase(this.context)

        Completable.fromAction { db.albumsDAO().updateTable(albumsList) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable?.add(d)
                }

                override fun onComplete() {
                    val date = Date(System.currentTimeMillis())
                    prefs.edit().putLong(lastUpdateTimeAlbums, date.getTime()).apply()
                    getAlbumsFromDatabase()
                }

                override fun onError(throwable: Throwable) {
                    Log.v(TAG, "saveAlbumsToDatabase onError ${throwable.message}")
                }
            })
    }

    private fun getAlbumsFromDatabase() {
        Log.v(TAG,"getAlbumsFromDatabase")
        //LiveData takes care of everything but shutting off the progress spinner
        Handler().postDelayed({ progress.value = false }, 100)
    }

    fun clearDisposable() {
        Log.v(TAG,"clearDisposable")
        compositeDisposable?.clear()
    }
}