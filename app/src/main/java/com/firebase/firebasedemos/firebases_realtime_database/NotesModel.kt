package com.firebase.firebasedemos.firebases_realtime_database

import android.os.Parcel
import android.os.Parcelable

data class NotesModel(
    var title: String = "",
    var description:String = "",
    var id:String = "",
    var url:String = ""
): Parcelable{
    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, p1: Int) {
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(id)
        parcel.writeString(url)
    }

    companion object CREATOR: Parcelable.Creator<NotesModel>{
        override fun createFromParcel(parcel: Parcel): NotesModel {
            return NotesModel(parcel)
        }

        override fun newArray(size: Int): Array<NotesModel?> {
            return arrayOfNulls(size)
        }

    }

    private constructor(parcel: Parcel): this (
        title = parcel.readString()?:"",
        description = parcel.readString() ?: "",
        id = parcel.readString() ?: "",
        url = parcel.readString() ?: "",
    )
}
