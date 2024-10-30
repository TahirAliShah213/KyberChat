package com.tahirdev.kyberchat.ui.profiles

import SettingItem
import SettingsAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tahirdev.kyberchat.R
import com.tahirdev.kyberchat.databinding.FragmentProfileBinding

class ProfilesFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupRecyclerView()

        requireActivity().window.navigationBarColor = ContextCompat.getColor(requireContext(), R.color.blue_light)


        return root
    }

    private fun setupRecyclerView() {
        val settings = listOf(
            SettingItem("Account Detail", R.drawable.uprofile),
            SettingItem("Privacy", R.drawable.lock),
            SettingItem("Message Request", R.drawable.info),

            SettingItem("Notification", R.drawable.notification),
            SettingItem("Chats", R.drawable.message),
            SettingItem("Appearance", R.drawable.magicpen),
            SettingItem("Subscriptions", R.drawable.cards),
            SettingItem("Recovery Password", R.drawable.shield),
            SettingItem("Help", R.drawable.question),
            SettingItem("Invite friends", R.drawable.message),
            SettingItem("Delete Account", R.drawable.mdi_delete)
        )



        val adapter = SettingsAdapter(settings) { settingItem ->
            // Handle click event for each setting item
            when (settingItem.name) {
                "Account Detail" -> {
                    findNavController().navigate(R.id.action_navigation_notifications_to_account_detailsFragment)
                }
                "Privacy" -> { /* Navigate to Privacy settings */ }
                "Message Request" -> { /* Navigate to Message Request settings */ }
                "Notification" -> { /* Navigate to Notification settings */ }
                "Chats" -> { /* Navigate to Chats */ }
                "Appearance" -> {
                    findNavController().navigate(R.id.action_navigation_notifications_to_appearanceFrag)
                }
                "Subscriptions" -> { /* Navigate to Subscriptions */ }
                "Recovery Password" -> { /* Navigate to Recovery Password */ }
                "Help" -> { /* Navigate to Help */ }
                "Invite friends" -> {
                    findNavController().navigate(R.id.action_navigation_notifications_to_inviteFriendFragment)
                }
                "Delete Account" -> { /* Navigate to Delete Account */ }
            }
        }

        binding.recyclerViewSettings.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewSettings.adapter = adapter


    }

    override fun onResume() {
        super.onResume()
        // Set navigation bar color when this fragment becomes active
        requireActivity().window.navigationBarColor = ContextCompat.getColor(requireContext(), R.color.blue_light)
    }

    override fun onPause() {
        super.onPause()
        // Reset or change navigation bar color when leaving the fragment
        requireActivity().window.navigationBarColor = ContextCompat.getColor(requireContext(), com.google.zxing.client.android.R.color.zxing_transparent)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        requireActivity().window.navigationBarColor = ContextCompat.getColor(requireContext(), com.google.zxing.client.android.R.color.zxing_transparent)

        _binding = null
    }
}


