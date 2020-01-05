package com.we2dx.hodop.ui.activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.we2dx.hodop.HomeActivity
import com.we2dx.hodop.R
import com.we2dx.hodop.ui.login.LoginActivity
import com.we2dx.hodop.ui.signup.SignUpActivity
import com.we2dx.hodop.utils.ApplicationConstants
import com.we2dx.hodop.utils.ApplicationUtility
import kotlinx.android.synthetic.main.activity_starter.*

class StarterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_starter)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDarker)
        }
        val mHasTakenPeek = ApplicationUtility.readBooleanValue(this,ApplicationConstants.HAS_TAKEN_A_PEEK)

        if (mHasTakenPeek){
            take_a_tour_button.text = getString(R.string.log)
        }
        take_a_tour_button.setOnClickListener {
            if (mHasTakenPeek){
                openLoginUpActivity()
            }else{
                startAppAsGuest()
            }
        }
        get_started_button.setOnClickListener {
            openSignUpActivity()
        }
    }

    private fun startAppAsGuest() {
        //Todo: login as anonymous
        val intent  = Intent(this, HomeActivity::class.java)
        intent.putExtra(ApplicationConstants.HAS_LOGIN,false)
        startActivity(intent)
        ApplicationUtility.storeBooleanValue(ApplicationConstants.HAS_TAKEN_A_PEEK,true,this)
    }

    private fun openSignUpActivity() {
        startActivity(Intent(this, SignUpActivity::class.java))

    }
    private fun openLoginUpActivity() {
        startActivity(Intent(this, LoginActivity::class.java))

    }


}
