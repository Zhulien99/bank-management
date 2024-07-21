package com.julien.bankmanagement.transfers

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.julien.bankmanagement.app.App
import com.julien.bankmanagement.database.entities.TransferEntity
import kotlinx.coroutines.flow.Flow

class TransfersViewModel(application: Application): AndroidViewModel(application) {

    private val transfersDao = App.instance.database.transfersDao()

    private val _currentAccountID = MutableLiveData(-1)

    val accountID: LiveData<Int> get() = _currentAccountID

    val transfers: LiveData<PagingData<TransferEntity>?> = _currentAccountID.switchMap {
        it?.let { accountID ->
            Pager(PagingConfig(pageSize = 20)) {
                transfersDao.getTransfersForAccount(accountID)
            }.flow.cachedIn(viewModelScope).asLiveData()
        }
    }

    fun updateAccountID(accountID: Int) {
        _currentAccountID.value = accountID
    }



}