package com.julien.bankmanagement.home

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.julien.bankmanagement.app.App
import com.julien.bankmanagement.database.entities.BankAccountEntity
import com.julien.bankmanagement.utils.PreferenceUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class HomeViewModel(application: Application): AndroidViewModel(application) {

    private val accountDao = App.instance.database.bankAccountDao()

    val accounts: Flow<PagingData<BankAccountEntity>?> = Pager(PagingConfig(pageSize = 20, enablePlaceholders = false)) {
        PreferenceUtils.getUser(getApplication())?.userID?.let { accountDao.getAccountsForUser(it) }!!
    }.flow.cachedIn(viewModelScope)


    fun deleteAccount(accountEntity: BankAccountEntity){
        viewModelScope.launch {
            accountDao.deleteAccount(accountEntity)
        }
    }

    private fun updateAccount(account: BankAccountEntity): Boolean {
        return try {
            viewModelScope.launch { accountDao.updateAccount(account) }
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun getUpdatedAccountEntry(  accountID: Int,
                                         userID: Int,
                                         name: String,
                                         iBAN: String,
                                         status: String,
                                         availableAmount: Double,
                                         createdOn: String,
                                         modifiedOn: String?): BankAccountEntity {
        return BankAccountEntity( accountID = accountID,
            userID = userID,
            name = name,
            iBAN = iBAN,
            status = status,
            availableAmount = availableAmount,
            createdOn = createdOn,
            modifiedOn = modifiedOn
        )
    }

    fun updateAccount(
        accountID: Int,
        userID: Int,
        name: String,
        iBAN: String,
        status: String,
        availableAmount: Double,
        createdOn: String,
        modifiedOn: String?
    ): Boolean {
        val updatedAccount = getUpdatedAccountEntry(
            accountID = accountID,
            userID = userID,
            name = name,
            iBAN = iBAN,
            status = status,
            availableAmount = availableAmount,
            createdOn = createdOn,
            modifiedOn = modifiedOn
        )

        return updateAccount(updatedAccount)
    }


}