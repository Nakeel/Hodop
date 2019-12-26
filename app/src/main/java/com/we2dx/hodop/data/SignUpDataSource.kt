package com.we2dx.hodop.data

import com.we2dx.hodop.firebase.FirebaseServices

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class SignUpDataSource (val firebaseServices: FirebaseServices){


     fun signup(email:String, password: String)
         = firebaseServices.register(email,password)


    fun logout() {
        // TODO: revoke authentication
    }

    fun currentUser() = firebaseServices.currentUser()

}




