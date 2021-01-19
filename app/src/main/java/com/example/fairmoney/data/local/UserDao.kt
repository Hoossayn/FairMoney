package com.example.fairmoney.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.fairmoney.data.Model.entities.Data
import com.example.fairmoney.data.Model.entities.User

@Dao
interface UserDao {

    @Query("SELECT * FROM characters")
    fun getAllUsers(): List<Data>?

    @Query("SELECT * FROM characters_detail WHERE id = :id")
    fun getUserDetail(id: String): User

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(users: List<Data>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Delete
    fun deleteAll(movie: List<Data>)

    @Delete
    fun delete(user:User)

}