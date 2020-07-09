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
import com.jacob.portfolio.models.Photo
import com.jacob.portfolio.models.PhotoItem
import com.jacob.portfolio.utilities.CommonUtilities
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class PhotosRepository (application: Application, albumId : Int) {

    private val TAG : String = "PhotosRepository"
    private var compositeDisposable: CompositeDisposable? = null
    private var prefs : SharedPreferences
    val lastUpdateTimePhotos : String = "lastUpdateTimePhotos"
    private var context: Context = application.applicationContext
    private var photos: LiveData<List<PhotoItem>>
    private var progress: MutableLiveData<Boolean>
    private var db: AppDatabase
    private val commonUtilities : CommonUtilities = CommonUtilities()

    init {
        prefs = this.context.getSharedPreferences("com.jacob.portfolio", Context.MODE_PRIVATE)
        db = AppDatabase(this.context)
        compositeDisposable = CompositeDisposable()
        progress = MutableLiveData()
        progress.value = false
        photos = db.photosDAO().getAllItemsByAlbumID(albumId)
        setupPhotos()
    }

    fun getPhotos() : LiveData<List<PhotoItem>>?{
        return photos
    }

    fun getProgress() : LiveData<Boolean>?{
        return progress
    }

    fun setupPhotos() {
        Log.v(TAG,"setupPhotos")
        progress.value = true
        val currentTime = Date(System.currentTimeMillis())
        val savedTime = Date(prefs.getLong(lastUpdateTimePhotos, 0))
        if (commonUtilities.getMinutesDifference(currentTime,savedTime)>=context.resources.getInteger(
                R.integer.time_interval)){
            getPhotosFromServer()
        }else{
            getPhotosFromDatabase()
        }
    }

    fun getPhotosFromServer() {
        Log.v(TAG,"getPhotosFromServer")
        progress.value = true
        compositeDisposable?.add(
            RetrofitBuilder.apiService.getPhotos()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponsePhotos, this::handleErrorFromServer))
    }

    private fun handleResponsePhotos(photosList: List<Photo>) {
        Log.v(TAG, "handleResponsePhotos")
        savePhotosToDatabase(photosList)
    }

    private fun handleErrorFromServer(throwable: Throwable) {
        Log.v(TAG, "handleErrorFromServer ${throwable.message}")
        getPhotosFromDatabase()
    }

    private fun savePhotosToDatabase(photosList: List<Photo>) {
        Log.v(TAG, "savePhotosToDatabase")
        val db = AppDatabase(this.context)

        Completable.fromAction { db.photosDAO().updateTable(photosList) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable?.add(d)
                }

                override fun onComplete() {
                    val date = Date(System.currentTimeMillis())
                    prefs.edit().putLong(lastUpdateTimePhotos, date.getTime()).apply()
                    getPhotosFromDatabase()
                }

                override fun onError(throwable: Throwable) {
                    Log.v(TAG, "savePhotosToDatabase onError ${throwable.message}")
                }
            })
    }

    private fun getPhotosFromDatabase() {
        Log.v(TAG,"getPhotosFromDatabase")
        //LiveData takes care of everything but shutting off the progress spinner
        Handler().postDelayed({ progress.value = false }, 100)
    }

    fun clearDisposable() {
        Log.v(TAG,"clearDisposable")
        compositeDisposable?.clear()
    }
}