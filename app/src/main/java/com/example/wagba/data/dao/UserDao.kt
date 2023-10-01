package com.example.wagba.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.wagba.data.internal.UserEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: UserEntity): Long

    @Query("DELETE FROM users")
    suspend fun clearUsers()

    @Query("SELECT * FROM users ORDER BY id ASC")
    fun readAllData(): LiveData<List<UserEntity>>

    @Query("SELECT * FROM users WHERE id = :myId")
    fun getUser(myId: String): UserEntity
}