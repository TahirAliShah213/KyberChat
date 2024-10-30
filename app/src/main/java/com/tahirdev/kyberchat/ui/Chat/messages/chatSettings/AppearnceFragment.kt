package com.tahirdev.kyberchat.ui.Chat.messages.chatSettings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import com.tahirdev.kyberchat.MainActivity
import com.tahirdev.kyberchat.R
import com.tahirdev.kyberchat.databinding.FragmentAppearnceBinding

class AppearnceFragment : Fragment() {

    private var _binding: FragmentAppearnceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAppearnceBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val backBtn: ImageView = binding.backBtnAppe
        backBtn.setOnClickListener {
            findNavController().navigate(R.id.action_appearance_to_navigation_notifications)
        }



        // Load saved theme from SharedPreferences
        val sharedPref = requireContext().getSharedPreferences("ThemePrefs", 0)
        var savedTheme = sharedPref.getString("selectedTheme", "light")  // default is light

        // Update UI based on saved theme
        when (savedTheme) {
            "default" -> {
                binding.chooseTheme.text = "System Default"
            }
            "light" -> {
                binding.chooseTheme.text = "Light"
            }
            "dark" -> {
                binding.chooseTheme.text = "Dark"
            }
        }

        binding.themeLayout.setOnClickListener {
            val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.appearance_dialog, null)

            val dialog = AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .create()

            val window = dialog.window
            window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            window?.setBackgroundDrawableResource(android.R.color.transparent)

            val radioGroup = dialogView.findViewById<RadioGroup>(R.id.radioGroup1)

            // Load the saved theme again when the dialog is opened to ensure the right one is selected
            savedTheme = sharedPref.getString("selectedTheme", "light")  // reload savedTheme

            // Pre-select the correct radio button based on the saved theme
            when (savedTheme) {
                "default" -> radioGroup.check(R.id.radioButton_default)
                "light" -> radioGroup.check(R.id.radioButton_Light)
                "dark" -> radioGroup.check(R.id.radioButton_Dark)
            }

            dialogView.findViewById<Button>(R.id.ok_btn).setOnClickListener {
                val selectedRadioButtonId = radioGroup.checkedRadioButtonId
                val editor = sharedPref.edit()

                when (selectedRadioButtonId) {
                    R.id.radioButton_default -> {
                        binding.chooseTheme.text = "System Default"
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                        editor.putString("selectedTheme", "default")
                    }
                    R.id.radioButton_Light -> {
                        binding.chooseTheme.text = "Light"
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        editor.putString("selectedTheme", "light")
                    }
                    R.id.radioButton_Dark -> {
                        binding.chooseTheme.text = "Dark"
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        editor.putString("selectedTheme", "dark")
                    }
                }

                editor.apply()  // Save the theme preference
                dialog.dismiss()
            }

            dialog.show()
        }

        return root
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setBottomNavVisibility(false)
    }

    override fun onPause() {
        super.onPause()
        (activity as MainActivity).setBottomNavVisibility(true)
    }

    override fun onDestroy() {
        super.onDestroy()
      //  findNavController().popBackStack()
    }
}
