package com.julien.bankmanagement.details

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.julien.bankmanagement.app.App
import com.julien.bankmanagement.database.entities.BankAccountEntity
import com.julien.bankmanagement.models.UIState
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AccountDetailsViewModel(application: Application): AndroidViewModel(application) {

    private val accountDao = App.instance.database.bankAccountDao()

    private val _viewState = MutableLiveData<BankAccountEntity?>()
    val viewState: LiveData<BankAccountEntity?> = _viewState

    private val _accountUpdateResult: MutableLiveData<UIState> = MutableLiveData()
    val accountUpdateResult: LiveData<UIState> = _accountUpdateResult


    fun getCurrentAccount(accountID: Int){
        viewModelScope.launch {
            try {
                val account = accountDao.getAccountById(accountID)
                _viewState.postValue(account)
            } catch (e: Exception) {
                Log.e("Error", e.printStackTrace().toString())
            }
        }
    }


    private fun updateAccount(account: BankAccountEntity): Boolean {
        return try {
            viewModelScope.launch { accountDao.updateAccount(account) }
            _accountUpdateResult.postValue(UIState(success = 1,"Account successfully updated"))
            true
        } catch (e: Exception) {
            _accountUpdateResult.postValue(UIState(success = 0,e.message))
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

        // Check for duplicate account name before updating
        return runBlocking {
            val count = accountDao.countAccountsByName(name, accountID)
            if (count > 0) {
                _accountUpdateResult.postValue(UIState(success = 0, message = "Account name already exists"))
                false
            } else {
                updateAccount(updatedAccount)
            }
        }
    }


}