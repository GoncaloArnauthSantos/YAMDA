package com.example.joao.yamda.domain.entities

import android.os.Parcel
import android.os.Parcelable

data class Operation(val action : String, val search : String?, val language: String): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(action)
        parcel.writeString(search)
        parcel.writeString(language)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Operation> {
        override fun createFromParcel(parcel: Parcel): Operation = Operation(parcel)

        override fun newArray(size: Int): Array<Operation?> = arrayOfNulls(size)
    }
}