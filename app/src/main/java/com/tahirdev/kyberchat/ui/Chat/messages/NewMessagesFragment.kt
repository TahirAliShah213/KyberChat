package com.tahirdev.kyberchat.ui.Chat.messages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.tahirdev.kyberchat.MainActivity
import com.tahirdev.kyberchat.R
import com.tahirdev.kyberchat.databinding.FragmentMessageBinding
import com.tahirdev.kyberchat.databinding.FragmentNewMessagesBinding

class NewMessagesFragment : Fragment() {

    private var _binding: FragmentNewMessagesBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewMessagesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val backBtn: ImageView = binding.backBtnNewMsg
        backBtn.setOnClickListener {
            findNavController().navigate(R.id.action_new_messageFragment_to_chatFragment)
        }

        binding.andersonTxtUser.setOnClickListener(){
            val navController = findNavController()
            navController.navigate(R.id.action_new_messageFragment_to_messageFragment)
        }

        binding.createGroupLayout.setOnClickListener(){
            val navController = findNavController()
            navController.navigate(R.id.action_new_messageFragment_to_createGroupFragment)
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

}