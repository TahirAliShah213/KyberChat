package com.tahirdev.kyberchat.ui.Contacts

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.GridView
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.Nullable
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.zxing.integration.android.IntentIntegrator
import com.tahirdev.kyberchat.R
import com.tahirdev.kyberchat.databinding.FragmentContactsBinding
import com.tahirdev.kyberchat.ui.Chat.emojis.EmojiAdapter
import com.tahirdev.kyberchat.ui.Chat.emojis.EmojiGridAdapter


class ContactsFragment : Fragment() {

    var bottomSheetSearch : Boolean = false
    lateinit var linear: LinearLayout
    private lateinit var params: ViewGroup.MarginLayoutParams // Declare params globally

    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!

    private lateinit var contactsAdapter: ContactsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel = ViewModelProvider(this).get(ContactsViewModel::class.java)

        _binding = FragmentContactsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        contactsAdapter = ContactsAdapter()

        // Floating Action Button to open MessageFragment
        binding.floatingAddContacts.setOnClickListener {
            addContactDialog()
        }



        requireActivity().window.navigationBarColor = ContextCompat.getColor(requireContext(), R.color.blue_light)


        /*  binding.contactsRecyclerView.apply {
              layoutManager = LinearLayoutManager(context)
              adapter = contactsAdapter
          }*/

        // Set up the search EditText listener
      /*  binding.searchEdit.addTextChangedListener { text ->
            contactsAdapter.filter(text.toString()) // Call filter on text change
        }*/

      /*  // Check for contact permission
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS)
            == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission granted, fetch contacts
            fetchContacts()
        } else {
            // Request permission if not granted
            requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
        }*/

    /*    binding.scanner.setOnClickListener(){
            val intentIntegrator = IntentIntegrator(requireActivity())
            intentIntegrator.setOrientationLocked(false)
            intentIntegrator.setPrompt("Scan a QR Code")
            intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            intentIntegrator.initiateScan()
        }*/

        // Set the screen orientation to portrait from a fragment
    //    activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        return root
    }



    // Launcher for permission request
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                fetchContacts()
            } else {
                Log.e("ContactsFragment", "Permission denied")
                // Handle permission denial case
            }
        }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        @Nullable data: Intent?
    ) {
        val intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (intentResult != null) {
            val contents = intentResult.contents
            if (contents != null) {
            //    binding.textView.setText(intentResult.contents)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }



    // Function to fetch contacts if permission is granted
    private fun fetchContacts() {
        val contacts = getPhoneContacts()
        Log.d("ContactsFragment", "Fetched ${contacts.size} contacts")
        contactsAdapter.submitList(groupContactsByInitial(contacts))
    }

    // Function to group contacts by their initial letter
    private fun groupContactsByInitial(contacts: List<Contact>): List<SectionItem> {
        return contacts.groupBy { it.name.first().uppercase() }
            .flatMap { (initial, groupedContacts) ->
                val sectionHeader = SectionItem(isHeader = true, letter = initial)
                val contactItems = groupedContacts.map { SectionItem(isHeader = false, contact = it) }
                listOf(sectionHeader) + contactItems
            }
    }


    // Function to fetch phone contacts
    private fun getPhoneContacts(): List<Contact> {
        val contactsList = mutableListOf<Contact>()
        val contentResolver = requireContext().contentResolver

        // Query the phone contacts
        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )

        cursor?.use {
            val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)

            // Iterate over the cursor to fetch all contacts
            while (it.moveToNext()) {
                val name = it.getString(nameIndex)
                Log.d("ContactsFragment", "Fetched contact: $name")
                contactsList.add(Contact(name))
            }
        }

        cursor?.close() // Always close the cursor to free resources
        return contactsList
    }


    private fun addContactDialog() {
        val dialogView = layoutInflater.inflate(R.layout.add_contacts_dialog, null)
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.CustomBottomSheetDialogTheme)

      //  val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(dialogView)


        val add_contact = dialogView.findViewById<TextView>(R.id.addContactBtn)
        val cancel = dialogView.findViewById<TextView>(R.id.cancelBtnText)


        add_contact.setOnClickListener {
            val dialogView =
                LayoutInflater.from(requireContext()).inflate(R.layout.add_new_contact_dialog, null)

            val dialog = AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .create()

            val window = dialog.window
            window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            window?.setBackgroundDrawableResource(android.R.color.transparent)

            // Delete option
            dialogView.findViewById<TextView>(R.id.cancelBtn).setOnClickListener {
                dialog.dismiss()
            }

            bottomSheetDialog.dismiss()
            dialog.show()

        }

        cancel.setOnClickListener(){
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
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
