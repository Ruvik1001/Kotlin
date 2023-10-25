package com.example.kotlin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlin.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.forgotPassword.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/Viktor1001")))
        }
        binding.btnLogin.setOnClickListener {
            // SEARCH IN DB
            val intent = Intent(this, MainWindowActivity::class.java)
            startActivity(intent)
            finish()
        }


//        val dbFirebase = DBFirebase()
//        dbFirebase.addUser("Ruvik1001", "Gfhjkm007q") {userAdded ->
//            if (userAdded) Log.d("MyTag", "Success")
//            else Log.d("MyTag", "Failed")
//        }
    }
}