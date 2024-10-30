package com.tahirdev.kyberchat.ui.Chat.messages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tahirdev.kyberchat.MainActivity
import com.tahirdev.kyberchat.R
import com.tahirdev.kyberchat.databinding.FragmentMessageRequestBinding

class MessageRequest : Fragment() {

    private var _binding: FragmentMessageRequestBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMessageRequestBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Hide BottomNavigationView
        val bottomNav = (activity as MainActivity).findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNav.visibility = View.GONE


        // Set up toolbar as action bar with back button
        val toolbar = binding.toolbarMessageRequest
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        binding.backBtn.setOnClickListener(){
            findNavController().navigateUp() // This will pop the back stack
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val bottomNav = (activity as MainActivity).findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNav.visibility = View.VISIBLE
        _binding = null
    }

}