package com.example.farmereats.forgetPassword

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.farmereats.R
import com.example.farmereats.databinding.FragmentResetPasswordBinding
import com.example.farmereats.forgetPassword.viewmodel.ResetPasswordFragmentViewModel
import com.example.farmereats.models.PasswordReset
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ResetPasswordFragment : Fragment() {
    private val viewModel: ResetPasswordFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentResetPasswordBinding.inflate(inflater)
        val nToken = ResetPasswordFragmentArgs.fromBundle(requireArguments()).getToken

        val handler = Handler(Looper.getMainLooper())
        viewModel.errorMsg.observe(viewLifecycleOwner) { error ->
            if (error.isNotEmpty()) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                if (error == "Please Enter New Password!") {
                    binding.enterPasswordText.text = error
                    binding.enterPasswordText.visibility = View.VISIBLE
                } else if (error == "Please Confirm Your Password!") {
                    binding.enterCPasswordText.text = error
                    binding.enterCPasswordText.visibility = View.VISIBLE
                }

                handler.postDelayed({
                    binding.enterPasswordText.visibility = View.GONE
                    binding.enterCPasswordText.visibility = View.GONE
                }, 2500)

                viewModel.clearErrorMsg()
            }
        }

        viewModel.getResponse.observe(viewLifecycleOwner) { response ->
            Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
            if (response.success == "true") {
                findNavController().navigate(R.id.action_resetPasswordFragment_to_loginFragment)
            }
        }

        binding.bigSubmitBtnReset.setOnClickListener {
            val password = binding.newPasswordEditText.text.toString()
            val cPassword = binding.confrimPasswordEditText.text.toString()
            val isValid = true
            if (isValid) {
                val setPassword = PasswordReset(
                    password = password,
                    cpassword = cPassword,
                    token = nToken
                )
                viewModel.setNewPassword(setPassword)
            }
        }
        binding.loginBtnGo.setOnClickListener {
            findNavController().navigate(R.id.action_resetPasswordFragment_to_loginFragment)
        }

        return binding.root
    }
}
