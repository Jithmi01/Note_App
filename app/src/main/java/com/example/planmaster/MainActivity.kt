package com.example.planmaster

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val main = findViewById<Button>(R.id.start1)
        main.setOnClickListener {
            val Intent = Intent(this,HomeActivity::class.java)
            startActivity(Intent)
        }
    }
}