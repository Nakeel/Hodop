package com.we2dx.hodop.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import com.we2dx.hodop.data.SignUpRepository

import com.we2dx.hodop.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SignUpViewModel(private val signUpRepository: SignUpRepository) : ViewModel() {

    private val _signUpForm = MutableLiveData<SignUpFormState>()
    val signUpFormState: LiveData<SignUpFormState> = _signUpForm

    private val _signUpResult = MutableLiveData<SignUpResult>()
    val signUpResult: LiveData<SignUpResult> = _signUpResult


    //auth listener
    var authListener: AuthListener? = null


    //disposable to dispose the Completable
    private val disposables = CompositeDisposable()

    val user by lazy {
        signUpRepository.currentUser()
    }

    //Doing same thing with signup
    fun signup(email:String, password: String) {
        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            authListener?.authFailed("Please input all values")
            return
        }
        authListener?.authStarted()
        val disposable = signUpRepository.signup(email, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                authListener?.authSuccess()
                _signUpResult.value =
                    SignUpResult(success = SignUpInUserView( userUID = signUpRepository.currentUser()!!.uid, email = email,password = password))
            }, {
                authListener?.authFailed(it.message!!)
                _signUpResult.value = SignUpResult(error = R.string.login_failed)
            })
        disposables.add(disposable)
    }

    fun signUpDataChanged(username: String, password: String,phone: String, email: String) {
        if (!isUserNameValid(username)) {
            _signUpForm.value = SignUpFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _signUpForm.value = SignUpFormState(passwordError = R.string.invalid_password)
        } else if (!isEmailValid(email)) {
        _signUpForm.value = SignUpFormState(emailError = R.string.invalid_username)
        }else if (!isPhoneValid(phone)) {
            _signUpForm.value = SignUpFormState(phoneError = R.string.invalid_phone_no)
        }else {
            _signUpForm.value = SignUpFormState(isDataValid = true)
        }
    }

    // A placeholder email validation check
    private fun isEmailValid(email: String): Boolean {
        return if (email.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        } else {
            email.isNotBlank()
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return  username.isNotBlank()
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
    private fun isPhoneValid(password: String): Boolean {
        return password.length > 10
    }

    //disposing the disposables
    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}
