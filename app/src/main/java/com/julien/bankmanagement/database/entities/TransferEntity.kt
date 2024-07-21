package com.julien.bankmanagement.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "transfers",
    foreignKeys = [
        ForeignKey(
            entity = BankAccountEntity::class,
            parentColumns = ["account_id"],
            childColumns = ["account_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = BankAccountEntity::class,
            parentColumns = ["account_id"],
            childColumns = ["beneficiary_account_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TransferEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "transaction_id")
    val transactionID: Int = 0,
    @ColumnInfo(name = "account_id")
    val accountID: Int,
    @ColumnInfo(name = "beneficiary_account_id")
    val beneficiaryAccountID: Int,
    @ColumnInfo(name = "type")
    val type: String,
    @ColumnInfo(name = "amount")
    val amount: Double,
    @ColumnInfo(name = "created_on")
    val createdOn: String,
    @ColumnInfo(name = "modified_on")
    val modifiedOn: String
)