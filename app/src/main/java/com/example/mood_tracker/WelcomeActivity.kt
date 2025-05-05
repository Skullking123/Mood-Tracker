package com.example.mood_tracker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class WelcomeActivity : AppCompatActivity() {
    private lateinit var nameInput: EditText
    private lateinit var radioGroup: RadioGroup
    private lateinit var warningMessage: TextView
    private lateinit var continueButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //sets the screen to welcome screen
        setContentView(R.layout.welcome_screen)

        nameInput = findViewById(R.id.nameEntered)
        radioGroup = findViewById(R.id.colorModeGroup)
        warningMessage = findViewById(R.id.warningText)
        continueButton = findViewById(R.id.continueButton)

        continueButton.setOnClickListener { checkInputs() }
    }

    //function will validate the form and move to the next view if valid
    fun checkInputs() {
        //gets name from nameInput field and trim extra spaces
        val name = nameInput.text.toString().trim()
        //gets if of the selected radioButton, if nothing selected, returns 0
        val selectedId = radioGroup.checkedRadioButtonId

        //sees if the name is not empty that that a color option was clicked
        if (name.isNotEmpty() && selectedId != -1) {
            // Save to preferences
            val selectedRadio = findViewById<RadioButton>(selectedId)
            //determines the mode where the user either selects normal or colorblind
            val mode = if (selectedRadio.id == R.id.colorblindOption) "colorblind" else "normal"

            //local persistent data
            val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
            prefs.edit()
                .putString("user_name", name)
                .putString("color_mode", mode)
                .apply()

            //goes to the next actvity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()

        //will display warning message
        } else {
            warningMessage.text = "Please enter your name and select a color preference."
            warningMessage.visibility = TextView.VISIBLE
        }
    }
}