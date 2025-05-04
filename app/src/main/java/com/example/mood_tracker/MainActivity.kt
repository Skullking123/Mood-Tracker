package com.example.mood_tracker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.CalendarView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Calendar
import java.util.Date

class MainActivity : AppCompatActivity() {
    lateinit var calendar : CalendarView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        calendar = findViewById(R.id.calendar)
        calendar.setOnDateChangeListener(CalendarListener())
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

