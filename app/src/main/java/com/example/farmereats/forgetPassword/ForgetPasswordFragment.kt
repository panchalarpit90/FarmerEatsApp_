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
import com.example.farmereats.databinding.FragmentForgetPasswordBinding
import com.example.farmereats.models.Number
import com.example.farmereats.forgetPassword.viewmodel.ForgetPasswordViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgetPasswordFragment : Fragment() {

    private val viewModel: ForgetPasswordViewModel by viewModels()
    private lateinit var binding: FragmentForgetPasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForgetPasswordBinding.inflate(inflater, container, false)

        val handler = Handler(Looper.getMainLooper())

        viewModel.errorMsg.observe(viewLifecycleOwner) { error ->
            if (error.isNotEmpty()) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                if (error == "Please Enter Phone Number!") {
                    binding.enterNumberText.text = error
                    binding.enterNumberText.visibility = View.VISIBLE

                    handler.postDelayed({
                        binding.enterNumberText.visibility = View.GONE
                    }, 2500)

                    viewModel.clearErrorMsg()
                }
            }
        }

        binding.sendCodeBtn.setOnClickListener {
            val phoneNumber = binding.phoneEditText.text.toString()
            val number = Number(phoneNumber)
            viewModel.forgetPasswordFun(number)

        }

        viewModel.getResponse.observe(viewLifecycleOwner) { response ->
            Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
            if (response.success == "true") {
                findNavController().navigate(R.id.action_forgetPasswordFragment_to_verrifyFragment)
            }

        }
        binding.loginBtnCo.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }
}
