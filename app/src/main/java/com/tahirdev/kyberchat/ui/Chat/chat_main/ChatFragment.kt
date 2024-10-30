package com.tahirdev.kyberchat.ui.Chat.chat_main

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tahirdev.kyberchat.R
import com.tahirdev.kyberchat.databinding.FragmentChatBinding

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    // List of conversations (for demo, this should be retrieved from your data source)
    private val chatList = mutableListOf(
        ChatItem(
            "Kieron Dotson",
            "Hey! How are you? How are you? How are you? How are you? How are you?",
            2,
            R.drawable.sample_cat
        ),
        ChatItem("Jane Smith", "Let's catch up tomorrow.", 0,
            R.drawable.sample_fitness),
        ChatItem("Alice Johnson", "Check this out!", 3,
            R.drawable.sample_hacker),
        ChatItem("Mira Clara", "Are you free tonight?", 1,
            R.drawable.sample_gamer),

        ChatItem(
            "John Doe",
            "Hey! How are you? How are you? How are you? How are you? How are you?",
            2,
            R.drawable.avatar2
        ),

        ChatItem(
            "Kieron Dotson",
            "Hey! How are you? How are you? How are you? How are you? How are you?",
            2,
            R.drawable.avatar1
        ),
        ChatItem("Jane Smith", "Let's catch up tomorrow.", 0,
            R.drawable.avatar4),
        ChatItem("Alice Johnson", "Check this out!", 3,
            R.drawable.person),
        ChatItem("Mira Clara", "Are you free tonight?", 1,
            R.drawable.avatar7),

        ChatItem(
            "John Doe",
            "Hey! How are you? How are you? How are you? How are you? How are you?",
            2,
            R.drawable.avatar4
        ),


        ChatItem("Jane Smith", "Let's catch up tomorrow.", 0),
        ChatItem("Alice Johnson", "Check this out!", 3),
        ChatItem("Bob Martin", "Are you free tonight?", 1),
        ChatItem("Jane Smith", "Let's catch up tomorrow.", 0),
        ChatItem("Alice Johnson", "Check this out!", 3),
        ChatItem("Bob Martin", "Are you free tonight?", 1)


    )

    // Simulated unknown message request count (for demo, update with actual data)
    private val unknownMessageRequests = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val chatViewModel = ViewModelProvider(this).get(ChatViewModel::class.java)

        _binding = FragmentChatBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Set up RecyclerView with Adapter
        val recyclerView = binding.chatRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = ChatAdapter(chatList)
        recyclerView.adapter = adapter

        // Set up toolbar as action bar
        val toolbar = binding.toolbarChat
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.title = ""

        // Enable menu in this fragment's toolbar
        setHasOptionsMenu(true)

        // Floating Action Button to open MessageFragment
        binding.floatingActionButton.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.action_chatFragment_to_new_messageFragment)
        }


        binding.messageRequestsSection.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.action_chatFragment_to_messageRequest)
        }


// Handle long press
        adapter.onItemLongPress = { chatItem ->
            showOptionsDialog(chatItem)
        }



      //  updateMessageRequests()
        binding.messageRequestsSection.visibility = View.GONE

        setupRecyclerViewScrollListener()

//        val window: Window = requireActivity().window
//        window.navigationBarColor = ContextCompat.getColor(requireContext(), R.color.light_sky)

        // Set the navigation bar color using a color from resources (R.color.light_sky)
        requireActivity().window.navigationBarColor = ContextCompat.getColor(requireContext(), R.color.blue_light)


        return root

    }




    private fun updateMessageRequests() {
        if (unknownMessageRequests > 0) {
            binding.messageRequestsSection.visibility = View.VISIBLE
            binding.messageRequestsCount.text = unknownMessageRequests.toString()
        } else {
            binding.messageRequestsSection.visibility = View.GONE
        }
    }

    private fun setupRecyclerViewScrollListener() {
        binding.chatRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // Check if the RecyclerView is scrolled to the top
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val firstVisibleItemPosition =
                    layoutManager.findFirstCompletelyVisibleItemPosition()

                if (firstVisibleItemPosition == 0) {
                    // At the top, reset toolbar and status bar to original colors
                    binding.toolbarChat.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                    activity?.window?.statusBarColor =
                        ContextCompat.getColor(requireContext(), R.color.white)
                } else {
                    // Scrolled down, change toolbar and status bar colors
                    binding.toolbarChat.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.gray_200
                        )
                    )
                    activity?.window?.statusBarColor =
                        ContextCompat.getColor(requireContext(), R.color.gray_200)
                }
            }
        })
    }

    // Inflate the custom menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.chat_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    // Handle menu item clicks
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> {
                updateMessageRequests()
                true
            }

            R.id.action_more -> {
                // Handle 3-dots menu click
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showOptionsDialog(chatItem: ChatItem) {
        val dialogView =
            LayoutInflater.from(requireContext()).inflate(R.layout.message_options_dialog, null)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        val window = dialog.window
        window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window?.setBackgroundDrawableResource(android.R.color.transparent)


        // Unread option
        dialogView.findViewById<LinearLayout>(R.id.option_unread).setOnClickListener {
            if (chatItem.newMessages > 0) {
                markAsUnread(chatItem)
            } else {
                pinChat(chatItem)
            }
            dialog.dismiss()
        }

        // Pin option
        dialogView.findViewById<LinearLayout>(R.id.option_pin).setOnClickListener {
            pinChat(chatItem)
            dialog.dismiss()
        }

        // Mute option
        dialogView.findViewById<LinearLayout>(R.id.option_mute).setOnClickListener {
            muteChat(chatItem)
            dialog.dismiss()
        }

        // Select option
        dialogView.findViewById<LinearLayout>(R.id.option_select).setOnClickListener {
            selectChat(chatItem)
            dialog.dismiss()
        }

        // Archive option
        dialogView.findViewById<LinearLayout>(R.id.option_archive).setOnClickListener {
            archiveChat(chatItem)
            dialog.dismiss()
        }

        // Delete option
        dialogView.findViewById<LinearLayout>(R.id.option_delete).setOnClickListener {
            deleteChat(chatItem)
            dialog.dismiss()
        }



        dialog.show()
    }


    private fun markAsUnread(chatItem: ChatItem) {
        // Logic to mark the chat as unread
    }

    private fun pinChat(chatItem: ChatItem) {
        // Logic to pin the chat
    }

    private fun muteChat(chatItem: ChatItem) {
        // Logic to mute the chat
    }

    private fun selectChat(chatItem: ChatItem) {
        // Logic to select the chat
    }

    private fun archiveChat(chatItem: ChatItem) {
        // Logic to archive the chat
    }

    private fun deleteChat(chatItem: ChatItem) {
        // Logic to delete the chat
    }


    private fun Int.dpToPx(): Int {
        return (this * resources.displayMetrics.density).toInt()
    }

    override fun onResume() {
        super.onResume()
        // Set the toolbar and status bar colors when the ChatFragment becomes active
        setupRecyclerViewScrollListener()

        // Optional: Set default colors for the toolbar and status bar on resume
        binding.toolbarChat.setBackgroundColor(
            ContextCompat.getColor(requireContext(), R.color.white)
        )
        activity?.window?.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.white)
        // Set navigation bar color when this fragment becomes active
        requireActivity().window.navigationBarColor = ContextCompat.getColor(requireContext(), R.color.blue_light)
    }

    override fun onPause() {
        super.onPause()
        // Reset the toolbar and status bar to their original colors when leaving the ChatFragment
        binding.toolbarChat.setBackgroundColor(
            ContextCompat.getColor(requireContext(), R.color.white) // Set to your app's default toolbar color
        )
        activity?.window?.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.white) // Set to your app's default status bar color

        // Reset or change navigation bar color when leaving the fragment
        requireActivity().window.navigationBarColor = ContextCompat.getColor(requireContext(), com.google.zxing.client.android.R.color.zxing_transparent)
    }


    override fun onDestroyView() {
        super.onDestroyView()

        requireActivity().window.navigationBarColor = ContextCompat.getColor(requireContext(), com.google.zxing.client.android.R.color.zxing_transparent)

        _binding = null
    }
}
