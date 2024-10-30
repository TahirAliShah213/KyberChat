package com.tahirdev.kyberchat.ui.onboard

import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.tahirdev.kyberchat.MainActivity
import com.tahirdev.kyberchat.R
import com.tahirdev.kyberchat.adapter.ViewPagerAdapter
import com.tahirdev.kyberchat.databinding.ActivityIntroBinding
import com.tahirdev.kyberchat.ui.accounts.CreateAccountActivity
import com.tahirdev.kyberchat.ui.accounts.LoginActivity
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator


class IntroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIntroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        }

            //  binding.intro.setBackgroundResource(R.drawable.ob_bg_night)

        val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        when (nightModeFlags) {
            Configuration.UI_MODE_NIGHT_YES -> {
                // Night mode is active, set the night background
                binding.intro.setBackgroundResource(R.drawable.ob_bg_night)
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                // Day mode is active, set the day background
                binding.intro.setBackgroundResource(R.drawable.obg2)
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                // Default mode or undefined, you can set a default background here
                binding.intro.setBackgroundResource(R.drawable.obg2)
            }
        }

        // Set up ViewPager and Adapter
        val adapter = ViewPagerAdapter(this)
        binding.viewPager.adapter = adapter

        val wormDotsIndicator = findViewById<WormDotsIndicator>(R.id.worm_dots_indicator)

        wormDotsIndicator.setViewPager2(binding.viewPager)


        // Attach TabLayout with ViewPager2 for dots indicator
        //    TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position -> }.attach()

        // Handle "I Have Account" button click
        binding.btnHaveAccount.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Handle "Create Account" button click
        binding.btnCreateAccount.setOnClickListener {
            val intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent)
            finish()
        }


    }

    private fun navigateToMainScreen() {
        // Navigate to the main activity (Main screen of the app)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToCreateAccountScreen() {
        // Navigate to account creation screen
        // You would need to create an account creation activity if it doesn't exist
     //   val intent = Intent(this, CreateAccountActivity::class.java)
        startActivity(intent)
        finish()
    }
}
