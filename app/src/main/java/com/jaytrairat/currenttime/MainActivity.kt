package com.jaytrairat.currenttime

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.jaytrairat.currenttime.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.timerTask


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var caseNumberTxt: EditText
    private lateinit var layoutUi: LinearLayout
    private fun hideKeyboardAndClearFocus(editText: EditText) {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(editText.windowToken, 0)
        editText.clearFocus()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)

        binding = ActivityMainBinding.inflate(layoutInflater)

        caseNumberTxt = binding.caseNumberTxt
        layoutUi = binding.linearLayout
        caseNumberTxt.setText(sharedPreferences.getString("caseNumber", "F00-00 EV00"))
        caseNumberTxt.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                val editor = sharedPreferences.edit()
                editor.putString("caseNumber", caseNumberTxt.text.toString())
                editor.apply()
                hideKeyboardAndClearFocus(caseNumberTxt)
            }
            true
        }

        layoutUi.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                hideKeyboardAndClearFocus(caseNumberTxt)
            }
            false
        }

        setContentView(binding.root)

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        caseNumberTxt.setText("test")
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            caseNumberTxt.setText("test")
        } else {
            Toast.makeText(this@MainActivity, "Change view", Toast.LENGTH_LONG).show()
        }
    }

    override fun onStart() {
        super.onStart()

        runOnUiThread {
            startTimer()
        }
    }

    fun startTimer() {
        try {
            val elmTimer: Timer = Timer()
            elmTimer.scheduleAtFixedRate(
                timerTask()
                {

                    val patternTime = "HH:mm:ss"
                    val simpleTimeFormat = SimpleDateFormat(patternTime)
                    val time = simpleTimeFormat.format(Date())

                    val patternDate = "(dd MMM YYYY)"
                    val simpleDateFormat = SimpleDateFormat(patternDate)
                    val date = simpleDateFormat.format(Date())

                    this@MainActivity.runOnUiThread(Runnable {
                        var dateView = findViewById<TextView>(R.id.dateView)
                        var timeView = findViewById<TextView>(R.id.timeView)
                        dateView.text = date
                        timeView.text = time
                    })
                }, 0, 1000
            )
        } catch (error: java.lang.Exception) {
            Log.e("ERROR", error.toString())
        }
    }


}