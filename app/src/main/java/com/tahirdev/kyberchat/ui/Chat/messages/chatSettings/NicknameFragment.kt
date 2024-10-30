package com.tahirdev.kyberchat.ui.Chat.messages.chatSettings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.tahirdev.kyberchat.MainActivity
import com.tahirdev.kyberchat.R
import com.tahirdev.kyberchat.databinding.FragmentChatSettingsBinding
import com.tahirdev.kyberchat.databinding.FragmentNicknameBinding

class NicknameFragment : Fragment() {
    private var _binding: FragmentNicknameBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentNicknameBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val backBtn: ImageView = binding.backBtnNick
        backBtn.setOnClickListener {
            findNavController().navigate(R.id.action_nicknameFrag_to_chatSettingMsg)
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