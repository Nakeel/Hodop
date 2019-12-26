package com.we2dx.hodop.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import com.we2dx.hodop.R
import com.we2dx.hodop.data.LoginRepository
import com.we2dx.hodop.ui.signup.AuthListener
import com.we2dx.hodop.ui.signup.SignUpInUserView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult
    //auth listener
    var authListener: AuthListener? = null


    //disposable to dispose the Completable
    private val disposables = CompositeDisposable()

    val user by lazy {
        loginRepository.currentUser()
    }

    //Doing same thing with signup
    fun login(email:String, password: String) {
        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            authListener?.authFailed("Please input all values")
            return
        }
        authListener?.authStarted()
        val disposable = loginRepository.login(email, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                authListener?.authSuccess()
                _loginResult.value =
                    LoginResult(success = SignUpInUserView( userUID = loginRepository.currentUser()!!.uid, email = email,password = password))
            }, {
                authListener?.authFailed(it.message!!)
                _loginResult.value = LoginResult(error = R.string.login_failed)
            })
        disposables.add(disposable)
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}
