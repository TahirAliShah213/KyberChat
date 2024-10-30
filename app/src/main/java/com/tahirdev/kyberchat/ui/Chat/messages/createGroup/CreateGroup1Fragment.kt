package com.tahirdev.kyberchat.ui.Chat.messages.createGroup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tahirdev.kyberchat.MainActivity
import com.tahirdev.kyberchat.R
import com.tahirdev.kyberchat.databinding.FragmentCreateGroup1Binding
import com.tahirdev.kyberchat.databinding.FragmentCreateGroupBinding
import com.tahirdev.kyberchat.databinding.FragmentNewMessagesBinding

class CreateGroup1Fragment : Fragment() {

    private var _binding: FragmentCreateGroup1Binding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateGroup1Binding.inflate(inflater, container, false)
        val root: View = binding.root

        val backBtn: ImageView = binding.backBtnCreateGroup1
        backBtn.setOnClickListener {
            findNavController().navigate(R.id.action_createGroupFragment1_to_create_group)
        }

        binding.photoLayout.setOnClickListener() {
            // Change the navigation bar color when the dialog is shown
            val window = requireActivity().window
       //     val originalNavBarColor = window.navigationBarColor
         //   window.navigationBarColor = ContextCompat.getColor(requireContext(), R.color.gray_200)

            val dialogView = layoutInflater.inflate(R.layout.add_photo_dialog_cgroup, null)
            val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.CustomBottomSheetDialogTheme)

            bottomSheetDialog.setContentView(dialogView)

            // Ensure the bottom sheet expands fully
            bottomSheetDialog.behavior.isFitToContents = true
            bottomSheetDialog.behavior.peekHeight = BottomSheetBehavior.PEEK_HEIGHT_AUTO
            dialogView.fitsSystemWindows = true

            val take_photo = dialogView.findViewById<LinearLayout>(R.id.takePhoto)
            take_photo.setOnClickListener {
                bottomSheetDialog.dismiss()
            }

            // Restore the original navigation bar color when the dialog is dismissed
            bottomSheetDialog.setOnDismissListener {
         //       window.navigationBarColor = originalNavBarColor
            }

            bottomSheetDialog.show()
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