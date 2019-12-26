package com.we2dx.hodop.ui.login

import com.we2dx.hodop.ui.signup.SignUpInUserView

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
    val success: SignUpInUserView? = null,
    val error: Int? = null
)
