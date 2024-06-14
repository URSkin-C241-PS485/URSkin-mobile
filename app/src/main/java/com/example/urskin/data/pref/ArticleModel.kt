package com.example.urskin.data.pref

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ArticleModel(
    val id: String?,
    val source: String?,
    val title: String?,
    val description: String?,
    val img: Int,
) : Parcelable