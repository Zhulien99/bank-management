package com.julien.bankmanagement.home.dialog

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.julien.bankmanagement.app.App
import com.julien.bankmanagement.database.entities.BankAccountEntity
import com.julien.bankmanagement.models.UIState
import com.julien.bankmanagement.utils.Constants
import com.julien.bankmanagement.utils.PreferenceUtils
import com.julien.bankmanagement.utils.Utils
import kotlinx.coroutines.launch

class AddAccountViewModel(application: Application): AndroidViewModel(application) {

    private val accountDao = App.instance.database.bankAccountDao()

    private val _accountInsertionResult: MutableLiveData<UIState> = MutableLiveData()
    val accountInsertionResult: LiveData<UIState> = _accountInsertionResult


    fun insertBankAccount(name: String, availableAmount: Double) {
        viewModelScope.launch {
            try {
                val existingAccount = accountDao.getAccountByName(name)
                if (existingAccount != null) {
                    // Account with the same name already exists
                    _accountInsertionResult.postValue(UIState(success = 0, message = "This name is reserved"))
                } else {
                    val newAccount = PreferenceUtils.getUser(getApplication())?.userID?.let {
                        BankAccountEntity(
                            userID = it,
                            name = name,
                            iBAN = Utils.generateUniqueId(),
                            status = Constants.STATUS_ACTIVE,
                            availableAmount = availableAmount,
                            createdOn = (System.currentTimeMillis() / 1000L).toString(),
                            modifiedOn = (System.currentTimeMillis() / 1000L).toString()
                        )
                    }
                    if (newAccount != null) {
                        accountDao.insertAccount(newAccount)
                        _accountInsertionResult.postValue(UIState(success = 1, message = "Account created"))
                    }
                }
            } catch (e: Exception) {
                Log.e("Error", e.printStackTrace().toString())
                _accountInsertionResult.postValue(UIState(success = 0, message = e.printStackTrace().toString()))
            }
        }
    }



}