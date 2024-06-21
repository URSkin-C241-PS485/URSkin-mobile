package com.example.urskin.data.response

import com.google.gson.annotations.SerializedName

data class ErrorResponse(

    @field:SerializedName("error")
    val error: String? = null,

    @field:SerializedName("message")
    val message: String? = null

)
