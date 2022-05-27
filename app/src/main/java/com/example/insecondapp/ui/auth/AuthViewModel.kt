package com.example.insecondapp.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.insecondapp.models.User
import com.example.insecondapp.repos.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AuthViewModel
@Inject
constructor(private val sessionManager: SessionManager )
    :ViewModel() {


    val _userDetails = MutableLiveData<HashMap<String,String?>>()
    val userDetails = _userDetails
     val isLogin = sessionManager.isLogin()

    init {
        getUserDetails()
    }

    fun createSeesion(user: User)= viewModelScope.launch(Dispatchers.IO)
    {
        sessionManager.createSeesion(user)
    }
    fun getUserDetails() = viewModelScope.launch(Dispatchers.IO){
        val user = sessionManager.getUserDetails()
        withContext(Dispatchers.Main){ _userDetails.value = user}

    }
    fun logoutUserFromSeesion(){ viewModelScope.launch(Dispatchers.IO) {
        sessionManager.logoutUserFromSeesion()
    }

    }
}


