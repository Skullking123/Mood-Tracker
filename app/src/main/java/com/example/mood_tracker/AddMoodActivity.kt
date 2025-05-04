package com.example.mood_tracker

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import androidx.appcompat.app.AppCompatActivity

class AddMoodActivity : AppCompatActivity() {
    lateinit var moodTracker : MoodTracker
    var year : Int = 0
    var month : Int = 0
    var day : Int = 0
    lateinit var description : EditText
    lateinit var rating : RatingBar
    lateinit var submit : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        moodTracker = MoodTracker(this)
        year = MainActivity.selectedDateYear
        month = MainActivity.selectedDateMonth
        day = MainActivity.selectedDateDay
        setContentView(R.layout.addmood)
        description = findViewById<EditText>(R.id.description)
        rating = findViewById<RatingBar>(R.id.rateMood)
        submit = findViewById<Button>(R.id.submitButton)
        submit.setOnClickListener(SubmitListener())
        Log.w("MainActivity", "Finished AddMoodActivity.onCreate")
        updateView()
    }


    //save mood into local persistent data
    override fun onDestroy() {
        super.onDestroy()
    }

    fun updateView() {
        moodTracker.getMood(year, month, day)
    }

    inner class SubmitListener : OnClickListener {
        override fun onClick(v: View?) {
            Log.w("MainActivity", "inside SubmitListener.onClick")
            moodTracker.submitMood(year, month, day)
            finish()
        }

    }

}