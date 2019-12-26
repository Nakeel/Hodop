package com.we2dx.hodop.ui.activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.core.content.ContextCompat
import com.we2dx.hodop.HomeActivity
import com.we2dx.hodop.R
import com.we2dx.hodop.activity.StarterActivity
import com.we2dx.hodop.firebase.FirebaseServices
import com.we2dx.hodop.ui.login.LoginActivity
import com.we2dx.hodop.utils.ApplicationConstants
import com.we2dx.hodop.utils.ApplicationUtility
import com.we2dx.hodop.utils.NetworkUtil
import com.we2dx.hodop.utils.TaskOnComplete

class SplashScreenActivity : AppCompatActivity(), TaskOnComplete {
    override fun onResponseReceived(response: String) {
        if (response == ApplicationConstants.SIGN_IN_SUCCESS){
            startHomeActivity()
        }else{
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
        }
    }



    private fun startHomeActivity(){
        val intent = Intent(this, HomeActivity::class.java)

        ApplicationUtility.storeBooleanValue(ApplicationConstants.HAS_LOGIN,true,this@SplashScreenActivity)
        startActivity(intent)

    }

    private val firebaseServices = FirebaseServices()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        //getting the window and making it to use the full screen
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        }

        splashHandler()
    }

    private fun splashHandler(){
        Handler().postDelayed({
            hasRun()
        }, ApplicationConstants.SPLASHSCREEN_TIME)
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
    /**this method check if the user stayed signed in
     * and returns a boolean result */
    private fun isLoggedIn(): Boolean {
        return ApplicationUtility.readBooleanValue(this,ApplicationConstants.HAS_LOGIN)
    }

    /**this method checks if the app has run before
     * other functions */
    private fun hasRun() {
        //see if it's run before, default no
        val hasRun = ApplicationUtility.readBooleanValue(this, ApplicationConstants.HAS_RUN)
        //checks if the app hasnt run
        if (!hasRun) {

            //set to has run
            ApplicationUtility.storeBooleanValue(ApplicationConstants.HAS_RUN,true,this)


            //starts the wizard if this is the first time the app has run
            startActivity(
                Intent(
                    applicationContext,
                    StarterActivity::class.java
                )
            )
        } else {
            //code if the app HAS run before
            //get the userCredentials

            val mPassword = ApplicationUtility.readValue(this,ApplicationConstants.PASSWORD)
            val mUsername = ApplicationUtility.readValue(this,ApplicationConstants.EMAIL)




            //checks if the user stayed logged in
            if (isLoggedIn()) {
                //if so then checks if the network is available an)d good
                if (NetworkUtil.isConnected(this)) {
                    //then perform the parse Login function
//                        firebaseServices.signInWithEmail(mPassword,mUsername,this)
                } else {
                    //if no network go to
                    startActivity(Intent(applicationContext, LoginActivity::class.java))

                    //set to has run
                    ApplicationUtility.storeBooleanValue(ApplicationConstants.HAS_LOGIN,true,this)
                }
            } else {
                //if not logged in, then go to where to login
                val intent = Intent(applicationContext, StarterActivity::class.java)
                startActivity(intent)
            }

        }
    }
}
