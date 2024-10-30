package com.tahirdev.kyberchat.ui.accounts

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.tahirdev.kyberchat.MainActivity
import com.tahirdev.kyberchat.R
import com.tahirdev.kyberchat.databinding.FragmentPasswordBinding
import com.tahirdev.kyberchat.ui.Chat.chat_main.ChatFragment

class PasswordFragment : Fragment() {

    private var _binding: FragmentPasswordBinding? = null // Use a nullable variable for binding
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
        _binding = FragmentPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pass = binding.password // Assuming you have an EditText with this ID in your layout
        val loginButton = binding.login // Assuming you have a MaterialButton with this ID in your layout
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

            // Retrieve the username from arguments passed by LoginFragment
            val username = arguments?.getString("USERNAME_KEY")

            // Set up click listener for the button to navigate to ChatFragment
                val chatFragment = ChatFragment()

                // Pass the username to ChatFragment
                val bundle = Bundle()
                bundle.putString("USERNAME_KEY", username) // Pass the same username
                chatFragment.arguments = bundle


                val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
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
            eyeIcon.setImageResource(R.drawable.ic_eye_hide) // Change to your visible icon
        } else {
            eyeIcon.setImageResource(R.drawable.eye_visible) // Change to your invisible icon
        }
    }

    private fun updateLoginButtonAppearance(loginButton: MaterialButton) {
        if (loginButton.isEnabled) {
            loginButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blue))
            loginButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
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
