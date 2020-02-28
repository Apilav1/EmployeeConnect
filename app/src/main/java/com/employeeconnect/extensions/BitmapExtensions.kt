package com.employeeconnect.extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.OutputStream


// convert from bitmap to byte array
fun Bitmap.getBytes(): ByteArray {
    val stream = ByteArrayOutputStream()
    this.compress(CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}

// convert from byte array to bitmap
fun ByteArray.getImage(): Bitmap {
    return BitmapFactory.decodeByteArray(this, 0, size)
}

fun Bitmap.convertBitmapToUri(context: Context): Uri {

    val uri: Uri = Uri.fromFile(File.createTempFile("temp_file_name", ".jpg", context.cacheDir))
    val outputStream: OutputStream = context.contentResolver.openOutputStream(uri)!!
    this.compress(CompressFormat.JPEG, 100, outputStream)
    outputStream.close()
    return uri

}
