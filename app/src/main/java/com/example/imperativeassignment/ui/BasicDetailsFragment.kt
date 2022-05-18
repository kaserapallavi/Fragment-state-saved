package com.example.imperativeassignment.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.imperativeassignment.R
import com.example.imperativeassignment.databinding.FragmentBasicDetailsBinding
import com.example.imperativeassignment.ui.models.CreateUserRequestParser
import com.example.imperativeassignment.utils.Constants.CreateEventFragmentNumbers.ADDRESS_DETAILS_FRAGMENT

class BasicDetailsFragment : Fragment() {
    lateinit var binding: FragmentBasicDetailsBinding
    var reqCreateEvent = CreateUserRequestParser()

    var fullName = ObservableField<String>()
    var mobileNo = ObservableField<String>()
    var emailId = ObservableField<String>()


    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set Instance true
        this.retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_basic_details, container, false)
        binding.fragment = this

        setListners()
        return binding.root
    }



    private fun setListners() {
        fullName.addOnPropertyChangedCallback(observableCallBack)
        mobileNo.addOnPropertyChangedCallback(observableCallBack)
        emailId.addOnPropertyChangedCallback(observableCallBack)

    }

    var observableCallBack = object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(observable: Observable, i: Int) {
//            if(hasAllFieldFilled()){
                enableNextButton()
//            }else{
//                disableNextButton()
//            }
        }
    }

    fun onNextClicked() {
            reqCreateEvent.fullName = binding.txtEventName.text.toString()
            reqCreateEvent.mobileNo = binding.txtMobileNo.text.toString()
            reqCreateEvent.emailId = binding.txtEmailId.text.toString()

            (activity as UserDetailActivity).apply {
                updateCreateEventRequestParser(reqCreateEvent)
                setViewPagerItem(ADDRESS_DETAILS_FRAGMENT)
            }

    }

    fun enableNextButton() {
        this.context?.let {
            ContextCompat.getColorStateList(
                it,
                R.color.createEventSelectedButtonColor
            )
        }?.let {
            binding.btnNext.backgroundTintList = it
        }
    }

    fun disableNextButton() {
        this.context?.let {
            ContextCompat.getColorStateList(
                it,
                R.color.createEventNonSelectedButtonColor
            )
        }?.let {
            binding.btnNext.backgroundTintList = it
        }
    }



    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }

}

