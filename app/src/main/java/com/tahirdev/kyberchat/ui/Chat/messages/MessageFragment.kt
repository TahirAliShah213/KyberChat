package com.tahirdev.kyberchat.ui.Chat.messages

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.GridView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tahirdev.kyberchat.MainActivity
import com.tahirdev.kyberchat.R
import com.tahirdev.kyberchat.databinding.FragmentMessageBinding
import com.tahirdev.kyberchat.ui.Chat.chat_main.Message
import com.tahirdev.kyberchat.ui.Chat.emojis.EmojiAdapter
import com.tahirdev.kyberchat.ui.Chat.emojis.EmojiGridAdapter
import com.tahirdev.kyberchat.ui.Chat.messages.allmedias.AllMediaActivity
import com.tahirdev.kyberchat.ui.Chat.messages.allmedias.DisMsgFragment
import java.io.IOException

class MessageFragment : Fragment() {

    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: MutableList<Message>
    var bottomSheetSearch : Boolean = false
    lateinit var linear: LinearLayout
    private lateinit var params: ViewGroup.MarginLayoutParams // Declare params globally
    private var isSendButtonVisible: Boolean = false

    private lateinit var recentEmojisAdapter: EmojiAdapter
    private lateinit var emojiGridAdapter: EmojiGridAdapter
    private val recentEmojis = mutableListOf<String>()

    private val smileyEmojis = listOf("😀", "😁", "😂", "😃", "😄", "😅", "😆", "😉", "😊", "😍")
    private val animalEmojis = listOf("🐶", "🐱", "🐭", "🐰", "🐻", "🐼", "🐨", "🐯", "🦁", "🐮")
    private lateinit var chatRecyclerView : RecyclerView
    private val RECORD_AUDIO_REQUEST_CODE = 101
    private var isRecording = false
    private lateinit var mediaRecorder: MediaRecorder
    private lateinit var audioFilePath: String
    private var mediaPlayer: MediaPlayer? = null
    private var isLocked : Boolean = false
    val swipeUpThreshold = -100f
    private val REQUEST_CODE_GALLERY = 100

    private var _binding: FragmentMessageBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMessageBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Set up the back button
        val backBtn: ImageButton = binding.backBtn
        backBtn.setOnClickListener {
           findNavController().navigate(R.id.action_messageFragment_to_new_messageFragment)
        }

        chatRecyclerView = binding.chatRecyclerView
        val messageInput = binding.messageEdit
        val sendButton = binding.micBtn
        val plusBtn = binding.plusBtn
        val emojisBtn = binding.emojisBtn
        val lockIcon = binding.lockIcon

        linear = binding.linearLayoutSearch
        params = linear.layoutParams as ViewGroup.MarginLayoutParams


        // Initialize message list and adapter
        messageList = mutableListOf()
        messageAdapter = MessageAdapter(messageList)
        chatRecyclerView.adapter = messageAdapter
        chatRecyclerView.layoutManager = LinearLayoutManager(requireContext())


        // Add a TextWatcher to handle the mic/send button toggle
        messageInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Check if the EditText has text
                if (s.toString().trim().isNotEmpty()) {
                    // Show send button
                    if (!isSendButtonVisible) {
                        sendButton.setImageResource(R.drawable.send_btn) // Change mic to send icon
                        isSendButtonVisible = true
                    }
                } else {
                    // Show mic button
                    if (isSendButtonVisible) {
                        sendButton.setImageResource(R.drawable.mic) // Change send to mic icon
                        isSendButtonVisible = false
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Handle sending messages
        sendButton.setOnClickListener {
            val messageText = messageInput.text.toString().trim()

            if (messageText.isNotEmpty()) {
                // Handle text message sending
                messageList.add(Message(messageText, true, System.currentTimeMillis()))
                messageAdapter.notifyItemInserted(messageList.size - 1)
                chatRecyclerView.scrollToPosition(messageList.size - 1)

                // Simulate a reply after a delay
                chatRecyclerView.postDelayed({
                    messageList.add(Message("This is a reply", false, System.currentTimeMillis()))
                    messageAdapter.notifyItemInserted(messageList.size - 1)
                    chatRecyclerView.scrollToPosition(messageList.size - 1)
                }, 1000)

                messageInput.text?.clear() // Clear input field
            }
        }

        binding.linearLayoutSearch.viewTreeObserver.addOnGlobalLayoutListener(
            ViewTreeObserver.OnGlobalLayoutListener {
                val r = Rect()
                binding.linearLayoutSearch.getWindowVisibleDisplayFrame(r)
                val screenHeight = binding.linearLayoutSearch.rootView.height
                val keypadHeight = screenHeight - r.bottom

                // Check if the keyboard is shown
                if (keypadHeight > screenHeight * 0.15) {
                    // Keyboard is visible
                    binding.cameraBtn.visibility = View.GONE // Hide camera button
                } else {
                    // Keyboard is hidden
                    binding.cameraBtn.visibility = View.VISIBLE // Show camera button
                }
            })


        // Handle mic button functionality for voice recording
       /* sendButton.setOnTouchListener { view, motionEvent ->
            if (messageInput.text?.isEmpty() == true) {
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        if (checkMicrophonePermissions()) {
                            // Zoom in mic button and show lock icon (start recording)
                            sendButton.startLargeRecordingAnimation()
                            lockIcon.showLockIcon()
                            startVoiceRecording()
                        } else {
                            requestMicrophonePermissions()
                        }
                        true
                    }

                    MotionEvent.ACTION_MOVE -> {
                        val y = motionEvent.y  // Get the vertical movement

                        if (y < swipeUpThreshold && !isLocked) {
                            // User swiped up, lock the recording
                            sendButton.lockRecording()
                            lockIcon.showLockIcon() // Show lock icon
                        }
                        true
                    }

                    MotionEvent.ACTION_UP -> {
                        if (isRecording && !isLocked) {
                            // Stop recording if not locked and zoom out mic button
                            sendButton.stopRecordingAnimation()
                            lockIcon.hideLockIcon()
                            stopVoiceRecordingAndSendMessage()
                        } else if (isLocked) {
                            // If locked, only hide lock icon when manually stopping
                            sendButton.stopRecordingAnimation()
                            lockIcon.hideLockIcon()
                            stopVoiceRecordingAndSendMessage()  // Optionally call stop based on manual action
                        }
                        true
                    }

                    else -> false
                }
            } else {
                false // Let the OnClickListener handle text input
            }
        }*/





        // Set up the plus button to trigger the bottom sheet

        emojisBtn.setOnClickListener(){
            showEmojiDialog()

        }



        plusBtn.setOnClickListener {
            toggleBottomSheet()
        }


        binding.dropdownMenu.setOnClickListener(){
            showOptionsPopup()
        }


        return root
    }

    fun ImageButton.startLargeRecordingAnimation() {
        this.animate()
            .scaleX(1.6f)
            .scaleY(1.6f)
            .setDuration(150)
            .start()
    }

    fun ImageButton.stopRecordingAnimation() {
        this.animate()
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(150)
            .start()
    }

    fun ImageButton.lockRecording() {
        // No additional zoom or scale here since it's already zoomed in
        isLocked = true
    }

    fun ImageView.showLockIcon() {
        this.visibility = View.VISIBLE
        this.animate()
            .alpha(1f)
            .setDuration(150)
            .start()
    }

    fun ImageView.hideLockIcon() {
        this.animate()
            .alpha(0f)
            .setDuration(150)
            .withEndAction { this.visibility = View.GONE }
            .start()
    }






    private fun showEmojiDialog() {
        val dialogView = layoutInflater.inflate(R.layout.emoji_dialog, null)
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(dialogView)

        // Set up Recent Emojis RecyclerView
        val recentEmojisRecycler = dialogView.findViewById<RecyclerView>(R.id.recycler_recent_emojis)
        recentEmojisAdapter = EmojiAdapter(recentEmojis) // Adapter for recent emojis
        recentEmojisRecycler.adapter = recentEmojisAdapter
        recentEmojisRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)


        val emojiGrid = dialogView.findViewById<GridView>(R.id.grid_emojis)
        emojiGridAdapter = EmojiGridAdapter(smileyEmojis) { selectedEmoji ->
            addToRecentEmojis(selectedEmoji)
        }
        emojiGrid.adapter = emojiGridAdapter


        var selectedButton: ImageButton? = null

        fun selectButton(button: ImageButton) {
            // Reset previous button color if one is selected
            selectedButton?.setColorFilter(ContextCompat.getColor(requireContext(), R.color.gray))

            // Set the selected button color to blue
            button.setColorFilter(ContextCompat.getColor(requireContext(), R.color.blue))

            // Update selectedButton to the current one
            selectedButton = button
        }


        val btnSmileys = dialogView.findViewById<ImageButton>(R.id.btn_smileys)
        val btnAnimals = dialogView.findViewById<ImageButton>(R.id.btn_animals)

        btnSmileys.setOnClickListener {
            emojiGridAdapter.updateEmojis(smileyEmojis)
            selectButton(btnSmileys)
        }

        btnAnimals.setOnClickListener {
            emojiGridAdapter.updateEmojis(animalEmojis)
            selectButton(btnAnimals)
        }

        bottomSheetDialog.show()
    }


    private fun addToRecentEmojis(emoji: String) {
        if (!recentEmojis.contains(emoji)) {
            if (recentEmojis.size >= 10) {
                recentEmojis.removeAt(0)
            }
            recentEmojis.add(emoji)
            recentEmojisAdapter.notifyDataSetChanged() // Refresh the recent emojis view
        }
    }






    private fun toggleBottomSheet() {
        // Toggle bottom sheet visibility state
        if (!bottomSheetSearch) {
            // Set larger bottom margin when the bottom sheet is open (assuming 416px is the height of the sheet)
            params.setMargins(params.leftMargin, params.topMargin, params.rightMargin, 416)
            linear.layoutParams = params
            bottomSheetSearch = true
        } else {
            // Reset margins when the bottom sheet is dismissed
            params.setMargins(params.leftMargin, params.topMargin, params.rightMargin, 0)
            linear.layoutParams = params
            bottomSheetSearch = false
        }

        showBottomSheetDialog()
    }

    @SuppressLint("IntentReset")
    private fun showBottomSheetDialog() {
        // Create BottomSheetDialog
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.CustomBottomSheetDialogTheme)

    //    val bottomSheetDialog = BottomSheetDialog(requireContext())
        val sheetView = layoutInflater.inflate(R.layout.layout_bottom_sheet, null)

        bottomSheetDialog.setContentView(sheetView)
    //    sheetView.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        // Handle dismissal event
        bottomSheetDialog.setOnDismissListener {
            // Reset margins to 0 when bottom sheet is dismissed
            params.setMargins(params.leftMargin, params.topMargin, params.rightMargin, 0)
            linear.layoutParams = params
            bottomSheetSearch = false
        }

        // Listen for state changes in the BottomSheet
        val bottomSheetBehavior = BottomSheetBehavior.from(sheetView.parent as View)
        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        // Adjust layout when the bottom sheet is fully expanded
                        params.setMargins(params.leftMargin, params.topMargin, params.rightMargin, 416)
                        linear.layoutParams = params
                    }
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        // Reset margins when the bottom sheet is hidden
                        params.setMargins(params.leftMargin, params.topMargin, params.rightMargin, 0)
                        linear.layoutParams = params
                        bottomSheetSearch = false
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // Optionally handle sliding behavior
            }
        })

        // Set click listeners for each option
        sheetView.findViewById<View>(R.id.option_gallery).setOnClickListener {
            bottomSheetDialog.dismiss()
            // Intent to open gallery
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*" // Specify the type of files you want to pick (images in this case)
            startActivityForResult(intent, REQUEST_CODE_GALLERY)
        }
        sheetView.findViewById<View>(R.id.option_file).setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        sheetView.findViewById<View>(R.id.option_contact).setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        sheetView.findViewById<View>(R.id.option_location).setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        // Show the BottomSheetDialog
        bottomSheetDialog.show()
        bottomSheetSearch = true
    }


    private fun checkMicrophonePermissions(): Boolean {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestMicrophonePermissions() {
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.RECORD_AUDIO), RECORD_AUDIO_REQUEST_CODE)
    }



    private fun startVoiceRecording() {
        audioFilePath = "${requireContext().externalCacheDir?.absolutePath}/voiceNote.3gp"

        try {
            mediaRecorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                setOutputFile(audioFilePath)

                try {
                    prepare()
                    start()
                    isRecording = true  // Recording successfully started
                    Toast.makeText(context, "Recording started", Toast.LENGTH_SHORT).show()  // Log success
                } catch (e: IOException) {
                    e.printStackTrace()
                    isRecording = false
                    Toast.makeText(context, "Failed to start recording: ${e.message}", Toast.LENGTH_SHORT).show()  // Log error
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            isRecording = false
            Toast.makeText(context, "Error initializing recorder: ${e.message}", Toast.LENGTH_SHORT).show()  // Log error
        }
    }

    private fun stopVoiceRecordingAndSendMessage() {
        if (isRecording) {
            try {
                mediaRecorder.stop() // Stop recording
                mediaRecorder.release()
                isRecording = false  // Reset the flag
                Toast.makeText(context, "Recording stopped", Toast.LENGTH_SHORT).show()  // Log success

                // Add the voice message to your list
                messageList.add(Message(audioFilePath, true, System.currentTimeMillis(), isVoiceNote = true))
                messageAdapter.notifyItemInserted(messageList.size - 1)
                chatRecyclerView.scrollToPosition(messageList.size - 1)

            } catch (e: RuntimeException) {
                e.printStackTrace()
                Toast.makeText(context, "Error stopping recording: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Not recording, cannot stop.", Toast.LENGTH_SHORT).show()  // Log "not recording" case
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_GALLERY && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri: Uri? = data.data
            if (selectedImageUri != null) {
                // Do something with the selected image URI (e.g., display it or upload it)
                val imagePath = selectedImageUri.toString()
                // Use the image path as needed
            }
        }
    }



    private fun showOptionsPopup() {
        val popupView = LayoutInflater.from(requireContext()).inflate(R.layout.menu_options_dialog, null)
        val popupWindow = PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)

        // Ensure the PopupWindow has no background
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Get the location of the toolbar
        val toolbar = binding.toolbarMessage
        val location = IntArray(2)
        toolbar.getLocationOnScreen(location)

        // Display the popup window at the bottom-right of the toolbar
        popupWindow.showAtLocation(toolbar, Gravity.NO_GRAVITY, location[0] + toolbar.width - popupWindow.width, location[1] + toolbar.height)

        // Set click listeners for popup options
        popupView.findViewById<TextView>(R.id.allMedia).setOnClickListener {

            val intent = Intent(requireContext(), AllMediaActivity::class.java)
            startActivity(intent)

            popupWindow.dismiss()
        }
        popupView.findViewById<TextView>(R.id.search).setOnClickListener {
            // Handle search click
            popupWindow.dismiss()
        }
        popupView.findViewById<TextView>(R.id.chatSetting).setOnClickListener {

            findNavController().navigate(R.id.action_messageFragment_to_chatSettingFragment)

            popupWindow.dismiss()  // Close the popup
        }
        popupView.findViewById<TextView>(R.id.deleteChat).setOnClickListener {

            val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.delete_all_chat_dialog, null)

            val dialog = AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .create()

            val window = dialog.window
            window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            window?.setBackgroundDrawableResource(android.R.color.transparent)


            dialogView.findViewById<TextView>(R.id.deleteAllChat).setOnClickListener {

                dialog.dismiss()
            }

            dialog.show()


            popupWindow.dismiss()
        }



        popupView.findViewById<TextView>(R.id.disMesg).setOnClickListener {

            findNavController().navigate(R.id.action_messageFragment_to_disappearingMsg)

            popupWindow.dismiss()  // Close the popup
        }




        popupView.findViewById<TextView>(R.id.block).setOnClickListener {
            val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.block_dialog, null)

            val dialog = AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .create()

            val window = dialog.window
            window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            window?.setBackgroundDrawableResource(android.R.color.transparent)


            dialogView.findViewById<TextView>(R.id.blockBtn).setOnClickListener {

                dialog.dismiss()
            }

            dialog.show()


            popupWindow.dismiss()
        }
        popupView.findViewById<TextView>(R.id.mute).setOnClickListener {
            val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.mute_notifications_dialog, null)

            val dialog = AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .create()

            val window = dialog.window
            window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            window?.setBackgroundDrawableResource(android.R.color.transparent)


            dialogView.findViewById<TextView>(R.id.mute1hr).setOnClickListener {

                dialog.dismiss()
            }

            dialog.show()


            popupWindow.dismiss()

        }
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

