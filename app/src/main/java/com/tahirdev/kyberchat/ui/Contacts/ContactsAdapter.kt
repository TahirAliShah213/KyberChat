package com.tahirdev.kyberchat.ui.Contacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tahirdev.kyberchat.R

class ContactsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var sectionedContactList: List<SectionItem> = emptyList() // Current filtered list
    private var originalContactList: List<SectionItem> = emptyList() // Original list for filtering

    // Method to submit the list of sectioned contacts
    fun submitList(contacts: List<SectionItem>) {
        originalContactList = contacts // Store the original list
        sectionedContactList = contacts // Initialize current list
        notifyDataSetChanged() // Notify adapter of the change
    }

    // Method to filter contacts based on the search query
    fun filter(query: String) {
        sectionedContactList = if (query.isEmpty()) {
            originalContactList // If the query is empty, show the original list
        } else {
            originalContactList.filter { sectionItem ->
                if (sectionItem.isHeader) {
                    false // Don't include headers in filtering
                } else {
                    sectionItem.contact?.name?.contains(query, ignoreCase = true) == true
                }
            }
        }
        notifyDataSetChanged() // Refresh the RecyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) { // Header view type
            val headerView = LayoutInflater.from(parent.context)
                .inflate(R.layout.section_header_item, parent, false) // Layout for section header
            SectionHeaderViewHolder(headerView)
        } else { // Contact view type
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.contacts_item, parent, false)  // Layout for contact item
            ContactViewHolder(itemView)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val sectionItem = sectionedContactList[position]
        if (sectionItem.isHeader) {
            // Bind header
            (holder as SectionHeaderViewHolder).bind(sectionItem.letter!!)
        } else {
            // Bind contact
            (holder as ContactViewHolder).bind(sectionItem.contact!!)
        }
    }

    override fun getItemCount(): Int = sectionedContactList.size // Return sectioned list size

    override fun getItemViewType(position: Int): Int {
        return if (sectionedContactList[position].isHeader) 0 else 1 // 0 for header, 1 for contact
    }

    // ViewHolder for contact items
    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val profileLetter: TextView = itemView.findViewById(R.id.profileLetter)
        private val nameTextView: TextView = itemView.findViewById(R.id.contact_name)

        // Bind contact details to the views
        fun bind(contact: Contact) {
            // Set the first letter of the name in the profile letter TextView
            profileLetter.text = contact.name.first().toString()
            // Set the contact name
            nameTextView.text = contact.name
        }
    }

    // ViewHolder for section header items
    class SectionHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val headerTextView: TextView = itemView.findViewById(R.id.section_header)

        // Bind header details to the views
        fun bind(letter: String) {
            headerTextView.text = letter
        }
    }
}
