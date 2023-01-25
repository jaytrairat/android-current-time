package com.jaytrairat.currenttime

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.jaytrairat.currenttime.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.timerTask


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                    val patternDate = "วันที่ dd MMM YYYY"
                    val simpleDateFormat = SimpleDateFormat(patternDate)
                    val date = simpleDateFormat.format(Date())

                    val patternTime = "HH:mm:ss"
                    val simpleTimeFormat = SimpleDateFormat(patternTime)
                    val time = simpleTimeFormat.format(Date())

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