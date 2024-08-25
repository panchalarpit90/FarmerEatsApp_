package com.example.farmereats.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.farmereats.utils.PagerData
import com.example.farmereats.viewpager.screens.FirstScreenFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity, private val data: List<PagerData>) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = data.size

    override fun createFragment(position: Int): Fragment {
        return FirstScreenFragment.newInstance(data[position])
    }
}
