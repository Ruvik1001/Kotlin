package com.example.kotlin.activity.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.kotlin.activity.hostfragmentwindow.MainWindowActivity
import com.example.kotlin.data.FirebaseLiveData
import com.example.kotlin.special.database.local.DBSupport
import com.example.kotlin.special.database.remote.DBFirebase
import com.example.kotlin.databinding.ActivityLoginBinding
import com.example.kotlin.special.debug.dbg
import com.example.kotlin.special.dialog.AlertDialog
import com.example.kotlin.special.global.GlobalArgs
import com.example.kotlin.special.model.User
import com.example.kotlin.worker.DailyReminderWorker
import java.util.concurrent.TimeUnit


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, LoginViewModelFactory(this))
            .get(LoginViewModel::class.java)


        viewModel.firebaseLD.connectedData.observe(this) {
            dbg.createLogI("Firebase connected: ${viewModel.firebaseLD.connectedData.value}")
        }

        binding.forgotPassword.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/Viktor1001")))
        }

        binding.btnLogin.setOnClickListener {
            viewModel.auth(
                binding.loginLog.text.toString(),
                binding.loginPassword.text.toString()
                )
        }

        val workRequest = PeriodicWorkRequest.Builder(
            DailyReminderWorker::class.java, 1, TimeUnit.DAYS
        ).build()

        WorkManager.getInstance(applicationContext).enqueue(workRequest)
    }
}