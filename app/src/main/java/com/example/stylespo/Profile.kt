package com.example.stylespo

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class Profile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.selectedItemId = R.id.bottom_profile
        bottomNavigationView.setOnItemSelectedListener { item: MenuItem ->
            val itemId = item.itemId
            if (itemId == R.id.bottom_home) {
                startActivity(Intent(applicationContext, HomePage::class.java))
                finish()
                return@setOnItemSelectedListener true
            } else if (itemId == R.id.bottom_add) {
                startActivity(Intent(applicationContext, Add::class.java))
                finish()
                return@setOnItemSelectedListener true
            } else if (itemId == R.id.bottom_discover) {
                startActivity(Intent(applicationContext, Discovery::class.java))
                finish()
                return@setOnItemSelectedListener true
            } else if (itemId == R.id.bottom_profile) {
                return@setOnItemSelectedListener true
            }
            false
        }
    }
}