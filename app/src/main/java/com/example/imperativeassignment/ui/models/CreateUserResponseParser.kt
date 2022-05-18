package com.example.imperativeassignment.ui.models


import com.google.gson.annotations.SerializedName

data class CreateUserResponseParser(
    @SerializedName("status")
    var status: String?,
    @SerializedName("message")
    var message: String?
)