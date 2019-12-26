package com.we2dx.hodop.data


/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class SignUpRepository(val dataSource: SignUpDataSource) {

    fun logout() {
       dataSource.logout()

    }

     fun signup(email:String, password: String) =
        // handle login
         dataSource.signup(email, password)


    fun currentUser() = dataSource.currentUser()


}
