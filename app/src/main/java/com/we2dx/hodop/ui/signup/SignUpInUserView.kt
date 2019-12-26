package com.we2dx.hodop.ui.signup

/**
 * User details post authentication that is exposed to the UI
 */
data class SignUpInUserView(
    val email: String,
    val password : String,
    val userUID: String
    //... other data fields that may be accessible to the UI
)
