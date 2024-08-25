package com.example.farmereats.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.farmereats.R
import com.example.farmereats.databinding.FragmentLoginBinding
import com.example.farmereats.models.Login
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels()
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private lateinit var callbackManager: CallbackManager
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentLoginBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        callbackManager = CallbackManager.Factory.create()
        auth = FirebaseAuth.getInstance()
        sharedPreferences =
            requireActivity().getSharedPreferences("com.example.farmereats", Context.MODE_PRIVATE)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        val handler = Handler(Looper.getMainLooper())
        viewModel.errorMsg.observe(viewLifecycleOwner) { error ->
            if (error.isNotEmpty()) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                if (error == "Please Enter Email!") {
                    binding.enterEmailText.text = error
                    binding.enterEmailText.visibility = View.VISIBLE
                } else if (error == "Please Enter Password!") {
                    binding.enterPasswordText.text = error
                    binding.enterPasswordText.visibility = View.VISIBLE
                } else if (error == "Invalid password.") {
                    binding.forgetPasswordBtn.visibility = View.VISIBLE
                }

                handler.postDelayed({
                    binding.enterEmailText.visibility = View.GONE
                    binding.enterPasswordText.visibility = View.GONE
                    binding.forgetPasswordBtn.visibility = View.GONE
                }, 3000)

                viewModel.clearErrorMsg()
            }
        }

        viewModel.loginResponse.observe(viewLifecycleOwner) { response ->
            Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
            if (response.success == "true") {
                sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            }
        }

        binding.bigLoginBtn.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val isValid = true
            if (isValid) {
                val loginRequest = Login(
                    device_token = "",
                    email = email,
                    password = password,
                    role = "farmer",
                    social_id = "",
                    type = "email"
                )
                viewModel.login(loginRequest)
            }
        }

        binding.forgetPasswordBtn.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgetPasswordFragment)
        }

        binding.createAccountBtn.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }

        binding.appleLoginBtn.setOnClickListener {
            Toast.makeText(requireContext(), "Not Tested Yet!", Toast.LENGTH_SHORT).show()
        }
        binding.googleLoginBtn.setOnClickListener {
            googleSignInClient.signOut()
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        val facebookLoginButton: Button = binding.facebookLoginBtn
        facebookLoginButton.setOnClickListener {
            LoginManager.getInstance()
                .logInWithReadPermissions(this, listOf("email", "public_profile"))
        }

        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    handleFacebookAccessToken(loginResult.accessToken.token)
                }

                override fun onCancel() {
                    Toast.makeText(requireContext(), "Login Cancelled", Toast.LENGTH_SHORT).show()
                }

                override fun onError(error: FacebookException) {
                    Toast.makeText(
                        requireContext(),
                        "Login Error: ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

        return binding.root
    }


    fun handleFacebookAccessToken(token: String) {
        val credential = FacebookAuthProvider.getCredential(token)
        auth.signInWithCredential(credential).addOnCompleteListener { authTask ->
            if (authTask.isSuccessful) {
                sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
                Toast.makeText(requireActivity(), "Signed in with Facebook", Toast.LENGTH_SHORT)
                    .show()
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            } else {
                Toast.makeText(
                    requireActivity(),
                    "Sign-in failed: ${authTask.exception?.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    firebaseAuthWithGoogle(account)
                }
            } catch (e: ApiException) {
                Toast.makeText(requireContext(), "Google Sign-In failed", Toast.LENGTH_SHORT).show()
            }
        }
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener { authTask ->
            if (authTask.isSuccessful) {
                sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
                Toast.makeText(requireActivity(), "Signed in with Google", Toast.LENGTH_SHORT)
                    .show()
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            } else {
                Toast.makeText(
                    requireActivity(),
                    "Sign-in failed: ${authTask.exception?.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }
}
