package com.tahirdev.kyberchat.ui.Chat.messages.allmedias

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tahirdev.kyberchat.MainActivity
import com.tahirdev.kyberchat.R
import com.tahirdev.kyberchat.databinding.FragmentAppearnceBinding
import com.tahirdev.kyberchat.databinding.FragmentDisMsgBinding

class DisMsgFragment : Fragment() {

    private var _binding: FragmentDisMsgBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDisMsgBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val backBtn: ImageView = binding.backBtnDis
        backBtn.setOnClickListener {
            findNavController().navigate(R.id.action_disappearingMsg_to_messageFragment)
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