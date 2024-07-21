package com.julien.bankmanagement.database.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.julien.bankmanagement.database.entities.UsersEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UsersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(usersEntity: UsersEntity): Long

    @Query("SELECT * FROM users WHERE user_id = :id")
    fun getUser(id: Int): LiveData<UsersEntity>

    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    fun login(username: String, password: String): Flow<UsersEntity?>

}