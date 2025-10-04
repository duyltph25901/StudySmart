package com.example.studysmart.data.database.converter

import androidx.room.TypeConverter

class ColorsConverter {
    @TypeConverter
    fun fromColors(colors: List<Int>) =
        colors.joinToString(",") { it.toString() }

    @TypeConverter
    fun toColors(colorsStr: String) =
        colorsStr.split(",").map { it.toInt() }
}