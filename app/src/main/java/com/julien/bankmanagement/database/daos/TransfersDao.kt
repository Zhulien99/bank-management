package com.julien.bankmanagement.database.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.julien.bankmanagement.database.entities.BankAccountEntity
import com.julien.bankmanagement.database.entities.TransferEntity

@Dao
interface TransfersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransfer(transfer: TransferEntity)

    @Query("SELECT * FROM transfers WHERE account_id = :accountID ORDER BY modified_on DESC")
    fun getTransfersForAccount(accountID: Int): PagingSource<Int, TransferEntity>

    @Delete
    suspend fun deleteTransfer(transfer: TransferEntity)
}