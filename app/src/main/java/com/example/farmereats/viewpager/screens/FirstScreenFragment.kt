package com.example.farmereats.viewpager.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.farmereats.R
import com.example.farmereats.databinding.FragmentFirstScreenBinding
import com.example.farmereats.utils.PagerData


class FirstScreenFragment : Fragment() {

    private var pagerData: PagerData? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentFirstScreenBinding.inflate(inflater)

        arguments?.let {
            pagerData = it.getParcelable("pagerData")
            pagerData?.let { data ->
                binding.headingText.setText(data.title)
                binding.aboutHeadingText.setText(data.desc)
                binding.screenOneImage.setImageResource(data.image)
                binding.containerBg.setBackgroundResource(data.color)
                binding.indegatorOneImage.setImageResource(data.dotImage)
                binding.joinBtn.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), data.color)
            }
        }

        binding.joinBtn.setOnClickListener {
            val viewPager = requireActivity().findViewById<ViewPager2>(R.id.view_pager)
            val currentPosition = viewPager.currentItem
            val itemCount = viewPager.adapter?.itemCount ?: 0

            if (currentPosition < itemCount - 1) {
                viewPager.currentItem = currentPosition + 1
            } else {
                findNavController().navigate(R.id.action_viewpagerFragment_to_loginFragment)
            }
        }

        binding.loginTextBtn.setOnClickListener {
            findNavController().navigate(R.id.action_viewpagerFragment_to_loginFragment)
        }

        return binding.root
    }

    companion object {
        fun newInstance(pagerData: PagerData): FirstScreenFragment {
            val fragment = FirstScreenFragment()
            val args = Bundle()
            args.putParcelable("pagerData", pagerData)
            fragment.arguments = args
            return fragment
        }
    }
}

