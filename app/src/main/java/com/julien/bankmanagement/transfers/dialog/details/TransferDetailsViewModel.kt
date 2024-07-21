package com.julien.bankmanagement.transfers.dialog.details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.julien.bankmanagement.app.App
import kotlinx.coroutines.launch

class TransferDetailsViewModel(application: Application): AndroidViewModel(application) {

    private val accountDao = App.instance.database.bankAccountDao()

    fun getAccountName(accountID: Int,callBack: (String?) -> Unit){
        viewModelScope.launch {
            callBack.invoke(accountDao.getAccountNameById(accountID))
        }

    }

}