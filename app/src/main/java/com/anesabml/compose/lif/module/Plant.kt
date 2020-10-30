package com.anesabml.compose.lif.module

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.DrawableRes
import com.anesabml.compose.lif.R


data class Plant(
    @DrawableRes
    val image: Int,
    val name: String,
    val nickname: String,
    val wateringInterval: Long = 86_400,
    val lastDayWatering: Int
) : Parcelable {
    val wateringIntervalDay: Int = (wateringInterval / (60 * 60 * 24)).toInt()
    val nextDayWatering = (lastDayWatering + wateringIntervalDay) % 7

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readLong(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(image)
        parcel.writeString(name)
        parcel.writeString(nickname)
        parcel.writeLong(wateringInterval)
        parcel.writeInt(lastDayWatering)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Plant> {
        override fun createFromParcel(parcel: Parcel): Plant {
            return Plant(parcel)
        }

        override fun newArray(size: Int): Array<Plant?> {
            return arrayOfNulls(size)
        }
    }

}

val plants = listOf(
    Plant(
        R.drawable.plant_1,
        "Aloe",
        "Planty",
        86_400 /* 1 day */,
        4
    ),
    Plant(
        R.drawable.plant_2,
        "Lilly",
        "Peace lily",
        259_200/* 3 day */,
        3
    ),
    Plant(
        R.drawable.plant_3,
        "Lillo",
        "Peace lilo",
        172_800/* 2 day */,
        3
    ),
)