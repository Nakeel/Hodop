package com.we2dx.hodop.ui.signup

/**
 * Authentication result : success (user details) or error message.
 */
data class SignUpResult(
    val success: SignUpInUserView? = null,
    val error: Int? = null
)
