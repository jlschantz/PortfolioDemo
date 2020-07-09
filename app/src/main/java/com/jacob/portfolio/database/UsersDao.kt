package com.jacob.portfolio.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.jacob.portfolio.models.User
import com.jacob.portfolio.models.UserItem

@Dao
interface UsersDao {
    @Query("SELECT * FROM users")
    fun getAllItems(): LiveData<List<UserItem>>

    @Query("SELECT * FROM users WHERE id LIKE :id")
    fun getItemByID(id: Int): LiveData<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItems(users: List<User>)

    @Delete
    fun deleteItem(user: User)

    @Update
    fun updateItem(user: User)

    @Query("DELETE FROM users")
    fun clearTable()

    @Transaction
    fun updateTable(users : List<User>) {
        clearTable()
        insertItems(users)
    }
}