package com.example.cookieclicker

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class MainActivity2 : AppCompatActivity() {
    private var currentScore = 0
    private var cookieSpeed = 1
    private var boostPrice = 20
    private var boostInterval: Long = 1000 // Интервал в миллисекундах
    private lateinit var sharedPref: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private var handler: Handler = Handler()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        // Получаем доступ к SharedPreferences
        sharedPref = getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE)
        editor = sharedPref.edit()

        // Инициализируем currentScore, cookieSpeed и boostPrice значениями из SharedPreferences
        currentScore = sharedPref.getInt("myNumber", 0)
        cookieSpeed = sharedPref.getInt("cookieSpeed", 1)
        boostPrice = sharedPref.getInt("boostPrice", 20)

        val backButton: Button = findViewById(R.id.back)
        val upg1Button: Button = findViewById(R.id.upg1)
        val lblTotal: TextView = findViewById(R.id.txtRolls)

        lblTotal.text= currentScore.toString()

        backButton.setOnClickListener{
            val randomIntent = Intent(this,MainActivity::class.java)
            startActivity(randomIntent)
            val returnIntent = Intent()
            returnIntent.putExtra("updatedScore", currentScore)
            returnIntent.putExtra("updatedCookieSpeed", cookieSpeed)
            returnIntent.putExtra("updatedBoostPrice", boostPrice)
            setResult(RESULT_OK, returnIntent)
            finish()
        }

        upg1Button.setOnClickListener {
            if (currentScore >= boostPrice) {
                currentScore -= boostPrice
                editor.putInt("myNumber", currentScore)
                editor.apply()

                // Увеличиваем скорость на 1 печеньку
                increaseCookieSpeedByOne()

                boostPrice += cookieSpeed * 20
                editor.putInt("boostPrice", boostPrice)
                editor.apply()

                // Обновляем текст на кнопке "Поднажать"
                upg1Button.text = "+1 бросок/сек ($boostPrice)"
                // После обновления значений


            } else {
                Toast.makeText(this, "Недостаточно печенек для увеличения скорости!", Toast.LENGTH_SHORT).show()
            }
        }


    }


    private fun increaseCookieSpeedByOne() {
        cookieSpeed++
        editor.putInt("cookieSpeed", cookieSpeed)
        editor.apply()


    }

    }
