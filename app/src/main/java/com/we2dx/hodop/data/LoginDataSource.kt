package com.we2dx.hodop.data

import com.we2dx.hodop.firebase.FirebaseServices


/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource (val firebaseServices: FirebaseServices){

    fun login(email: String, password: String)  = firebaseServices.login(email,password)

    fun logout() {
        // TODO: revoke authentication
    }

    fun currentUser() = firebaseServices.currentUser()

}

