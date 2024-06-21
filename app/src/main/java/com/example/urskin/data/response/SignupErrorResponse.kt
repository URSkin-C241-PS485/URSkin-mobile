package com.example.urskin.data.response

import com.google.gson.annotations.SerializedName

data class SignupErrorResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
