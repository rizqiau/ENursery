package com.example.enursery.presentation.auth

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.enursery.core.data.source.local.entity.RoleEntity
import com.example.enursery.core.data.source.local.entity.WilayahKerjaEntity
import com.example.enursery.core.domain.model.User
import com.example.enursery.core.domain.usecase.UserUseCase
import kotlinx.coroutines.launch

class AuthViewModel(private val userUseCase: UserUseCase) : ViewModel() {

    private val _loginResult = MutableLiveData<Result<User>>()
    val loginResult: LiveData<Result<User>> = _loginResult

    private val _registerResult = MutableLiveData<Result<Unit>>()
    val registerResult: LiveData<Result<Unit>> = _registerResult

    val roleList: LiveData<List<RoleEntity>> = userUseCase.getAllRoles()
    val wilayahList: LiveData<List<WilayahKerjaEntity>> = userUseCase.getAllWilayah()

    var currentRoles: List<RoleEntity> = emptyList()
    var currentWilayah: List<WilayahKerjaEntity> = emptyList()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginResult.value = userUseCase.loginUser(email, password)
        }
    }

    fun onRegisterClicked(
        name: String,
        email: String,
        password: String,
        role: String,
        wilayah: String,
        fotoUri: Uri?
    ) {
        viewModelScope.launch {
            val id = userUseCase.generateUserId()
            val user = User(
                id = id,
                nama = name,
                role = role,
                wilayahKerja = wilayah,
                foto = fotoUri.toString(),
                email = email,
                password = password
            )
            _registerResult.value = userUseCase.registerUser(user)
        }
    }
}
