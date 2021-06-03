package com.example.library

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        splash()
    }

    private fun splash(){
        Handler().postDelayed(
            {
                val intent = Intent(this@SplashActivity, GenreActivity::class.java)
                startActivity(intent)
                finish()
            },2000)
    }
}