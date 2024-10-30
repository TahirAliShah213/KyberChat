package com.tahirdev.kyberchat.ui.Chat.messages.allmedias

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.tahirdev.kyberchat.R
import com.tahirdev.kyberchat.databinding.ActivityAllMediaBinding
import com.tahirdev.kyberchat.databinding.ActivityCreateAccountBinding
import com.tahirdev.kyberchat.databinding.FragmentMessageBinding

class AllMediaActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    lateinit var binding: ActivityAllMediaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       // enableEdgeToEdge()

        setContentView(R.layout.activity_all_media)

        binding = ActivityAllMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)

      binding.backBtnAllMedia.setOnClickListener(){
          finish()
      }

        // Apply insets to toolbar so it doesn’t overlap with the status bar

        // Set toolbar as action bar
        val toolbar: Toolbar = findViewById(R.id.toolbar_all_media)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""  // Set title as empty






        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewPager)

        // Set adapter to ViewPager
        val adapter = SectionsPagerAdapter(this)
        viewPager.adapter = adapter

        // Attach TabLayout with ViewPager
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Media"
                1 -> "Links"
                else -> "Documents"
            }
        }.attach()
    }
}
