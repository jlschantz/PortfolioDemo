package com.jacob.portfolio.repositories

import androidx.lifecycle.LiveData
import com.jacob.portfolio.http.RetrofitBuilder
import com.jacob.portfolio.models.Photo
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

object MainRepository {
    /*
    var job: CompletableJob? = null

    fun getPhotos(): LiveData<List<Photo>>{
        job = Job()
        return object: LiveData<List<Photo>>(){
            override fun onActive() {
                super.onActive()
                job?.let{ theJob ->
                    CoroutineScope(IO + theJob).launch {
                        val photos = RetrofitBuilder.apiService.getPhotos()
                        withContext(Main){
                            value = photos
                            theJob.complete()
                        }
                    }

                }

            }
        }
    }

    fun cancelJobs(){
        job?.cancel()
    }

     */
}