package com.jacob.portfolio.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.jacob.portfolio.models.Album
import com.jacob.portfolio.models.Photo
import com.jacob.portfolio.models.User

@Database(
    entities = [Album::class, Photo::class, User::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase(){
    abstract fun albumsDAO(): AlbumsDao
    abstract fun photosDAO(): PhotosDao
    abstract fun usersDAO(): UsersDao

    companion object {
        @Volatile private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context)= instance
            ?: synchronized(LOCK){
            instance
                ?: buildDatabase(
                    context
                ).also { instance = it}
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
            AppDatabase::class.java, "database.db")
            .build()
    }
}