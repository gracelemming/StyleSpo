package com.example.stylespo

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomePage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.selectedItemId = R.id.bottom_home
        bottomNavigationView.setOnItemSelectedListener { item: MenuItem ->
            val itemId = item.itemId
            if (itemId == R.id.bottom_home) {
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
                startActivity(Intent(applicationContext, Profile::class.java))
                finish()
                return@setOnItemSelectedListener true
            }
            false
        }
    }
}