package com.employeeconnect.extensions

import java.lang.Exception

inline fun <T, R : Any> Iterable<T>.firstResult(predicate: (T) -> R?): R {
    for (element in this) {
        val result = predicate(element)
        if (result != null) return result
    }
    throw Exception("Data source providers problem")
}

fun <K, V : Any> Map<K, V?>.toVarargArray(): Array<out Pair<K, V?>> =
map { Pair(it.key, it.value) }.toTypedArray()