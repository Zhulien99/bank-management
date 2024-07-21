package com.julien.bankmanagement.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.julien.bankmanagement.app.App
import com.julien.bankmanagement.database.entities.UsersEntity
import kotlinx.coroutines.launch

class LoginViewModel(application: Application): AndroidViewModel(application) {

    private val usersDao = App.instance.database.usersDao()

    private val _viewLogin: MutableLiveData<UsersEntity?> = MutableLiveData()
    val viewLogin: LiveData<UsersEntity?> = _viewLogin

    fun login(username: String, password: String) {
        viewModelScope.launch {
            usersDao.login(username, password)
                .collect { user ->
                    _viewLogin.postValue(user)
                }
        }
    }

    fun register(username: String, password: String, email: String, birthDay: String) {
        viewModelScope.launch {
            try {
                // Check if the user already exists
                val newUser = UsersEntity(
                    username = username,
                    password = password,
                    email = email,
                    birthDay = birthDay
                )
                val newUserId = usersDao.insertUser(newUser).toInt()
                _viewLogin.postValue(newUser.copy(userID = newUserId))
            } catch (e: Exception) {
                _viewLogin.postValue(null)
            }
        }
    }
}
