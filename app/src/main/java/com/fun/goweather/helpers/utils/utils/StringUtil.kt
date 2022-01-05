package com.`fun`.goweather.helpers.utils.utils

import android.content.Context
import java.io.File
import java.text.DecimalFormat

 object StringUtil {

    fun validateTemp(input: Double?): String {
        return DecimalFormat("#").format(input)
    }

     fun doesDatabaseExist(context: Context, dbName: String): Boolean {
         val dbFile: File = context.getDatabasePath(dbName)
         return dbFile.exists()
     }
}