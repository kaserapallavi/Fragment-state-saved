package com.example.imperativeassignment.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.imperativeassignment.R
import com.example.imperativeassignment.customview.stepcomplitionforrecyclerview.StepComplitionForRecyclerView
import com.example.imperativeassignment.databinding.ActivityUserDetailBinding
import com.example.imperativeassignment.ui.adapter.CreateUserPagerAdapter
import com.example.imperativeassignment.ui.models.CreateUserRequestParser

class UserDetailActivity : AppCompatActivity() {
    private var TOTAL_STEPS_CREATE_ACCOUNT = 3
    private lateinit var binding: ActivityUserDetailBinding
    var createEventReq: CreateUserRequestParser? = null
//    var eventImageList : HashMap<Int,EventImageReqResParser> = HashMap()
//    var eventCoverImageList : HashMap<Int,EventImageReqResParser> = HashMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_detail)
        binding.activity = this
        initializeComponents()
    }

    fun initializeComponents() {
        val pagerAdapter = CreateUserPagerAdapter(this)
        binding.vpCreateEvent.adapter = pagerAdapter
        //disable swipe action of viewpager
        binding.vpCreateEvent.isUserInputEnabled = false
//        createEventReq?.let { userDetailsViewModel.setData(it) }

         setupStepView()
    }

    private fun setupStepView() {
        binding.layStepIndicator.setStepsNumber(TOTAL_STEPS_CREATE_ACCOUNT)
    }

    fun setListeners() {
    }

    override fun onBackPressed() {
        onBackClicked()
    }

    fun setViewPagerItem(item : Int?){
        item?.let {
            binding.vpCreateEvent.setCurrentItem(it,true)
            if (item < binding.layStepIndicator.getStepCount()) {
                binding.layStepIndicator.go(item, true)
            } else {
                binding.layStepIndicator.done(true)
            }
        }
    }



    fun getItemViewType(position: Int): Int {
        return StepComplitionForRecyclerView.getStepComplitionViewType(position, TOTAL_STEPS_CREATE_ACCOUNT)
    }

    fun gotoDashboard(){
//        val i = Intent(this@UserDetailActivity, DashboardActivity::class.java)
//        eventImageList.clear()
//        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        startActivity(i)
//        finish()
    }

    fun updateCreateEventRequestParser(req: CreateUserRequestParser) {
        createEventReq = createEventReq ?: CreateUserRequestParser()
        createEventReq?.apply {
            fullName = req.fullName
            mobileNo = req.mobileNo
            emailId = req.emailId
            permanentAdd = req.permanentAdd
            correspondenceAdd = req.correspondenceAdd
            fatherName = req.fatherName
            motherName = req.motherName
            occupation = req.occupation
        }
    }

    fun updateUserAddressRequestParser(req: CreateUserRequestParser) {
        createEventReq = createEventReq ?: CreateUserRequestParser()
        createEventReq?.apply {
            permanentAdd = req.permanentAdd
            correspondenceAdd = req.correspondenceAdd

        }
    }

    fun updateOtherRequestParser(req: CreateUserRequestParser) {
        createEventReq = createEventReq ?: CreateUserRequestParser()
        createEventReq?.apply {
            fatherName = req.fatherName
            motherName = req.motherName
            occupation = req.occupation
        }
    }




    fun onBackClicked() {
        if (binding.vpCreateEvent.getCurrentItem() == 0)
            gotoDashboard()
        else
            setViewPagerItem(binding.vpCreateEvent.getCurrentItem() - 1)
    }


    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }
}