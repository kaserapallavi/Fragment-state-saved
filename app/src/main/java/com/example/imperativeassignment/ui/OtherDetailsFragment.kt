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
import com.example.imperativeassignment.databinding.FragmentOtherDetailsBinding
import com.example.imperativeassignment.network.UserApplication
import com.example.imperativeassignment.ui.models.CreateUserRequestParser
import com.example.imperativeassignment.ui.models.CreateUserResponseParser
import com.example.imperativeassignment.utils.UtilityClass
import retrofit2.Call
import retrofit2.Response
import java.lang.Exception

class OtherDetailsFragment : Fragment() {
    lateinit var binding: FragmentOtherDetailsBinding
    var reqCreateEvent = CreateUserRequestParser()

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

        reqCreateEvent.fatherName = binding.txtFatherName.text.toString()
        reqCreateEvent.motherName = binding.txtMotherName.text.toString()
        reqCreateEvent.occupation = binding.spnOccupation.selectedItem.toString()

        (activity as UserDetailActivity).apply {
            updateOtherRequestParser(reqCreateEvent)

        }
        UtilityClass.showToastMgs(context,  "network call ${(activity as UserDetailActivity).createEventReq.toString()}")
        //IT IS ONLY FOR ONLY REPRESENTATIONAL SO I COMMENTED THIS FUNCTION
//        createEventAPICalled((activity as UserDetailActivity).createEventReq)



    }

    private fun createEventAPICalled(createEventReq: CreateUserRequestParser?) {
        UserApplication.getRetroApiClient().createUser(createEventReq)
            .enqueue(object : retrofit2.Callback<CreateUserResponseParser?> {
                override fun onResponse(
                    call: Call<CreateUserResponseParser?>,
                    response: Response<CreateUserResponseParser?>
                ) {
                    try {
                        UtilityClass.showToastMgs(context,"User created Succesfully")

//
                    } catch (e: Exception) {
                        e.printStackTrace()
                        UtilityClass.showToastMgs(context, e.message)
                    }
                }

                override fun onFailure(call: Call<CreateUserResponseParser?>, t: Throwable) {
                    UtilityClass.showToastMgs(context, t.message)

                }
            })
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

