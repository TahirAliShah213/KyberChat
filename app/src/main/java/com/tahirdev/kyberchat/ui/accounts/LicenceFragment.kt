package com.tahirdev.kyberchat.ui.accounts

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.SpannableString
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.tahirdev.kyberchat.MainActivity
import com.tahirdev.kyberchat.R
import com.tahirdev.kyberchat.databinding.FragmentLicenceBinding
import com.tahirdev.kyberchat.databinding.FragmentPasswordBinding

class LicenceFragment : Fragment() {


    private var _binding: FragmentLicenceBinding? = null // Use a nullable variable for binding
    private val binding get() = _binding!! // Non-nullable property
    private var isPasswordVisible = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Initialize ViewBinding
        _binding = FragmentLicenceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val textView = binding.textweb
        // Create the text with the clickable portion
        val text = "Please use the web link to enter your license key in order to enhance your privacy"
        val spannableString = SpannableString(text)

        // Define the clickable span
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                // Open the web link when the span is clicked
                val url = "https://www.example.com" // Replace with your actual URL
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                startActivity(intent)
            }
        }

        // Set the clickable span from "web link"
        val startIndex = text.indexOf("web link")
        val endIndex = startIndex + "web link".length
        spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(StyleSpan(Typeface.BOLD), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        // Apply the SpannableString to the TextView
        textView.text = spannableString

        // Enable links and make it clickable
        textView.movementMethod = android.text.method.LinkMovementMethod.getInstance()



        val pass = binding.password // Assuming you have an EditText with this ID in your layout
        val loginButton =
            binding.login // Assuming you have a MaterialButton with this ID in your layout
        val eyeIcon = binding.togglePasswordVisibility // Reference to the eye icon

        // Disable the button initially
        loginButton.isEnabled = false

        loginButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blue_btn_off))
        loginButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))

        // Set password input type
        pass.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        // Set up a TextWatcher for the password input
        pass.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Check if the length of the input is greater than 3
                loginButton.isEnabled = s != null && s.length > 3
                updateLoginButtonAppearance(loginButton)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Set up a click listener for the eye icon
        eyeIcon.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            updatePasswordVisibility(pass)
            updateEyeIcon(eyeIcon)
        }

        // Set up a click listener for the login button
        loginButton.setOnClickListener {
            val loginFragment = LoginFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, loginFragment) // Use the correct container ID
                .addToBackStack(null) // Optional: Add this transaction to the back stack
                .commit()
        }
    }

    private fun updatePasswordVisibility(pass: EditText) {
        pass.inputType = if (isPasswordVisible) {
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
        pass.setSelection(pass.text.length) // Keep cursor at the end
    }

    private fun updateEyeIcon(eyeIcon: ImageView) {
        if (isPasswordVisible) {
            eyeIcon.setImageResource(R.drawable.visibility) // Change to your visible icon
        } else {
            eyeIcon.setImageResource(R.drawable.notvisible) // Change to your invisible icon
        }
    }

    private fun updateLoginButtonAppearance(loginButton: MaterialButton) {
        if (loginButton.isEnabled) {
            loginButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blue))
            loginButton.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            ) // Active text color
        } else {
            loginButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blue_btn_off))
            loginButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Avoid memory leaks
    }
}
