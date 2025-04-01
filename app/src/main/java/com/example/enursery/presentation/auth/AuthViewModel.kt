package com.example.enursery.presentation.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.enursery.core.data.source.local.entity.RoleEntity
import com.example.enursery.core.data.source.local.entity.WilayahKerjaEntity
import com.example.enursery.core.domain.model.User
import com.example.enursery.core.domain.usecase.LoginUseCase
import com.example.enursery.core.domain.usecase.SessionUseCase
import com.example.enursery.core.domain.usecase.UserUseCase
import kotlinx.coroutines.launch

class AuthViewModel(
    private val userUseCase: UserUseCase,
    private val loginUseCase: LoginUseCase,
    private val sessionUseCase: SessionUseCase
) : ViewModel() {

    private val _loginResult = MutableLiveData<Result<User>>()
    val loginResult: LiveData<Result<User>> = _loginResult

    private val _registerResult = MutableLiveData<Result<Unit>>()
    val registerResult: LiveData<Result<Unit>> = _registerResult

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    val roleList: LiveData<List<RoleEntity>> = userUseCase.getAllRoles()
    val wilayahList: LiveData<List<WilayahKerjaEntity>> = userUseCase.getAllWilayah()

    var currentRoles: List<RoleEntity> = emptyList()
    var currentWilayah: List<WilayahKerjaEntity> = emptyList()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = loginUseCase.loginUser(email, password)
            _loginResult.value = result

            if (result.isSuccess) {
                result.getOrNull()?.let { user ->
                    sessionUseCase.saveSession(user.idUser, user.namaUser, user.role)
                }
            }

            _isLoading.value = false
        }
    }

    fun onRegisterClicked(
        name: String,
        email: String,
        password: String,
        role: String,
        wilayah: String,
        fotoUri: String?
    ) {
        viewModelScope.launch {
            val id = userUseCase.generateUserId()
            val user = User(
                idUser = id,
                namaUser = name,
                role = role,
                wilayahKerja = wilayah,
                foto = fotoUri.toString(),
                email = email,
                password = password
            )
            _registerResult.value = userUseCase.registerUser(user)
        }
    }

    fun isLoggedIn(): Boolean {
        return sessionUseCase.isLoggedIn()
    }
}
