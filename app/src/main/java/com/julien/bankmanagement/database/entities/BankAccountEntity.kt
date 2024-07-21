package com.julien.bankmanagement.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "accounts",
    foreignKeys = [ForeignKey(
        entity = UsersEntity::class,
        parentColumns = ["user_id"],
        childColumns = ["user_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class BankAccountEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "account_id")
    val accountID: Int = 0,
    @ColumnInfo(name = "user_id")
    val userID: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "iban")
    val iBAN: String,
    @ColumnInfo(name = "status")
    var status: String,
    @ColumnInfo(name = "available_amount")
    var availableAmount: Double,
    @ColumnInfo(name = "created_on")
    val createdOn: String,
    @ColumnInfo(name = "modified_on")
    var modifiedOn: String?
){
    override fun toString(): String {
        return "$name ($$availableAmount)" // Return the property you want to display
    }
}