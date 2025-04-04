package com.example.enursery.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.enursery.core.domain.model.User
import com.example.enursery.core.domain.usecase.user.SessionUseCase
import com.example.enursery.core.domain.usecase.user.UserUseCase

class ProfileViewModel(
    private val sessionUseCase: SessionUseCase,
    userUseCase: UserUseCase
) : ViewModel() {
     private val userId = sessionUseCase.getUserId()

     val currentUser: LiveData<User>? = userId?.let {
        userUseCase.getUserById(it)
    }

    fun logout() {
        sessionUseCase.logout()
    }
}