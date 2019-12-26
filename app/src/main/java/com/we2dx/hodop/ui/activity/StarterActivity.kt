package com.we2dx.hodop.activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.we2dx.hodop.HomeActivity
import com.we2dx.hodop.R
import com.we2dx.hodop.ui.signup.SignUpActivity
import kotlinx.android.synthetic.main.activity_starter.*

class StarterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_starter)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)
        }
        take_a_tour_button.setOnClickListener {
            startAppAsGuest()
        }
        get_started_button.setOnClickListener {
            openSignUpActivity()
        }
    }

    private fun startAppAsGuest() {
        //Todo: login as anonymous
        startActivity(Intent(this, HomeActivity::class.java))
    }

    private fun openSignUpActivity() {
        startActivity(Intent(this, SignUpActivity::class.java))

    }


}
