package com.google.firebase.quickstart.InnDiary

import android.content.Intent
import com.firebase.example.internal.BaseEntryChoiceActivity
import com.firebase.example.internal.Choice
import com.google.firebase.quickstart.InnDiary.java.MainActivity

class EntryChoiceActivity : BaseEntryChoiceActivity() {

    override fun getChoices(): List<Choice> {
        return listOf(
                Choice(
                        "Java",
                        "Run the Firebase Auth quickstart written in Java.",
                        Intent(this, MainActivity::class.java)),
                Choice(
                        "Kotlin",
                        "Run the Firebase Auth quickstart written in Kotlin.",
                        Intent(this, com.google.firebase.quickstart.InnDiary.kotlin.MainActivity::class.java))
        )
    }
}