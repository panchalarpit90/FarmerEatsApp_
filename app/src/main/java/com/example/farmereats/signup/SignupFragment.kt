package com.example.farmereats.signup

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.farmereats.R
import com.example.farmereats.databinding.FragmentSignupBinding
import com.example.farmereats.utils.AuthCallbacks
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
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class SignupFragment : Fragment(), AuthCallbacks {

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private lateinit var callbackManager: CallbackManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSignupBinding.inflate(inflater)
        val currentUser = sharedViewModel.newUser.value
        callbackManager = CallbackManager.Factory.create()

        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        binding.bigContinueBtn.setOnClickListener {
            val newNam = binding.emailEditText.text.toString()
            val newPassword = binding.passwordEditText.text.toString()
            val confirmPassword = binding.reEnterPasswordEditText.text.toString()
            val newPhone = binding.phoneEditText.text.toString()
            val newEmail = binding.emailEditText.text.toString()

            if (newPassword != confirmPassword) {
                showPasswordMismatchError(binding)
                return@setOnClickListener
            }

            val updatedUser = currentUser?.copy(
                full_name = newNam,
                password = newPassword,
                phone = newPhone,
                email = newEmail
            )
            updatedUser?.let {
                sharedViewModel.updateUser(it)
                findNavController().navigate(R.id.action_signupFragment_to_framInfoFragment)
            }
        }
        binding.appleLoginBtn.setOnClickListener {
            Toast.makeText(requireContext(), "Not Tested Yet!", Toast.LENGTH_SHORT).show()

        }
        binding.reloginTextBtn.setOnClickListener {
            findNavController().popBackStack()
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

    private fun showPasswordMismatchError(binding: FragmentSignupBinding) {
        binding.passwordNotmatchText.visibility = View.VISIBLE

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            binding.passwordNotmatchText.visibility = View.GONE
        }, 2500)
    }

    override fun onGoogleSignInSuccess() {
        findNavController().navigate(R.id.action_signupFragment_to_homeFragment)
        Toast.makeText(requireActivity(), "Signed in with Google", Toast.LENGTH_SHORT).show()
    }

    override fun onFacebookSignInSuccess() {
        findNavController().navigate(R.id.action_signupFragment_to_homeFragment)
        Toast.makeText(requireActivity(), "Signed in with Facebook", Toast.LENGTH_SHORT).show()
    }

    override fun onAuthFailure(errorMessage: String) {
        Toast.makeText(requireActivity(), "Sign-in failed: $errorMessage", Toast.LENGTH_SHORT)
            .show()
    }

    private fun handleFacebookAccessToken(token: String) {
        val credential = FacebookAuthProvider.getCredential(token)
        auth.signInWithCredential(credential).addOnCompleteListener { authTask ->
            if (authTask.isSuccessful) {
                onFacebookSignInSuccess()
            } else {
                onAuthFailure(authTask.exception?.message ?: "Unknown error")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            account?.let {
                val credential = GoogleAuthProvider.getCredential(it.idToken, null)
                auth.signInWithCredential(credential).addOnCompleteListener { authTask ->
                    if (authTask.isSuccessful) {
                        onGoogleSignInSuccess()
                    } else {
                        onAuthFailure(authTask.exception?.message ?: "Unknown error")
                    }
                }
            }
        } catch (e: ApiException) {
            onAuthFailure(e.message ?: "Unknown error")
        }
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }
}
