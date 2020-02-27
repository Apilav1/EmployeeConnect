package com.employeeconnect.extensions

import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream


// convert from bitmap to byte array
fun Bitmap.getBytes(): ByteArray {
    val stream = ByteArrayOutputStream()
    this.compress(CompressFormat.PNG, 0, stream)
    return stream.toByteArray()
}

// convert from byte array to bitmap
fun ByteArray.getImage(): Bitmap {
    return BitmapFactory.decodeByteArray(this, 0, size)
}
