package com.example.mood_tracker
import android.provider.ContactsContract.Data
import android.text.Editable
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Date

class MoodTracker {
    var firebase : FirebaseDatabase
    var currRating : Double = 0.0
    lateinit var currDesc : String
    lateinit var addMoodActivity : AddMoodActivity

    constructor(activity : AddMoodActivity) {
        firebase = FirebaseDatabase.getInstance()
        addMoodActivity = activity
    }

    fun getMood(year  : Int, month : Int, day : Int) {
        var ratingRef : DatabaseReference = firebase.getReference("" + month.toString() + "/" +
                day.toString() + "/" + year.toString() + "rating")
        var descRef : DatabaseReference = firebase.getReference("" + month.toString() + "/" +
                day.toString() + "/" + year.toString() + "description")
//        Log.w("MainActivity", "Path:" ratingRef.path.toString())
        ratingRef.child("").get().addOnSuccessListener {
                    if (it.exists()) {
                        Log.w("MainActivity", (it.value == null).toString())
                        if (it.value is Long) {
                            addMoodActivity.rating.rating = (it.value as Long).toFloat()
                        } else {
                            addMoodActivity.rating.rating = (it.value as Double).toFloat()
                        }
                        Log.w("MainActivity", "snapshot rating: " + it.value)
                    }
        }.addOnFailureListener {
            Log.e("MainActivity", "Error getting data", it)
        }

        descRef.child("").get().addOnSuccessListener {
                    if (it.exists()) {
                        addMoodActivity.description.text = Editable.Factory.getInstance()
                            .newEditable(it.value.toString())
                        Log.w("MainActivity", "snapshot desc: " + it.value.toString())
                    }

        }.addOnFailureListener {
//            Log.e("firebase", "Error getting data", it)
        }
    }

    fun submitMood(year  : Int, month : Int, day : Int) {
        Log.w("MainActivity", "Inside submitMood")
        val ratingRef : DatabaseReference =
            firebase.getReference( "" + month.toString() + "/" +
                    day.toString() + "/" + year.toString() + "rating")
        Log.w("MainActivity", "Inside submitMood")
        val descRef : DatabaseReference =
            firebase.getReference("" + month.toString() + "/" +
                    day.toString() + "/" + year.toString() + "description")
        ratingRef.setValue(addMoodActivity.rating.rating.toDouble())
        descRef.setValue(addMoodActivity.description.text.toString())
        ratingRef.addValueEventListener(DataListener())
        descRef.addValueEventListener(DataListener())
        Log.w("MainActivity", "Finished submitMood")
    }

    inner class DataListener : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.value != null) {
                if (snapshot.value is String) { //is a description
                    addMoodActivity.description.text = Editable.Factory.getInstance()
                        .newEditable(snapshot.value.toString())
                    Log.w("MainActivity", "snapshot desc: " + snapshot.value.toString())
                } else if (snapshot.value is Long) { //in case the rating is a whole number
                    addMoodActivity.rating.rating = (snapshot.value as Long).toFloat()
                    Log.w("MainActivity", "snapshot rating: " + snapshot.value)
                } else if (snapshot.value is Double) { //in case the rating is a decimal
                    addMoodActivity.rating.rating = (snapshot.value as Double).toFloat()
                    Log.w("MainActivity", "snapshot rating: " + snapshot.value)
                }
            }
        }



        override fun onCancelled(error: DatabaseError) {
            Log.w("MainActivity", "Read failed: " + error.code)
        }

    }

    //encapsulates the rating and description
    inner class Mood {
        lateinit var desc : String
        var rating : Double = 0.0

        constructor(desc : String, rating : Double) {
            this.desc = desc
            this.rating = rating
        }
    }
}