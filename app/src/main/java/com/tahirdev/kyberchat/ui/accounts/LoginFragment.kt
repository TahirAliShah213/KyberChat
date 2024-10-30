package com.tahirdev.kyberchat.ui.accounts

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.tahirdev.kyberchat.R
import com.tahirdev.kyberchat.databinding.FragmentLoginBinding // Make sure you have this binding class

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null // Use a nullable variable for binding
    private val binding get() = _binding!! // Non-nullable property

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment and initialize binding
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = binding.username // Assuming you have an EditText with this ID in your layout
        val loginButton = binding.login // Assuming you have a MaterialButton with this ID in your layout

        // Disable the button initially
        loginButton.isEnabled = false
        loginButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blue_btn_off))
        loginButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))

        // Set up a TextWatcher for the username input
        username.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Check if the length of the input is greater than 3
                if (s != null && s.length > 3) {
                    loginButton.isEnabled = true
                    loginButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blue))
                    loginButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                } else {
                    // Disable the button if the condition is not met
                    loginButton.isEnabled = false
                    loginButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blue_btn_off))
                    loginButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // Not needed
            }
        })

        // Set up a click listener for the login button
        loginButton.setOnClickListener {

            val passwordFragment = PasswordFragment()

            // Pass username using Bundle
            val bundle = Bundle()
            bundle.putString("USERNAME_KEY", username.toString())
            passwordFragment.arguments = bundle

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, passwordFragment) // Use the correct container ID
                .addToBackStack(null) // Optional: Add this transaction to the back stack
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Avoid memory leaks
    }
}
