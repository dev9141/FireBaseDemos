package com.firebase.firebasedemos.firebase_cloud_firestore

import android.os.Parcel
import android.os.Parcelable

data class NotesModelStore(
    var title: String = "",
    var description:String = "",
    var url:String = "",
    var id:String = ""
): Parcelable{
    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, p1: Int) {
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(url)
        parcel.writeString(id)
    }

    companion object CREATOR: Parcelable.Creator<NotesModelStore>{
        override fun createFromParcel(parcel: Parcel): NotesModelStore {
            return NotesModelStore(parcel)
        }

        override fun newArray(size: Int): Array<NotesModelStore?> {
            return arrayOfNulls(size)
        }

    }

    private constructor(parcel: Parcel): this (
        title = parcel.readString()?:"",
        description = parcel.readString() ?: "",
        url = parcel.readString() ?: "",
        id = parcel.readString() ?: "",
    )
}
