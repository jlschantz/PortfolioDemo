package com.jacob.portfolio.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.jacob.portfolio.models.Album
import com.jacob.portfolio.models.AlbumItem
import com.jacob.portfolio.repositories.AlbumsRepository

class AlbumsViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG : String = "AlbumsViewModel"
    private val repository: AlbumsRepository = AlbumsRepository(application)
    private val albums: LiveData<List<AlbumItem>>?
    private val progress: LiveData<Boolean>?

    init {
        albums = repository.getAlbums()
        progress = repository.getProgress()
    }

    fun getAlbums() : LiveData<List<AlbumItem>>?{
        return albums
    }

    fun setupAlbums() {
        repository.setupAlbums()
    }

    fun getAlbumsFromServer(){
        repository.getAlbumsFromServer()
    }

    fun getProgress() : LiveData<Boolean>?{
        return progress
    }

    override fun onCleared(){
        repository.clearDisposable()
    }
}