package com.example.urskin.data.response

import com.google.gson.annotations.SerializedName

data class ProfileResponse(

	@field:SerializedName("fullName")
	val fullName: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)
