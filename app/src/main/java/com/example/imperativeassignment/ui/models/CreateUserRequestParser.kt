package com.example.imperativeassignment.ui.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class CreateUserRequestParser(
    @SerializedName("full_name")
    var fullName: String? = null,
    @SerializedName("mobile_no")
    var mobileNo: String? = null,
    @SerializedName("email_id")
    var emailId: String? = null,
    @SerializedName("permanent_add")
    var permanentAdd: String? = null,
    @SerializedName("correspondence_add")
    var correspondenceAdd: String? = null,
    @SerializedName("father_name")
    var fatherName: String? = null,
    @SerializedName("mother_name")
    var motherName: String? = null,
    @SerializedName("occupation")
    var occupation: String? = null
) : Parcelable
