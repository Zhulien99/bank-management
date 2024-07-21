package com.julien.bankmanagement.transfers.dialog.add

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.julien.bankmanagement.app.App
import com.julien.bankmanagement.database.entities.BankAccountEntity
import com.julien.bankmanagement.database.entities.TransferEntity
import com.julien.bankmanagement.models.UIState
import com.julien.bankmanagement.utils.Constants
import kotlinx.coroutines.launch

class AddTransferViewModel(application: Application): AndroidViewModel(application) {

    private val accountDao = App.instance.database.bankAccountDao()
    private val transfersDao = App.instance.database.transfersDao()

    private val _accountList: MutableLiveData<List<BankAccountEntity>> = MutableLiveData()
    val accountList: LiveData<List<BankAccountEntity>> = _accountList

    private val _transferInsertionResult: MutableLiveData<UIState> = MutableLiveData()
    val transferInsertionResult: LiveData<UIState> = _transferInsertionResult


    fun getAccountsForUser(userID: Int,excludeAccountID: Int) {
        viewModelScope.launch {
            try {
                val accounts = accountDao.getAccountsForUserExcluding(userID,excludeAccountID)
                _accountList.postValue(accounts)
            } catch (e: Exception) {
                Log.e("Error", e.printStackTrace().toString())
                _accountList.postValue(emptyList())
            }
        }
    }

    fun getCurrentAccount(accountID: Int,callback:(BankAccountEntity)-> Unit){
        viewModelScope.launch {
            try {
                val account = accountDao.getAccountById(accountID)
                if (account != null) {
                    callback.invoke(account)
                }
            } catch (e: Exception) {
                Log.e("Error", e.printStackTrace().toString())
            }
        }
    }

    fun createTransfer(accountID: Int, beneficiaryAccountID: Int?, amount: Double?) {
        viewModelScope.launch {
            // Begin a transaction
            try {
                // Fetch the account details from the database
                val fromAccount = accountDao.getAccountById(accountID)
                val toAccount = accountDao.getAccountById(beneficiaryAccountID ?: -1)

                // Check if the accounts are valid and amount is not null
                if (fromAccount != null && toAccount != null && amount != null) {
                    val transactionType: String
                    val isSufficientFunds = fromAccount.availableAmount >= amount

                    if (isSufficientFunds) {
                        // Sufficient funds, set transaction type to Debit
                        transactionType = Constants.TRANSACTION_DEBIT

                        // Update the fromAccount
                        fromAccount.availableAmount -= amount
                    } else {
                        // Insufficient funds, set transaction type to Credit
                        transactionType = Constants.TRANSACTION_CREDIT

                        // Update the fromAccount to reflect the negative balance
                        fromAccount.availableAmount -= amount
                    }

                    // Update the toAccount with the transfer amount
                    toAccount.availableAmount += amount

                    // Update the modified times
                    fromAccount.modifiedOn = (System.currentTimeMillis() / 1000L).toString()
                    toAccount.modifiedOn = (System.currentTimeMillis() / 1000L).toString()

                    // Perform account updates
                    accountDao.updateAccount(fromAccount)
                    accountDao.updateAccount(toAccount)

                    // Insert the transfer record
                    val transfer = TransferEntity(
                        accountID = accountID,
                        beneficiaryAccountID = beneficiaryAccountID ?: -1,
                        type = transactionType,
                        amount = amount,
                        createdOn = (System.currentTimeMillis() / 1000L).toString(),
                        modifiedOn = (System.currentTimeMillis() / 1000L).toString()
                    )
                    transfersDao.insertTransfer(transfer)

                    // Optionally, log or handle success
                    Log.d("Transfer", "Transfer completed successfully")
                    _transferInsertionResult.postValue(UIState(success = 1, message = "Transfer completed successfully"))

                } else {
                    // Handle invalid accounts or missing amount
                    _transferInsertionResult.postValue(UIState(success = 0, message = "Invalid account information"))
                }
            } catch (e: Exception) {
                // Handle errors
                Log.e("Transfer Error", e.message ?: "Unknown error")
                _transferInsertionResult.postValue(UIState(success = 0, message = e.message ?: "Unknown error"))
            }
        }
    }


}