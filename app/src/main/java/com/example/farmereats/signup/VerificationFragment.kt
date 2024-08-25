package com.example.farmereats.signup

import android.os.Bundle
import android.provider.MediaStore
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.farmereats.R
import com.example.farmereats.databinding.FragmentVerificationBinding

class VerificationFragment : Fragment() {
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var binding: FragmentVerificationBinding

    private val getContent: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                val fileName = getFileName(it)
                applyUnderlineToText(fileName)
                showAttachmentUI()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVerificationBinding.inflate(inflater)

        binding.attachBtn.setOnClickListener {
            getContent.launch("*/*")
        }

        binding.closeBtn.setOnClickListener {
            removeAttachment()
        }

        binding.bigContinueBtn.setOnClickListener {
            val newAttachment = binding.fileNameTextView.text.toString()
            val currentUser = sharedViewModel.newUser.value
            val updatedUser = currentUser?.copy(
                registration_proof = newAttachment
            )
            updatedUser?.let {
                sharedViewModel.updateUser(it)
                findNavController().navigate(R.id.action_verificationFragment_to_businessHoursFragment)
            }
        }
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

    private fun getFileName(uri: android.net.Uri): String {
        val cursor = requireActivity().contentResolver.query(uri, null, null, null, null)
        return cursor?.use {
            it.moveToFirst()
            val nameIndex = it.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
            it.getString(nameIndex)
        } ?: uri.lastPathSegment ?: "Unknown file"
    }

    private fun applyUnderlineToText(text: String) {
        val spannableString = SpannableString(text)
        spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, 0)
        binding.fileNameTextView.text = spannableString
    }

    private fun showAttachmentUI() {
        binding.fileNameTextView.visibility = View.VISIBLE
        binding.closeBtn.visibility = View.VISIBLE
    }

    private fun removeAttachment() {
        binding.fileNameTextView.text = ""
        binding.fileNameTextView.visibility = View.GONE
        binding.closeBtn.visibility = View.GONE
    }
}
