package com.example.mood_tracker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.CalendarView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Calendar
import java.util.Date
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class MainActivity : AppCompatActivity() {
    lateinit var calendar : CalendarView
    private lateinit var ad : InterstitialAd

    private lateinit var layout : LinearLayout
    private lateinit var title : TextView
    private lateinit var description : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        calendar = findViewById(R.id.calendar)
        calendar.setOnDateChangeListener(CalendarListener())

        //reading user preference
        val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val name = prefs.getString("user_name", "there")
        val mode = prefs.getString("color_mode", "normal")!!

        //find the views
        layout = findViewById(R.id.main)
        title = findViewById<TextView>(R.id.title)
        description = findViewById<TextView>(R.id.calendarInstructions)
        calendar = findViewById(R.id.calendar)

        //setting the colors
        applyColorMode(mode)

        //applying name
        val displayName = name
            .orEmpty()
            .ifEmpty { "there" }
        description.text = "Hi $displayName! click on a day to add an entry!"

        showTheAd( )
    }

    private fun applyColorMode(mode: String) {
        val backgroundColor = ContextCompat.getColor(this,
            if (mode == "colorblind") R.color.backgroundColor_cb else R.color.backgroundColor)
        val titleColor = ContextCompat.getColor(this,
            if (mode == "colorblind") R.color.titleColor_cb else R.color.titleColor)
        val textColor = ContextCompat.getColor(this,
            if (mode == "colorblind") R.color.textColor_cb else R.color.textColor)

        layout.setBackgroundColor(backgroundColor)
        title.setTextColor(titleColor)
        description.setTextColor(textColor)
    }



    inner class CalendarListener : CalendarView.OnDateChangeListener {
        override fun onSelectedDayChange(
            view: CalendarView,
            year: Int,
            month: Int,
            dayOfMonth: Int
        ) {
            Log.w("MainActivity", "" + month + "/" + dayOfMonth + "/" + year)
            var intent : Intent = Intent(this@MainActivity, AddMoodActivity::class.java)
            selectedDateYear = year
            selectedDateMonth = month
            selectedDateDay = dayOfMonth
            startActivity(intent)
        }

    }

    fun showTheAd( ) {
        var builder : AdRequest.Builder = AdRequest.Builder( )
        builder.addKeyword( "fitness" ).addKeyword( "workout" )
        var request : AdRequest = builder.build()

        var adUnitId : String = "ca-app-pub-3940256099942544/1033173712"
        var adLoad : AdLoad = AdLoad( )
        InterstitialAd.load( this, adUnitId, request, adLoad )
    }

    inner class AdLoad : InterstitialAdLoadCallback() {
        override fun onAdLoaded(p0: InterstitialAd) {
            super.onAdLoaded(p0)
            Log.w( "MainActivity", "Inside onAdLoaded" )
            ad = p0
            ad.show( this@MainActivity )

            var manageAdShowing : ManageAdShowing = ManageAdShowing( )
            ad.fullScreenContentCallback = manageAdShowing
        }

        override fun onAdFailedToLoad(p0: LoadAdError) {
            super.onAdFailedToLoad(p0)
            Log.w( "MainActivity", "Inside onAdFailedToLoad" )
        }
    }

    inner class ManageAdShowing : FullScreenContentCallback() {
        override fun onAdClicked() {
            super.onAdClicked()
            Log.w( "MainActivity", "Inside adClicked" )
        }

        override fun onAdImpression() {
            super.onAdImpression()
            Log.w( "MainActivity", "Inside onAdImpression" )
        }

        override fun onAdDismissedFullScreenContent() {
            super.onAdDismissedFullScreenContent()
            Log.w( "MainActivity", "Inside onAdDismissedFullScreenContent" )
        }

        override fun onAdShowedFullScreenContent() {
            super.onAdShowedFullScreenContent()
            Log.w( "MainActivity", "Inside on AdShowedFullScreenContent" )
        }

        override fun onAdFailedToShowFullScreenContent(p0: AdError) {
            super.onAdFailedToShowFullScreenContent(p0)
            Log.w( "MainActivity", "Inside onAdFailedToShowFullScreenContent" )
        }
    }

    companion object {
        var selectedDateYear : Int = 0
        var selectedDateMonth : Int = 0
        var selectedDateDay : Int = 0
    }
}

