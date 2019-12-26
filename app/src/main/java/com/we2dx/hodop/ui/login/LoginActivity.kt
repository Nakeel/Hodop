package com.we2dx.hodop.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.we2dx.hodop.HomeActivity
import com.we2dx.hodop.R
import com.we2dx.hodop.ui.signup.AuthListener
import com.we2dx.hodop.ui.signup.SignUpActivity
import com.we2dx.hodop.ui.signup.SignUpInUserView
import com.we2dx.hodop.utils.ApplicationConstants
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity(), AuthListener {
    override fun authStarted() {
        loading.visibility = View.VISIBLE
        loginText.visibility = View.GONE
        email.isEnabled = false
        password.isEnabled = false
    }

    override fun authSuccess() {
        loading.visibility = View.GONE
        loginText.visibility = View.VISIBLE
        email.isEnabled = true
        password.isEnabled = true
    }

    override fun authFailed(errorMessage: String) {
        loading.visibility = View.GONE
        loginText.visibility = View.VISIBLE
        email.isEnabled = true
        password.isEnabled = true
    }

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var loading : ProgressBar
    private lateinit var loginText : AppCompatTextView
    private lateinit var email : TextInputEditText
    private lateinit var password : TextInputEditText
    private lateinit var loginLayout : RelativeLayout



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)
        }

         email = findViewById(R.id.email)
         password = findViewById(R.id.password)
         loginText = findViewById(R.id.sign_in_button_text)
         loginLayout = findViewById(R.id.login_layout)

         loading = findViewById(R.id.loading_sign_in)

        new_user_button.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        loginViewModel = ViewModelProviders.of(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        loginViewModel.authListener = this

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            loginLayout.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                email.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })


        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)
            }
            setResult(Activity.RESULT_OK)

            //Complete and destroy login activity once successful
            finish()
        })

        email.afterTextChanged {
            loginViewModel.loginDataChanged(
                email.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    email.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            email.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }

            loginLayout.setOnClickListener {
                loginViewModel.login(email.text.toString(), password.text.toString())
            }
        }
    }

    private fun updateUiWithUser(model: SignUpInUserView) {
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra(ApplicationConstants.USER_UID,model.userUID)
        startActivity(intent)
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}
