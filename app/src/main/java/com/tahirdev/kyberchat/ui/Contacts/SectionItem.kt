package com.tahirdev.kyberchat.ui.Contacts

data class SectionItem(
    val isHeader: Boolean,  // Indicates if this item is a header
    val contact: Contact? = null,  // The contact associated with this item (if it's not a header)
    val letter: String? = null  // The letter for the section header (if it's a header)
)
