package com.tahirdev.kyberchat.ui.Chat.chat_main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tahirdev.kyberchat.R

class ChatAdapter(private val chatList: MutableList<ChatItem>) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    var onItemLongPress: ((ChatItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_list_item, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chatItem = chatList[position]
        holder.bind(chatItem)

        // Set long press listener
        holder.itemView.setOnLongClickListener {
            onItemLongPress?.invoke(chatItem)
            true // Return true to indicate the long press is handled
        }
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val profileImage: ImageView = itemView.findViewById(R.id.profile_image)
        private val chatName: TextView = itemView.findViewById(R.id.chat_name)
        private val lastMessage: TextView = itemView.findViewById(R.id.last_message)
        private val newMessageCount: TextView = itemView.findViewById(R.id.new_message_count)

        fun bind(chatItem: ChatItem) {
            chatName.text = chatItem.chatName
            lastMessage.text = chatItem.lastMessage

            // Use proper logic to load the image (e.g., using a URL with Glide or a resource ID)
            if (chatItem.profileImageResId != null) {
                profileImage.setImageResource(chatItem.profileImageResId)
            } else {
                profileImage.setImageResource(R.drawable.person) // Default image if no profile image is provided
            }

            if (chatItem.newMessages > 0) {
                newMessageCount.text = chatItem.newMessages.toString()
                newMessageCount.visibility = View.VISIBLE
            } else {
                newMessageCount.visibility = View.GONE
            }
        }
    }
}

data class ChatItem(
    val chatName: String,
    val lastMessage: String,
    val newMessages: Int,
    val profileImageResId: Int? = R.drawable.person
)
