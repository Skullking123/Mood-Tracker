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

class MainActivity : AppCompatActivity() {
    lateinit var calendar : CalendarView

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
        description.text = "Hi $name, click on a day to add an entry!"


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

    companion object {
        var selectedDateYear : Int = 0
        var selectedDateMonth : Int = 0
        var selectedDateDay : Int = 0
    }
}

