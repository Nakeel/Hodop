package com.we2dx.hodop.ui.signup

interface AuthListener {
    fun authStarted()
    fun authSuccess()
    fun authFailed(errorMessage:String)
}