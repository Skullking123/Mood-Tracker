package com.example.mood_tracker

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import org.w3c.dom.Text

class WelcomeActivity : AppCompatActivity() {
    private lateinit var nameInput: EditText
    private lateinit var radioGroup: RadioGroup
    private lateinit var warningMessage: TextView
    private lateinit var continueButton: Button
    private lateinit var layout : RelativeLayout
    private lateinit var title : TextView
    private lateinit var description: TextView
    private lateinit var text : TextView
    private lateinit var colorblindText : TextView
    private lateinit var normalText : TextView
    private lateinit var button : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //sets the screen to welcome screen
        setContentView(R.layout.welcome_screen)

        //getting relevent views
        nameInput = findViewById(R.id.nameEntered)
        radioGroup = findViewById(R.id.colorModeGroup)
        warningMessage = findViewById(R.id.warningText)
        continueButton = findViewById(R.id.continueButton)
        layout = findViewById(R.id.layout)
        title = findViewById(R.id.welcomeTitle)
        description = findViewById(R.id.welcomeDescription)
        text = findViewById(R.id.colorPreferenceText)
        colorblindText = findViewById(R.id.colorblindOption)
        normalText = findViewById(R.id.normalColorOption)

        //load possible saved local data
        val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val savedName = prefs.getString("user_name", "")
        val savedMode = prefs.getString("color_mode", "")

        //load colors base on data, if theres nothing saved, just normal mode
        if (savedMode != null) {
            applyColorMode(savedMode)
        } else {
            applyColorMode("normal")
        }

        //fill name if saved
        nameInput.setText(savedName)

        //check radio button of the last one
        when (savedMode) {
            "colorblind" -> radioGroup.check(R.id.colorblindOption)
            "normal" -> radioGroup.check(R.id.normalColorOption)
        }

        //onClickListener for Radio Button
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.colorblindOption) {
                val mode = "colorblind"
                applyColorMode(mode)
            } else {
                val mode = "normal"
                applyColorMode(mode)
            }
        }

        continueButton.setOnClickListener { checkInputs() }
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
        text.setTextColor(titleColor)
        normalText.setTextColor(textColor)
        colorblindText.setTextColor(textColor)
        continueButton.backgroundTintList = ColorStateList.valueOf(textColor)
    }

    //function will validate the form and move to the next view if valid
    fun checkInputs() {
        //gets name from nameInput field and trim extra spaces
        val name = nameInput.text.toString().trim()
        //gets if of the selected radioButton, if nothing selected, returns 0
        val selectedId = radioGroup.checkedRadioButtonId

        //sees if a color option was clicked
        if (selectedId != -1) {
            // Save to preferences
            val selectedRadio = findViewById<RadioButton>(selectedId)
            //determines the mode where the user either selects normal or colorblind
            val mode = if (selectedRadio.id == R.id.colorblindOption) "colorblind" else "normal"

            //local persistent data
            val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
            prefs.edit()
                .putString("user_name", name) //empty is fine
                .putString("color_mode", mode)
                .apply()

            //goes to the next actvity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()

        //will display warning message
        } else {
            warningMessage.text = "Please select a color preference."
            warningMessage.visibility = TextView.VISIBLE
        }
    }
}