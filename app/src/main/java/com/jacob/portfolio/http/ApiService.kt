package com.jacob.portfolio.http

import com.jacob.portfolio.models.Album
import com.jacob.portfolio.models.Photo
import com.jacob.portfolio.models.User
import io.reactivex.Observable
import retrofit2.http.GET

interface ApiService {

    @GET("photos")
    fun getPhotos() : Observable<List<Photo>>
    @GET("albums")
    fun getAlbums() : Observable<List<Album>>
    @GET("users")
    fun getUsers() : Observable<List<User>>
}