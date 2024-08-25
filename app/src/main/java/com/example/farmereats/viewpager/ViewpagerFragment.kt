package com.example.farmereats.viewpager

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.farmereats.R
import com.example.farmereats.databinding.FragmentViewpagerBinding
import com.example.farmereats.utils.PagerData
import com.google.firebase.auth.FirebaseAuth

class ViewpagerFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        sharedPreferences =
            requireActivity().getSharedPreferences("com.example.farmereats", Context.MODE_PRIVATE)

        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        if (isLoggedIn || auth.currentUser != null) {
            findNavController().navigate(R.id.action_viewpagerFragment_to_homeFragment)
            return null
        }
        val binding = FragmentViewpagerBinding.inflate(inflater)
        val data = listOf(
            PagerData(
                R.drawable.ic_screen_one,
                R.string.quality,
                R.string.page1sec,
                R.color.greenBg,
                R.drawable.ic_id_one
            ),
            PagerData(
                R.drawable.ic_screen_two,
                R.string.convenient,
                R.string.page2sec,
                R.color.redBg,
                R.drawable.ic_id_two
            ),
            PagerData(
                R.drawable.ic_screen_three,
                R.string.local,
                R.string.page3sec,
                R.color.yellowBg,
                R.drawable.ic_id_three
            )
        )
        binding.viewPager.isUserInputEnabled = false
        binding.viewPager.adapter = ViewPagerAdapter(requireActivity(), data)
        return binding.root
    }
}
