package com.example.imperativeassignment.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.imperativeassignment.ui.AddressDetailsFragment
import com.example.imperativeassignment.ui.BasicDetailsFragment
import com.example.imperativeassignment.ui.OtherDetailsFragment
import com.example.imperativeassignment.utils.Constants.CreateEventFragmentNumbers.*

class CreateUserPagerAdapter(fa: FragmentActivity) :   FragmentStateAdapter(fa) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position){
            BASIC_DETAILS_FRAGMENT -> BasicDetailsFragment()
            ADDRESS_DETAILS_FRAGMENT -> AddressDetailsFragment()
            OTHER_DETAILS_FRAGMENT -> OtherDetailsFragment()
            else -> BasicDetailsFragment()
        }
    }
}
