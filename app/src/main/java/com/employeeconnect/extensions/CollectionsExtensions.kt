package com.employeeconnect.extensions

import android.graphics.Bitmap
import com.employeeconnect.domain.datasource.DataSource
import java.lang.Exception

inline fun <T, R : Any> Iterable<T>.firstResult(predicate: (T) -> R?): R {
    for (element in this) {
       if((element as DataSource).ready && (element as DataSource).preferred) {
           val result = predicate(element)
           if (result != null) return result
       }
    }
    throw Exception("Data source providers problem")
}

fun <K, V : Any> Map<K, V?>.toVarargArray(): Array<Pair<K, Any?>> =
    map { Pair(it.key, if(it.value is Bitmap?) (it.value as Bitmap).getBytes() else it.value ) }.toTypedArray()
