package com.we2dx.hodop.ui.signup

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
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.we2dx.hodop.HomeActivity

import com.we2dx.hodop.R
import com.we2dx.hodop.ui.login.LoginActivity

class SignUpActivity : AppCompatActivity(), AuthListener {
    override fun authStarted() {
        loading.visibility = View.VISIBLE
        signupText.visibility= View.GONE
        email.isEnabled = false
        password.isEnabled = false
        username.isEnabled = false
    }

    override fun authSuccess() {
        loading.visibility = View.GONE
        signupText.visibility= View.VISIBLE
    }

    override fun authFailed(errorMessage: String) {
        loading.visibility = View.GONE
        signupText.visibility= View.VISIBLE
        email.isEnabled = true
        password.isEnabled = true
        username.isEnabled = true
    }

    private lateinit var signUpViewModel: SignUpViewModel
    private lateinit var username: TextInputEditText

    private lateinit var password: TextInputEditText
    private lateinit var email: TextInputEditText
    private lateinit var phone: TextInputEditText

    private lateinit var signupText: AppCompatTextView

    private lateinit var existingUserButton: AppCompatTextView
    private lateinit var signupLayout: RelativeLayout
    private lateinit var loading: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_sign_up)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDarker)
        }

         username = findViewById(R.id.sign_up_username)
         password = findViewById(R.id.sign_up_password)
         email = findViewById(R.id.sign_up_email)
        phone = findViewById(R.id.sign_up_phone)
        signupText = findViewById(R.id.sign_up_button_text)
        signupLayout = findViewById(R.id.signup_layout)
        existingUserButton = findViewById(R.id.existing_user_button)

         loading = findViewById(R.id.loading_sign_up)

        existingUserButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        signUpViewModel = ViewModelProviders.of(this, SignUpViewModelFactory())
            .get(SignUpViewModel::class.java)

        signUpViewModel.authListener = this

        signUpViewModel.signUpFormState.observe(this, Observer {
            val signUpState = it ?: return@Observer

            // disable signUp button unless both username / password is valid
            signupLayout.isEnabled = signUpState.isDataValid

            if (signUpState.usernameError != null) {
                username.error = getString(signUpState.usernameError)
            }
            if (signUpState.emailError != null) {
                email.error = getString(signUpState.emailError)
            }
            if (signUpState.passwordError != null) {
                password.error = getString(signUpState.passwordError)
            }
        })

        signUpViewModel.signUpResult.observe(this, Observer {
            val signUpResult = it ?: return@Observer

            if (signUpResult.error != null) {
                showsignUpFailed(signUpResult.error)
            }

            if (signUpResult.success != null) {
                signInUser(signUpResult.success)
            }
            setResult(Activity.RESULT_OK)


        })

        username.afterTextChanged {
            signUpViewModel.signUpDataChanged(
                username.text.toString(),
                password.text.toString(),
                phone.text.toString(),
                email.text.toString()
            )
        }
        phone.afterTextChanged {
            signUpViewModel.signUpDataChanged(
                username.text.toString(),
                password.text.toString(),
                phone.text.toString(),
                email.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                signUpViewModel.signUpDataChanged(
                    username.text.toString(),
                    password.text.toString(),
                    phone.text.toString(),
                    email.text.toString()
                )
            }

             setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        signUpViewModel.signup(
                            email.text.toString(),
                            password.text.toString()

                        )
                }
                false
            }

            signupLayout.setOnClickListener {
                signUpViewModel.signup(email.text.toString(),password.text.toString())
            }
        }
    }

    private fun signInUser(model: SignUpInUserView) {
        loading.visibility = View.VISIBLE
        signupText.visibility= View.GONE
        FirebaseAuth.getInstance().signInWithEmailAndPassword(model.email, model.password)
            .addOnCompleteListener {
                loading.visibility = View.GONE
                signupText.visibility= View.VISIBLE
            }
            .addOnSuccessListener {
                startActivity(Intent(this, HomeActivity::class.java)) //Complete and destroy signUp activity once successful
                finish()
            }.addOnFailureListener {
                Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
            }
    }

    private fun showsignUpFailed(@StringRes errorString: Int) {
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
