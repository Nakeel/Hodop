package com.we2dx.hodop.ui.signup

/**
 * Data validation state of the login form.
 */
data class SignUpFormState(
    val usernameError: Int? = null,
    val passwordError: Int? = null,
    val emailError: Int? = null,
    val phoneError: Int? = null,
    val isDataValid: Boolean = false
)
