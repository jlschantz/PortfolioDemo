package com.jacob.portfolio.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.jacob.portfolio.models.Photo
import com.jacob.portfolio.models.PhotoItem
import com.jacob.portfolio.repositories.PhotosRepository

class PhotosViewModel(application: Application, albumId : Int) : AndroidViewModel(application) {

    private val TAG : String = "PhotosViewModel"
    private val repository: PhotosRepository = PhotosRepository(application,albumId)
    private val photos: LiveData<List<PhotoItem>>?
    private val progress: LiveData<Boolean>?

    init {
        photos = repository.getPhotos()
        progress = repository.getProgress()
    }

    fun getPhotos() : LiveData<List<PhotoItem>>?{
        return photos
    }

    fun setupPhotos() {
        repository.setupPhotos()
    }

    fun getPhotosFromServer(){
        repository.getPhotosFromServer()
    }

    fun getProgress() : LiveData<Boolean>?{
        return progress
    }

    override fun onCleared(){
        repository.clearDisposable()
    }
}