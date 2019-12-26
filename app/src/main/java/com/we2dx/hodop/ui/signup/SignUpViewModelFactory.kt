package com.we2dx.hodop.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.we2dx.hodop.data.SignUpDataSource
import com.we2dx.hodop.data.SignUpRepository
import com.we2dx.hodop.firebase.FirebaseServices

/**
 * ViewModel provider factory to instantiate SignUpViewModel.
 * Required given SignUpViewModel has a non-empty constructor
 */
class SignUpViewModelFactory : ViewModelProvider.Factory {

    private val firebaseServices: FirebaseServices by lazy {
        FirebaseServices()
    }
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            return SignUpViewModel(
                signUpRepository = SignUpRepository(
                    dataSource = SignUpDataSource(firebaseServices)
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
