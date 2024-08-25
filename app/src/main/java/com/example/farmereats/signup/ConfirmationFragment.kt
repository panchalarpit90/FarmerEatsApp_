package com.example.farmereats.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.farmereats.R
import com.example.farmereats.databinding.FragmentConfirmationBinding

class ConfirmationFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentConfirmationBinding.inflate(inflater)
        binding.bigGotitBtn.setOnClickListener {
            findNavController().navigate(R.id.action_confirmationFragment_to_loginFragment)
        }

        return binding.root
    }


}