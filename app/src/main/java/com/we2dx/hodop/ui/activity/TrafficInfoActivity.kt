package com.we2dx.hodop.ui.activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.we2dx.hodop.R

class TrafficInfoActivity : AppCompatActivity() {

    private lateinit var mShareTrafficInfoCard : CardView
    private lateinit var mAlternativeRoute : CardView
    private lateinit var mPostOnHodopCard : CardView
    private lateinit var mTrafficReportCard : CardView

    private lateinit var mFromDestination : AppCompatTextView
    private lateinit var mToDestination : AppCompatTextView
    private lateinit var mTrafficInfoText : AppCompatTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_traffic_info)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDarker)
        }
        mShareTrafficInfoCard = findViewById(R.id.share_traffic_report_card)
        mAlternativeRoute = findViewById(R.id.alter_route_card)
        mPostOnHodopCard = findViewById(R.id.post_on_hodop_card)
        mTrafficReportCard = findViewById(R.id.traffic_report_card)

        mFromDestination = findViewById(R.id.from_loctaion_text)
        mToDestination = findViewById(R.id.to_loctaion_text)
        mTrafficInfoText = findViewById(R.id.traffic_report_text)

        mShareTrafficInfoCard.setOnClickListener { shareRouteTrafficInfo("Traffic") }
    }
    fun shareRouteTrafficInfo(routeTrafficInfo : String)  {

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
