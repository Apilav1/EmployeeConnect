package com.employeeconnect.extensions

import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.MapRowParser
import org.jetbrains.anko.db.SelectQueryBuilder

fun SQLiteDatabase.clear(tableName: String){
    execSQL("Delete from $tableName")
}

fun SelectQueryBuilder.byId(id: String) = whereSimple("_id= ?", id)

fun <T : Any> SelectQueryBuilder.parseList(parser: (Map<String, Any?>) -> T): List<T> =
    parseList(object : MapRowParser<T> {
        override fun parseRow(columns: Map<String, Any?>): T = parser(columns)
    })

fun <T : Any> SelectQueryBuilder.parseOpt(parser: (Map<String, Any?>) -> T): T? =
    parseOpt(object : MapRowParser<T> {
        override fun parseRow(columns: Map<String, Any?>): T = parser(columns)
    })

fun Long.toBoolean(): Boolean{
    return this == 1L
}
