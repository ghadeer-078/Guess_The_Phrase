package com.example.guessthephrase

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var clMain: ConstraintLayout
    lateinit var guss: EditText
    lateinit var sub: Button
    lateinit var tvPhrase: TextView
    lateinit var tvLetters: TextView
    lateinit var myHighScore: TextView

    lateinit var letters: ArrayList<String>
    private val answer = "ghader"
    private val myAnswerDictionary = mutableMapOf<Int,Char>()
    private var myAnswer = ""
    private var guessedLetters = ""
    private var count = 0
    private var guessPhrase = true

    private lateinit var sharedPreferences: SharedPreferences

    private var score = 0
    private var highScore = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = this.getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        highScore = sharedPreferences.getInt("HighScore", 0)

        myHighScore = findViewById(R.id.tvHS)
        myHighScore.text = "High Score: $highScore"


        for(i in answer.indices){
            if(answer[i] == ' '){
                myAnswerDictionary[i] = ' '
                myAnswer += ' '
            }else{
                myAnswerDictionary[i] = '*'
                myAnswer += '*'
            }
        }

        letters = ArrayList()

        clMain = findViewById(R.id.clMain)

        rcGuss.adapter = MessageAdapter(this, letters)
        rcGuss.layoutManager = LinearLayoutManager(this)

        tvPhrase = findViewById(R.id.tvPhrase)
        tvLetters = findViewById(R.id.tvLetters)
        guss = findViewById(R.id.etGuss)
        sub = findViewById(R.id.btSub)
        sub.setOnClickListener { addMessage() }

        updateText()
    }

    private fun addMessage(){
        val msg = guss.text.toString()

        if(guessPhrase){
            if(msg == answer){
                disableEntry()
                updateScore()
                showAlertDialog("You win!\n\nPlay again?")
            }else{
                letters.add("Wrong guess: $msg")
                guessPhrase = false
                updateText()
            }
        }else{
            if(msg.isNotEmpty() && msg.length==1){
                myAnswer = ""
                guessPhrase = true
                checkLetters(msg[0])
            }else{
                Snackbar.make(clMain, "Please enter one letter only",
                    Snackbar.LENGTH_LONG).show()
            }
        }
        guss.text.clear()
        guss.clearFocus()
        rcGuss.adapter?.notifyDataSetChanged()
    }


    private fun disableEntry(){
        sub.isEnabled = false
        sub.isClickable = false
        guss.isEnabled = false
        guss.isClickable = false
    }


    private fun updateText(){
        tvPhrase.text = "Phrase:  " + myAnswer.uppercase()
        tvLetters.text = "Guessed Letters:  " + guessedLetters
        if(guessPhrase){
            guss.hint = "Guess the full phrase"
        }else{
            guss.hint = "Guess a letter"
        }
    }


    private fun checkLetters(guessedLetter: Char){
        var found = 0

        for(i in answer.indices){
            if(answer[i] == guessedLetter){
                myAnswerDictionary[i] = guessedLetter
                found++
            }
        }
        for(i in myAnswerDictionary){
            myAnswer += myAnswerDictionary[i.key]
        }
        if(myAnswer==answer){
            disableEntry()
            updateScore()
            showAlertDialog("You win!\n\nPlay again?")
        }

        if(guessedLetters.isEmpty()){
            guessedLetters+=guessedLetter
        }
        else{
            guessedLetters+=", "+guessedLetter
         }

        if(found>0){
            letters.add("Found $found ${guessedLetter.uppercaseChar()}(s)")
        }else{
            letters.add("No ${guessedLetter.uppercaseChar()}s found")
        }
        count++

        val guessesLeft = 10 - count
        if(count<10){
            letters.add("$guessesLeft guesses remaining")
        }

        updateText()
        
        rcGuss.scrollToPosition(letters.size - 1)
    }


    private fun updateScore(){
        score = 10 - count
        if(score >= highScore){
            highScore = score
            with(sharedPreferences.edit()) {
                putInt("HighScore", highScore)
                apply()
            }
            Snackbar.make(clMain, "NEW HIGH SCORE!", Snackbar.LENGTH_LONG).show()
        }
    }


    private fun showAlertDialog(title: String) {
        // build alert dialog
        val dialogBuilder = AlertDialog.Builder(this)

        // set message of alert dialog
        dialogBuilder.setMessage(title)
            // if the dialog is cancelable
            .setCancelable(false)
            // positive button text and action
            .setPositiveButton("Yes", DialogInterface.OnClickListener {
                    dialog, id -> this.recreate()
            })
            // negative button text and action
            .setNegativeButton("No", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
            })

        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle("Game Over")
        // show alert dialog
        alert.show()
    }
}