package com.tahirdev.kyberchat.ui.profiles

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.tahirdev.kyberchat.MainActivity
import com.tahirdev.kyberchat.R
import com.tahirdev.kyberchat.databinding.FragmentAccountDetailsBinding
import com.tahirdev.kyberchat.databinding.FragmentInviteBinding

class InviteFragment : Fragment() {

    private var _binding: FragmentInviteBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInviteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val backBtn: ImageView = binding.backBtnInvite
        backBtn.setOnClickListener {
            findNavController().navigate(R.id.action_inviteFriendFragment_to_navigation_notifications)
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