package com.julien.bankmanagement.database.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.julien.bankmanagement.database.entities.BankAccountEntity

@Dao
interface BankAccountDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: BankAccountEntity)

    @Query("SELECT * FROM accounts WHERE user_id = :userID ORDER BY modified_on DESC")
    fun getAccountsForUser(userID: Int): PagingSource<Int, BankAccountEntity>

    @Query("SELECT * FROM accounts WHERE LOWER(name) = LOWER(:name)")
    suspend fun getAccountByName(name: String): BankAccountEntity?

    @Query("SELECT * FROM accounts WHERE account_id = :accountID")
    suspend fun getAccountById(accountID: Int): BankAccountEntity?

    @Query("SELECT * FROM accounts WHERE user_id = :userID AND account_id != :excludeAccountID")
    suspend fun getAccountsForUserExcluding(userID: Int, excludeAccountID: Int): List<BankAccountEntity>

    @Query("SELECT COUNT(*) FROM accounts WHERE name = :name AND account_id != :accountID")
    suspend fun countAccountsByName(name: String, accountID: Int): Int

    @Query("SELECT name FROM accounts WHERE account_id = :accountID")
    suspend fun getAccountNameById(accountID: Int): String?

    @Update
    suspend fun updateAccount(account: BankAccountEntity)

    @Delete
    suspend fun deleteAccount(account: BankAccountEntity)
}