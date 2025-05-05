package com.example.mood_tracker

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import androidx.appcompat.app.AppCompatActivity
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

class AddMoodActivity : AppCompatActivity() {
    private var speechPermission : String = Manifest.permission.RECORD_AUDIO
    private lateinit var launcher : ActivityResultLauncher<String>
    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var listenIntent : Intent

    private lateinit var layout: LinearLayout
    private lateinit var title: TextView


    lateinit var moodTracker : MoodTracker
    var year : Int = 0
    var month : Int = 0
    var day : Int = 0
    lateinit var description : EditText
    lateinit var rating : RatingBar
    lateinit var submit : Button
    lateinit var mic : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.addmood)


        // prepare for speech recognition
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer( this )
        // RecognitionListener
        var speechHandler : SpeechHandler = SpeechHandler( )
        // setRecognitionListener
        speechRecognizer.setRecognitionListener( speechHandler )
        // Intent (listenIntent)
        listenIntent = Intent( RecognizerIntent.ACTION_RECOGNIZE_SPEECH )
        listenIntent.putExtra( RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US" )
        listenIntent.putExtra( RecognizerIntent.EXTRA_PROMPT, "Say something" ) // useless

        mic = findViewById<Button>(R.id.micButton)
        mic.setOnClickListener {
            checkPermissionAndStartListening()
        }

        launcher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                listen()
            } else {
                Toast.makeText(this, "Microphone permission denied", Toast.LENGTH_SHORT).show()
            }
        }

        moodTracker = MoodTracker(this)
        year = MainActivity.selectedDateYear
        month = MainActivity.selectedDateMonth
        day = MainActivity.selectedDateDay
        description = findViewById<EditText>(R.id.description)
        rating = findViewById<RatingBar>(R.id.rateMood)
        submit = findViewById<Button>(R.id.submitButton)
        submit.setOnClickListener(SubmitListener())
        Log.w("MainActivity", "Finished AddMoodActivity.onCreate")

        //finding views for colors
        layout = findViewById(R.id.main)
        title = findViewById(R.id.title)

        //loading user perferences
        val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val mode = prefs.getString("color_mode", "normal")!!
        //applying the colors
        applyColorMode(mode)

        updateView()
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


    private fun checkPermissionAndStartListening() {
        if (ContextCompat.checkSelfPermission(this, speechPermission) == PackageManager.PERMISSION_GRANTED) {
            listen()
        } else {
            launcher.launch(speechPermission)
        }
    }


    //save mood into local persistent data
    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer.destroy()
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

    fun listen( ) {
        try {
            speechRecognizer.startListening(listenIntent)
            Toast.makeText(this, "Listening...", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Error starting speech recognition", Toast.LENGTH_SHORT).show()
        }
    }

    inner class SpeechHandler : RecognitionListener {
        override fun onReadyForSpeech(p0: Bundle?) {
            Log.w( "MainActivity", "Inside onReadyForSpeech" )
        }

        override fun onBeginningOfSpeech() {
            Log.w( "MainActivity", "Inside onBeginningOfSpeech" )
        }

        override fun onRmsChanged(p0: Float) {
            // Log.w( "MainActivity", "Inside onRmsChanged" )
        }

        override fun onBufferReceived(p0: ByteArray?) {
            Log.w( "MainActivity", "Inside onBufferReceived" )
        }

        override fun onEndOfSpeech() {
            Log.w( "MainActivity", "Inside onEndOfSpeech" )
        }

        override fun onError(p0: Int) {
            val errorMessage = when (p0) {
                SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
                SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech detected"
                SpeechRecognizer.ERROR_NO_MATCH -> "Speech not recognized"
                else -> "Error code: $p0"
            }
            Log.w( "MainActivity", errorMessage )
        }

        override fun onResults(p0: Bundle?) {
            Log.w("MainActivity", "Inside onResults")
            if (p0 != null) {
                val words: ArrayList<String>? = p0.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!words.isNullOrEmpty()) {
                    description.setText(words[0]) // Sets text to EditText
                    Log.w("MainActivity", "matched word(s): " + words[0])
                } else {
                    Log.w("MainActivity", "NO result")
                }
            }
        }

        override fun onPartialResults(p0: Bundle?) {
            Log.w( "MainActivity", "Inside onPartialResults" )
        }

        override fun onEvent(p0: Int, p1: Bundle?) {
            Log.w( "MainActivity", "Inside onEvent" )
        }

    }

}