package com.we2dx.hodop.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.we2dx.hodop.data.LoginDataSource
import com.we2dx.hodop.data.LoginRepository
import com.we2dx.hodop.firebase.FirebaseServices

/**
 * ViewModel provider factory to instantiate SignUpViewModel.
 * Required given SignUpViewModel has a non-empty constructor
 */
class LoginViewModelFactory : ViewModelProvider.Factory {
    private val firebaseServices: FirebaseServices by lazy {
        FirebaseServices()
    }
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(
                loginRepository = LoginRepository(
                    dataSource = LoginDataSource(firebaseServices)
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
