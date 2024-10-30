package com.tahirdev.kyberchat.ui.accounts

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.tahirdev.kyberchat.R
import com.tahirdev.kyberchat.databinding.ActivityCreateAccountBinding
import com.tahirdev.kyberchat.databinding.ActivityLoginBinding

class CreateAccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        lateinit var binding: ActivityCreateAccountBinding

        super.onCreate(savedInstanceState)

            binding = ActivityCreateAccountBinding.inflate(layoutInflater)
            setContentView(binding.root)

            // Load the LoginFragment initially
            if (savedInstanceState == null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, LicenceFragment()) // Use the correct container ID
                    .commit()
            }


    }
}