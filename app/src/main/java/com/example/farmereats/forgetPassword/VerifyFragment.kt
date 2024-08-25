package com.example.farmereats.forgetPassword

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.farmereats.R
import com.example.farmereats.databinding.FragmentVerrifyBinding
import com.example.farmereats.forgetPassword.viewmodel.VerifyViewModel
import com.example.farmereats.models.OtpToken
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VerifyFragment : Fragment() {
    private val viewModel: VerifyViewModel by viewModels()
    private lateinit var binding: FragmentVerrifyBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVerrifyBinding.inflate(inflater, container, false)
        binding.resendTextBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.loginBtnPo.setOnClickListener {
            findNavController().navigate(R.id.action_verrifyFragment_to_loginFragment)
        }
        configOtpEditText(
            binding.code1,
            binding.code2,
            binding.code3,
            binding.code4,
            binding.code5,
            binding.code6
        )

        val otp = createOtp(
            binding.code1.text.toString(),
            binding.code2.text.toString(),
            binding.code3.text.toString(),
            binding.code4.text.toString(),
            binding.code5.text.toString(),
            binding.code6.text.toString()
        )
        binding.bigSubmitBtn.setOnClickListener {
            val otpToken = OtpToken(otp)
            viewModel.getOtpFun(otpToken)
            Log.d("OtpData", otp)
        }

        val handler = Handler(Looper.getMainLooper())
        viewModel.errorMsg.observe(viewLifecycleOwner) { error ->
            if (error.isNotEmpty()) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                if (error == "Please Enter OTP!") {
                    binding.enterOtpText.text = error
                    binding.enterOtpText.visibility = View.VISIBLE

                    handler.postDelayed({
                        binding.enterOtpText.visibility = View.GONE
                    }, 2500)

                    viewModel.clearErrorMsg()
                }
            }
        }
        viewModel.getResponse.observe(viewLifecycleOwner) { response ->
            Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
            if (response.success == "true") {
                val action =
                    VerifyFragmentDirections.actionVerrifyFragmentToResetPasswordFragment(otp)
                findNavController().navigate(action)
            }
        }

        return binding.root
    }

    fun configOtpEditText(vararg etList: EditText) {
        val afterTextChanged = { index: Int, e: Editable? ->
            val view = etList[index]
            val text = e.toString()

            when (view.id) {
                etList[0].id -> {
                    if (text.isNotEmpty() && index < etList.size - 1) {
                        etList[index + 1].requestFocus()
                    }
                }

                etList[etList.size - 1].id -> {
                    if (text.isEmpty() && index > 0) {
                        etList[index - 1].requestFocus()
                    }
                }

                else -> {
                    if (text.isNotEmpty() && index < etList.size - 1) {
                        etList[index + 1].requestFocus()
                    } else if (text.isEmpty() && index > 0) {
                        etList[index - 1].requestFocus()
                    }
                }
            }
            false
        }
        etList.forEachIndexed { index, editText ->
            editText.doAfterTextChanged { afterTextChanged(index, it) }
        }
    }


    private fun createOtp(vararg codes: String): String {
        return codes.joinToString(separator = "")
    }
}
