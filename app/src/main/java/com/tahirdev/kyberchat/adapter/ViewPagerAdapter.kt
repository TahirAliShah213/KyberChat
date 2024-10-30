package com.tahirdev.kyberchat.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tahirdev.kyberchat.ui.onboard.OnboardingFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        // Return the number of onboarding screens
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        // Return a new instance of the onboarding fragment
        return OnboardingFragment.newInstance(position)
    }
}
