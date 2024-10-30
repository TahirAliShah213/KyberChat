package com.tahirdev.kyberchat.ui.Chat.messages
import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.tahirdev.kyberchat.R
import com.tahirdev.kyberchat.ui.Chat.chat_main.Message
import java.io.IOException

class MessageAdapter(private val messageList: MutableList<Message>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_SENT = 1
    private val VIEW_TYPE_RECEIVED = 2
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_SENT) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.sent_message_item, parent, false)
            SentMessageViewHolder(view,this)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.received_message_item, parent, false)
            ReceivedMessageViewHolder(view,this)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messageList[position]
        if (holder is SentMessageViewHolder) {
            holder.bind(message)
        } else if (holder is ReceivedMessageViewHolder) {
            holder.bind(message)
        }

        // Set long press listener
        holder.itemView.setOnLongClickListener {
            showReactionDialog(holder, position)
            true // Return true to indicate the long press was handled
        }
    }

    override fun getItemCount(): Int = messageList.size

    override fun getItemViewType(position: Int): Int {
        return if (messageList[position].isSentByUser) VIEW_TYPE_SENT else VIEW_TYPE_RECEIVED
    }

    class SentMessageViewHolder(itemView: View, private val adapter: MessageAdapter) : RecyclerView.ViewHolder(itemView) {
        private val messageText: TextView = itemView.findViewById(R.id.message_text)
        private val reactionText: TextView = itemView.findViewById(R.id.reaction_text)
        private val playButton: ImageView = itemView.findViewById(R.id.play_button)
        private val waveAnimation: ImageView = itemView.findViewById(R.id.wave_animation)
        private val linearLayout: LinearLayout = itemView.findViewById(R.id.linearLayout_voice)
        private val timeTickContainer: LinearLayout = itemView.findViewById(R.id.time_tick_container)
        private val messageTextLayout: ConstraintLayout = itemView.findViewById(R.id.message_text_layout)

        fun bind(message: Message) {
            if (message.isVoiceNote) {
                messageText.visibility = View.GONE
                playButton.visibility = View.VISIBLE
                linearLayout.visibility = View.VISIBLE
                waveAnimation.visibility = View.GONE // Hide wave animation initially
            } else {
                messageText.text = message.content
                playButton.visibility = View.GONE

                // Dynamically adjust the layout to show time based on single-line or multi-line message
               /* messageText.viewTreeObserver.addOnGlobalLayoutListener {
                    val constraintSet = ConstraintSet()
                    constraintSet.clone(messageTextLayout)

                    if (messageText.lineCount == 1) {
                        constraintSet.connect(R.id.time_tick_container, ConstraintSet.BOTTOM, R.id.message_text, ConstraintSet.BOTTOM)
                        constraintSet.connect(R.id.time_tick_container, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
                        constraintSet.clear(R.id.time_tick_container, ConstraintSet.TOP)
                    } else {
   *//*                     // Connect to the parent layout's bottom instead of message_text's bottom
                        constraintSet.connect(R.id.time_tick_container, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
                        constraintSet.connect(R.id.time_tick_container, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
                        constraintSet.clear(R.id.time_tick_container, ConstraintSet.TOP)*//*
                        // For multi-line text, position time_tick_container below the message_text
                        constraintSet.connect(R.id.time_tick_container, ConstraintSet.TOP, R.id.message_text, ConstraintSet.BOTTOM)
                        constraintSet.connect(R.id.time_tick_container, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
                        constraintSet.clear(R.id.time_tick_container, ConstraintSet.TOP)
                    }

                    // Apply the updated constraints
                    constraintSet.applyTo(messageTextLayout)
                }
*/


            }

            // Show reaction if available
            if (message.reaction != null && message.reaction!!.isNotEmpty()) {
                reactionText.text = message.reaction
                reactionText.visibility = View.VISIBLE
                linearLayout.visibility = View.VISIBLE
            } else {
                reactionText.visibility = View.GONE
            }

            // Handle click event to play voice message
            if (message.isVoiceNote) {
                playButton.setOnClickListener {
                    // Toggle play/pause
                    if (adapter.mediaPlayer?.isPlaying == true) {
                        adapter.pauseVoiceMessage(waveAnimation, playButton) // Call to pause
                    } else {
                        adapter.playVoiceMessage(itemView.context, message.content, waveAnimation, playButton) // Call to play
                    }
                }
            }
        }
    }


    class ReceivedMessageViewHolder(itemView: View,private val adapter: MessageAdapter) : RecyclerView.ViewHolder(itemView) {
        private val messageText: TextView = itemView.findViewById(R.id.message_text)
        private val reactionText: TextView = itemView.findViewById(R.id.reaction_text)
        private val playButton: ImageView = itemView.findViewById(R.id.play_button)
        private val waveAnimation: ImageView = itemView.findViewById(R.id.wave_animation)




        fun bind(message: Message) {
            messageText.text = message.content
            if (message.isVoiceNote) {
                messageText.text = "Voice Message" // Indicate it's a voice message
                playButton.visibility = View.VISIBLE
                waveAnimation.visibility = View.GONE // Hide wave animation initially
            } else {
                messageText.text = message.content
                playButton.visibility = View.GONE
            }
            // Show reaction if available
            if (message.reaction != null && message.reaction!!.isNotEmpty()) {
                reactionText.text = message.reaction
                reactionText.visibility = View.VISIBLE
            } else {
                reactionText.visibility = View.GONE
            }
            // Handle click event to play voice message
            if (message.isVoiceNote) {
                playButton.setOnClickListener {
                    // Toggle play/pause
                    if (adapter.mediaPlayer?.isPlaying == true) {
                        adapter.pauseVoiceMessage(waveAnimation, playButton) // Call to pause
                    } else {
                        adapter.playVoiceMessage(itemView.context, message.content, waveAnimation, playButton) // Call to play
                    }
                }
            }
        }
    }

    // Function to play the voice message
// Function to play the voice message
    private fun playVoiceMessage(context: Context, audioFilePath: String, waveAnimation: ImageView, playPauseButton: ImageView) {
        try {
            // Release any existing MediaPlayer instance
            mediaPlayer?.release()

            // Initialize and set up MediaPlayer
            mediaPlayer = MediaPlayer().apply {
                setDataSource(audioFilePath) // Set audio file path
                prepare()
                start() // Start playback
            }

            Toast.makeText(context, "Playing voice message...", Toast.LENGTH_SHORT).show()

            // Start wave animation
            waveAnimation.visibility = View.VISIBLE // Make the animation view visible
            val waveAnimationDrawable = waveAnimation.drawable as AnimationDrawable // Cast drawable to AnimationDrawable
            waveAnimationDrawable.start() // Start the animation

            // Change button icon to pause
            playPauseButton.setImageResource(R.drawable.ic_pause) // Replace with your pause icon

            // Release MediaPlayer resources when playback is done
            mediaPlayer?.setOnCompletionListener {
                mediaPlayer?.release()
                mediaPlayer = null
                Toast.makeText(context, "Playback completed", Toast.LENGTH_SHORT).show()

                // Stop wave animation
                waveAnimationDrawable.stop() // Stop the animation
                waveAnimation.visibility = View.GONE // Hide the animation view

                // Reset button icon to play
                playPauseButton.setImageResource(R.drawable.ic_play) // Replace with your play icon
            }

        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(context, "Error playing voice message", Toast.LENGTH_SHORT).show()
        }
    }


    // Remember to release the MediaPlayer when the adapter is no longer in use
    fun releaseMediaPlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    // Function to pause the voice message
    private fun pauseVoiceMessage(waveAnimation: ImageView, playPauseButton: ImageView) {
        mediaPlayer?.pause() // Pause playback

        // Stop wave animation
        val waveAnimationDrawable = waveAnimation.drawable as AnimationDrawable
        waveAnimationDrawable.stop() // Stop the animation
        waveAnimation.visibility = View.GONE // Hide the animation view

        // Change button icon to play
        playPauseButton.setImageResource(R.drawable.ic_play) // Replace with your play icon
    }



    @SuppressLint("ServiceCast")
    private fun showReactionDialog(holder: RecyclerView.ViewHolder, position: Int) {
        // Inflate custom dialog layout
        val context = holder.itemView.context
        val dialogView = LayoutInflater.from(context).inflate(R.layout.message_reaction_dialog, null)
        val alertDialog = AlertDialog.Builder(context).create()

        // Set the window background to transparent programmatically
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)


        // Get the message content to show in the dialog
        val message = messageList[position]

        // Find and set the message content TextView
        val messageTextView = dialogView.findViewById<TextView>(R.id.dialog_message_text)
        messageTextView.text = message.content // Set the message content to the TextView

        // Handle Reactions
        dialogView.findViewById<TextView>(R.id.reaction_like).setOnClickListener {
            // Update message reaction here
            messageList[position].reaction = "👍"
            notifyItemChanged(position)
            alertDialog.dismiss()
        }

        dialogView.findViewById<TextView>(R.id.reaction_love).setOnClickListener {
            message.reaction = "❤️"
            notifyItemChanged(position)
            alertDialog.dismiss()
        }

        // Handle Actions (Copy, Reply, Forward, Delete, Info)
        dialogView.findViewById<LinearLayout>(R.id.action_copy).setOnClickListener {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Message", message.content)
            clipboard.setPrimaryClip(clip)
            alertDialog.dismiss()
        }

        dialogView.findViewById<LinearLayout>(R.id.action_reply).setOnClickListener {
            // Handle reply logic
            alertDialog.dismiss()
        }

        dialogView.findViewById<LinearLayout>(R.id.action_forward).setOnClickListener {
            // Handle forward logic
            alertDialog.dismiss()
        }

        dialogView.findViewById<LinearLayout>(R.id.action_delete).setOnClickListener {
            // Create and show a delete confirmation dialog
            val deleteDialogView = LayoutInflater.from(context).inflate(R.layout.delete_message_dialog, null)
            val deleteAlertDialog = AlertDialog.Builder(context).create()

            // Set transparent background for the delete dialog
            deleteAlertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

            // Handle delete for everyone
            deleteDialogView.findViewById<TextView>(R.id.dialog_deleteforeveryone_message).setOnClickListener {
                if (position in messageList.indices) {
                    // Update the message content to show "This message was deleted"
                    messageList[position].content = "This message was deleted for everyone" // Make sure 'content' is a field in your message model
                    notifyItemChanged(position) // Notify the adapter to refresh this item
                }
                deleteAlertDialog.dismiss()
                alertDialog.dismiss() // Close the initial action dialog
            }

            // Handle delete for me
            deleteDialogView.findViewById<TextView>(R.id.dialog_deleteforme_message).setOnClickListener {
                if (position in messageList.indices) {
                    // Update the message content to show "This message was deleted" (or different behavior for delete for me)
                    messageList[position].content = "This message was deleted"
                    notifyItemChanged(position)
                }
                deleteAlertDialog.dismiss()
                alertDialog.dismiss() // Close the initial action dialog
            }

            // Handle cancel
            deleteDialogView.findViewById<TextView>(R.id.dialog_delete_message_cancel).setOnClickListener {
                deleteAlertDialog.dismiss()
            }

            // Show the delete confirmation dialog
            deleteAlertDialog.setView(deleteDialogView)
            deleteAlertDialog.show()
            alertDialog.dismiss()
        }




        dialogView.findViewById<LinearLayout>(R.id.action_info).setOnClickListener {
            // Create and show a delete confirmation dialog
            val infoDialogView = LayoutInflater.from(context).inflate(R.layout.info_message_dialog, null)
            val infoAlertDialog = AlertDialog.Builder(context).create()

            // Set transparent background for the delete dialog
            infoAlertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)


            // Show the delete confirmation dialog
            infoAlertDialog.setView(infoDialogView)
            infoAlertDialog.show()
            alertDialog.dismiss()
        }

        // Show dialog
        alertDialog.setView(dialogView)
        alertDialog.show()
    }

}


