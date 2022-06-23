package com.example.melisearch.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class PictureItemDetail (
    @SerializedName("id") val id:String? ,// "992637-MLM43192648311_082020",
    @SerializedName("secure_url") val url: String?,// "https://http2.mlstatic.com/D_992637-MLM43192648311_082020-O.jpg",
        ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PictureItemDetail> {
        override fun createFromParcel(parcel: Parcel): PictureItemDetail {
            return PictureItemDetail(parcel)
        }

        override fun newArray(size: Int): Array<PictureItemDetail?> {
            return arrayOfNulls(size)
        }
    }
}