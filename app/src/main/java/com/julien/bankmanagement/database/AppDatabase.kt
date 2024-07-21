package com.julien.bankmanagement.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.julien.bankmanagement.database.daos.BankAccountDao
import com.julien.bankmanagement.database.daos.TransfersDao
import com.julien.bankmanagement.database.daos.UsersDao
import com.julien.bankmanagement.database.entities.BankAccountEntity
import com.julien.bankmanagement.database.entities.TransferEntity
import com.julien.bankmanagement.database.entities.UsersEntity

@Database(entities = [UsersEntity::class, BankAccountEntity::class, TransferEntity::class] , version = 4, exportSchema = false)
abstract class AppDatabase: RoomDatabase(){

    abstract fun usersDao(): UsersDao

    abstract fun bankAccountDao(): BankAccountDao

    abstract fun transfersDao(): TransfersDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "bank_management.db"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}