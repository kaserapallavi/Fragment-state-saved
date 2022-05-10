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
import com.example.imperativeassignment.R
import com.example.imperativeassignment.databinding.FragmentBasicDetailsBinding
import com.example.imperativeassignment.databinding.FragmentOtherDetailsBinding
import com.example.imperativeassignment.ui.models.CreateEventRequestParser
import com.example.imperativeassignment.utils.Constants.CreateEventFragmentNumbers.ADDRESS_DETAILS_FRAGMENT
import com.example.imperativeassignment.utils.UtilityClass

class OtherDetailsFragment : Fragment() {
    lateinit var binding: FragmentOtherDetailsBinding
    var reqCreateEvent = CreateEventRequestParser()

    var motherName = ObservableField<String>()
    var fatherName = ObservableField<String>()
    var occupation = ObservableField<String>()

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
            DataBindingUtil.inflate(inflater, R.layout.fragment_other_details, container, false)
        binding.fragment = this
        initializeComponents()
        setListners()
        return binding.root
    }

    fun initializeComponents() {


    }

    private fun setListners() {
        fatherName.addOnPropertyChangedCallback(observableCallBack)
        motherName.addOnPropertyChangedCallback(observableCallBack)
        occupation.addOnPropertyChangedCallback(observableCallBack)

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

        UtilityClass.showToastMgs(context, "Network call")
//            reqCreateEvent.fullName = binding.txtFatherName.text.toString()
//            reqCreateEvent.mobileNo = binding.txtMotherName.text.toString()
//            reqCreateEvent.emailId = binding.spnOccupation.selectedItem.toString()
//
//            (activity as UserDetailActivity).apply {
//                updateCreateEventRequestParser(reqCreateEvent)
//                setViewPagerItem(ADDRESS_DETAILS_FRAGMENT)
//            }

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

