package com.izmir.izmirli.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Location(
    var lat: Double? = null,
    var long: Double? = null,
    var name: String? = null
): Parcelable
