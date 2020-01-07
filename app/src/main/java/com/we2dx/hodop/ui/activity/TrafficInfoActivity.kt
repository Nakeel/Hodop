package com.we2dx.hodop.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.we2dx.hodop.HomeActivity
import com.we2dx.hodop.R
import com.we2dx.hodop.utils.ApplicationConstants
import com.we2dx.hodop.utils.ApplicationUtility
import com.we2dx.hodop.utils.RevealCircleAnimatorHelper

class TrafficInfoActivity : AppCompatActivity() {

    private lateinit var mShareTrafficInfoCard : CardView
    private lateinit var mAlternativeRoute : CardView
    private lateinit var mPostOnHodopCard : CardView
    private lateinit var mTrafficReportCard : CardView

    private lateinit var mFromDestination : AppCompatTextView
    private lateinit var mToDestination : AppCompatTextView
    private lateinit var mTrafficInfoText : AppCompatTextView

    companion object {
        fun newIntent(context: Context, sourceView: View? = null): Intent {
            return Intent(context, TrafficInfoActivity::class.java).also {
                RevealCircleAnimatorHelper.addBundleValues(it, sourceView)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_traffic_info)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDarker)
        }
        val rootView = findViewById<View>(R.id.traffic_info_root)
        RevealCircleAnimatorHelper
            .create(this)
            .start(rootView,ContextCompat.getColor(this,R.color.colorPrimaryDark),ContextCompat.getColor(this,R.color.colorPrimaryDarker))

        mShareTrafficInfoCard = findViewById(R.id.share_traffic_report_card)
        mAlternativeRoute = findViewById(R.id.alter_route_card)
        mPostOnHodopCard = findViewById(R.id.post_on_hodop_card)
        mTrafficReportCard = findViewById(R.id.traffic_report_card)

        mFromDestination = findViewById(R.id.from_loctaion_text)
        mToDestination = findViewById(R.id.to_loctaion_text)
        mTrafficInfoText = findViewById(R.id.traffic_report_text)

        mShareTrafficInfoCard.setOnClickListener { shareRouteTrafficInfo("Traffic") }

        //Todo:To modify arguments
        mPostOnHodopCard.setOnClickListener { shareOnHodop("Traffic","Uthman") }


    }

    private fun shareOnHodop(trafficInfo: String,username :String) {
        val mAgreeToTC = ApplicationUtility.readBooleanValue(this, ApplicationConstants.HAS_ACCEPT_TC)
        val mAgreeToRules = ApplicationUtility.readBooleanValue(this, ApplicationConstants.HAS_ACCEPT_TRAFFIC_RULE)

        if (mAgreeToRules && mAgreeToTC){
            val intent = Intent(this,HomeActivity::class.java)
            intent.putExtra(ApplicationConstants.TRAFFIC_INFO,trafficInfo)
            startActivity(intent)
        }else{
            val intent = Intent(this,ConsentActivity::class.java)
            intent.putExtra(ApplicationConstants.TRAFFIC_INFO,trafficInfo)
            intent.putExtra(ApplicationConstants.USER_FULL_NAME,username)
            startActivity(intent)
        }
    }

    private fun shareRouteTrafficInfo(routeTrafficInfo : String)  {

        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT,
                "$routeTrafficInfo\n Get more traffic info\n Download Hodop app on Google playstore"
            )
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }
}
