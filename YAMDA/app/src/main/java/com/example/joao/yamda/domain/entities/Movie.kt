package com.example.joao.yamda.domain.entities

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable

data class Movie(
        val id :Int,
        val title : String,
        val poster_path: String?,
        val vote_average : Double?,
        val overview : String?,
        val release_date : String?,
        var image : Bitmap?) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Double::class.java.classLoader) as? Double,
            parcel.readString(),
            parcel.readString(),
            parcel.readParcelable(Bitmap::class.java.classLoader))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(poster_path)
        parcel.writeValue(vote_average)
        parcel.writeString(overview)
        parcel.writeString(release_date)
        parcel.writeParcelable(image, flags)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Movie> {
        override fun createFromParcel(parcel: Parcel): Movie = Movie(parcel)
        override fun newArray(size: Int): Array<Movie?> = arrayOfNulls(size)
    }
}