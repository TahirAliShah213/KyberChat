package com.tahirdev.kyberchat.ui.Chat.messages.allmedias

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.tahirdev.kyberchat.MainActivity
import com.tahirdev.kyberchat.R
import com.tahirdev.kyberchat.databinding.FragmentChatSettingsBinding
import com.tahirdev.kyberchat.databinding.FragmentMessageBinding

class ChatSettingsFragment : Fragment() {

    private var _binding: FragmentChatSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChatSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val backBtn: ImageView = binding.backBtnChatSetting
        backBtn.setOnClickListener {
            findNavController().navigate(R.id.action_chatSettingMsg_to_messageFragment)
        }

//        binding.chatSettingDis.setOnClickListener(){
//            findNavController().navigate(R.id.action_chatSettingMsg_to_appearanceFrag)
//        }

        binding.chatSettingNick.setOnClickListener(){
            findNavController().navigate(R.id.action_chatSettingMsg_to_nicknameFrag)
        }

       /* binding.chatSettingAppearance.setOnClickListener(){
            findNavController().navigate(R.id.action_chatSettingMsg_to_appearanceFrag)
        }*/

        binding.chatSettingSpam.setOnClickListener(){
            val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.report_spam_dialog, null)

            val dialog = AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .create()

            val window = dialog.window
            window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            window?.setBackgroundDrawableResource(android.R.color.transparent)


            dialogView.findViewById<TextView>(R.id.dialog_report_spam_cancel).setOnClickListener {

                dialog.dismiss()
            }

            dialog.show()
        }

        // findNavController().navigate(R.id.action_messageFragment_to_disappearingMsg)

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