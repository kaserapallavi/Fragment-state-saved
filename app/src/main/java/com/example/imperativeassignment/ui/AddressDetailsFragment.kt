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
import com.example.imperativeassignment.databinding.FragmentAddressDetailsBinding
import com.example.imperativeassignment.ui.models.CreateUserRequestParser
import com.example.imperativeassignment.utils.Constants.CreateEventFragmentNumbers.OTHER_DETAILS_FRAGMENT

class AddressDetailsFragment : Fragment() {
    lateinit var binding: FragmentAddressDetailsBinding
    var reqCreateEvent = CreateUserRequestParser()

    var permanentAdd = ObservableField<String>()
    var correspondenceAdd = ObservableField<String>()

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
            DataBindingUtil.inflate(inflater, R.layout.fragment_address_details, container, false)
        binding.fragment = this
        setListners()
        return binding.root
    }



    private fun setListners() {
        permanentAdd.addOnPropertyChangedCallback(observableCallBack)
        correspondenceAdd.addOnPropertyChangedCallback(observableCallBack)

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
            reqCreateEvent.permanentAdd = binding.txtPermanentAdd.text.toString()
            reqCreateEvent.correspondenceAdd = binding.txtCorrespondAdd.text.toString()

            (activity as UserDetailActivity).apply {
                updateUserAddressRequestParser(reqCreateEvent)
                setViewPagerItem(OTHER_DETAILS_FRAGMENT)
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

