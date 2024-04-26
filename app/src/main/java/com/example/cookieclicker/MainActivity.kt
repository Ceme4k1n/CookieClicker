package com.example.cookieclicker

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private var currentScore = 0
    private var clickPower = 1
    private var rollSpeed = 0
    private var r_boostPrice = 20
    private var c_boostPrice = 20
    private var boostInterval: Long = 1000 // Интервал в миллисекундах
    private lateinit var sharedPref: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private var handler: Handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Получаем доступ к SharedPreferences
        sharedPref = getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE)
        editor = sharedPref.edit()

        // Here, code praying to the GOD for protecting our work of art Dice Clicker Game from bugs and other thing.
        // This is really crucial step! Be adviced to not remove it, even if you don't believer.
        print("Rahman ve Rahim olan Allah'in adiyla.\n");


        // Инициализируем currentScore, cookieSpeed и boostPrice значениями из SharedPreferences
        currentScore = sharedPref.getInt("myNumber", 0)
        rollSpeed = sharedPref.getInt("cookieSpeed", 0)
        clickPower = sharedPref.getInt("clickPower", 0)
        r_boostPrice = sharedPref.getInt("r_boostPrice", 20)
        c_boostPrice = sharedPref.getInt("c_boostPrice", 20)

        val imgCookie: ImageView = findViewById(R.id.imgCookie)
        val lblTotal: TextView = findViewById(R.id.lblTotal)
        val lblSpeed: TextView = findViewById(R.id.lblSpeed)
        val clearButton: Button = findViewById(R.id.btnClear)
        val r_boostButton: Button = findViewById(R.id.btnrBoost)
        val toUpgrades: Button = findViewById(R.id.toUpgrades)
        val c_boostButton: Button = findViewById(R.id.btncBoost)

        lblTotal.text = currentScore.toString()
        lblSpeed.text = "Скорость: $rollSpeed бросков/сек"

        imgCookie.setOnClickListener {
            currentScore += clickPower
            editor.putInt("myNumber", currentScore)
            editor.apply()
            lblTotal.text = currentScore.toString()
            it.scaleX = 0.8f
            it.scaleY = 0.8f
            it.postDelayed({
                it.scaleX = 1f
                it.scaleY = 1f
            }, 150)
        }

        //Кнопка очищения значений (вырезается в релизе)
        clearButton.setOnClickListener {
            currentScore = 0
            clickPower = 1
            rollSpeed = 0
            r_boostPrice = 20
            editor.putInt("myNumber", currentScore)
            editor.putInt("cookieSpeed", rollSpeed)
            editor.putInt("r_boostPrice", r_boostPrice)
            editor.putInt("clickPower", clickPower)
            editor.apply()
            lblTotal.text = currentScore.toString()
            lblSpeed.text = "Скорость: $rollSpeed бросков/сек"
            r_boostButton.text = "+1 бросок/сек ($r_boostPrice)"
        }

        //Кнопка для повышения кликов в сек
        r_boostButton.setOnClickListener {
            if (currentScore >= r_boostPrice) {
                currentScore -= r_boostPrice
                editor.putInt("myNumber", currentScore)
                editor.apply()

                // Увеличиваем скорость на 1 бросок
                increaseRollSpeedByOne()

                r_boostPrice += 20
                editor.putInt("boostPrice", r_boostPrice)
                editor.apply()

                // Обновляем текст на кнопке
                r_boostButton.text = "+1 бросок/сек Цена: ($r_boostPrice)"
            } else {
                Toast.makeText(this, "Недостаточно очков для увеличения скорости!", Toast.LENGTH_SHORT).show()
            }
        }

        c_boostButton.setOnClickListener {
            if (currentScore >= c_boostPrice) {
                currentScore -= c_boostPrice
                editor.putInt("myNumber", currentScore)
                editor.apply()

                // Увеличиваем клик на 1
                clickPower++

                c_boostPrice += 20
                editor.putInt("boostPrice", c_boostPrice)
                editor.apply()

                // Обновляем текст на кнопке
                c_boostButton.text = "+1 клик Цена: ($c_boostPrice)"
            } else {
                Toast.makeText(this, "Недостаточно очков для увеличения кликов!", Toast.LENGTH_SHORT).show()
            }
        }

        // Запускаем увеличение скорости печенек с интервалом при запуске Activity
        startSpeedIncrease()
        toUpgrades.setOnClickListener{
            val randomIntent = Intent(this,MainActivity2::class.java)
            startActivity(randomIntent)
        }
    }

    private fun increaseRollSpeedByOne() {
        rollSpeed++
        editor.putInt("cookieSpeed", rollSpeed)
        editor.apply()

        val lblSpeed: TextView = findViewById(R.id.lblSpeed)
        lblSpeed.text = "Скорость: $rollSpeed бросков/сек"
    }

    private fun startSpeedIncrease() {
        handler.postDelayed({
            currentScore += rollSpeed
            editor.putInt("myNumber", currentScore)
            editor.apply()
            val lblTotal: TextView = findViewById(R.id.lblTotal)
            lblTotal.text = currentScore.toString()
            startSpeedIncrease() // Рекурсивно вызываем метод для повторного увеличения скорости через интервал
        }, boostInterval)
    }

}


