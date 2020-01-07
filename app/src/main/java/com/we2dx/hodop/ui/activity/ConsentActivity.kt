package com.we2dx.hodop.ui.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.we2dx.hodop.HomeActivity
import com.we2dx.hodop.R
import com.we2dx.hodop.utils.ApplicationConstants
import com.we2dx.hodop.utils.ApplicationUtility

import kotlinx.android.synthetic.main.activity_request_consent.*

class ConsentActivity : AppCompatActivity() {

    private lateinit var mAgreeTCButton : AppCompatButton
    private lateinit var mAgreeTCCheckbox : AppCompatCheckBox
    private lateinit var mAgreeToRuleCheckbox : AppCompatCheckBox
    private lateinit var mUsernameText: AppCompatTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_consent)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDarker)
        }

        mAgreeTCButton = findViewById(R.id.agree_to_tc_button_proceed)
        mAgreeTCCheckbox = findViewById(R.id.agree_to_tc_checkbox)
        mAgreeToRuleCheckbox = findViewById(R.id.agree_to_post_rule_checkbox)
        mUsernameText = findViewById(R.id.username_greetings)

        val username = intent.getStringExtra(ApplicationConstants.USER_FULL_NAME)
        mUsernameText.text = "Hello$username"


        mAgreeTCButton.setOnClickListener {
            //Todo: To modify argument
            proceedToTrafficActivity("Traffic Info")
        }
    }

    private fun proceedToTrafficActivity(trafficInfo:String) {
        if (mAgreeTCCheckbox.isChecked && mAgreeToRuleCheckbox.isChecked){
            ApplicationUtility.storeBooleanValue(ApplicationConstants.HAS_ACCEPT_TRAFFIC_RULE,true,this)
            ApplicationUtility.storeBooleanValue(ApplicationConstants.HAS_ACCEPT_TC,true,this)
            val intent = Intent(this,HomeActivity::class.java)
            intent.putExtra(ApplicationConstants.TRAFFIC_INFO,trafficInfo)
            startActivity(intent)
        }else{
            Toast.makeText(this,"Please accept terms and conditions to proceed",Toast.LENGTH_LONG).show()
        }
    }
}
