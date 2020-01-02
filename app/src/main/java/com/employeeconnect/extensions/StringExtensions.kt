package com.employeeconnect.extensions

import java.lang.Math.abs
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

fun String.isEmailValid(): Boolean{

    val regExpn = ("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$")

    val inputStr: CharSequence = this

    val pattern: Pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE)
    val matcher: Matcher = pattern.matcher(inputStr)

    return matcher.matches()
}

fun String.isPasswordValid(): Boolean{
    return this.length > 7
}

fun String.isGithubProfileValide(): Boolean{
    val url = "https://api.github.com/users/"
    val COMPLETE_URL = url + this
    var githubJsonStr: String?= null
//    doAsync {
//        val githubJsonStr = URL(COMPLETE_URL).readText()
//    }

    //TODO: async
    return true//githubJsonStr?.contains("Not Found")
}

fun String.getDateTimeFromTimestamp() : String? {
    try{
        val sdf = SimpleDateFormat("MM/dd/yyyy")
        val messageDate = Date(this.toLong() * 1000)
        val currentDate = Date()

        val currentCalendar = Calendar.getInstance()
        currentCalendar.time = currentDate
        val currentYear = currentCalendar.get(Calendar.YEAR)
        val currentMonth = currentCalendar.get(Calendar.MONTH)
        val currentDayOfMonth = currentCalendar.get(Calendar.DAY_OF_MONTH)

        val calendar = Calendar.getInstance()
        calendar.time = messageDate
        val messageDateYear = calendar.get(Calendar.YEAR)
        val messageDateMonth = calendar.get(Calendar.MONTH)
        val messageDateDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val messageIsSentThisYear = messageDateYear == currentYear
        val messageIsSentInThisMonth = messageDateMonth == currentMonth
        val messageIsSentInThisWeek = abs(messageDateDayOfMonth - currentDayOfMonth) < 7

        if(messageIsSentThisYear){

              if(messageIsSentInThisMonth && messageIsSentInThisWeek){

                  if(messageDateDayOfMonth == currentDayOfMonth)
                      return "Today"
                  if(messageDateDayOfMonth == currentDayOfMonth-1)
                      return "Yesterday"

                  val monthFormat = SimpleDateFormat("EEEE")
                  return monthFormat.format(calendar.time)
              }
              else{
                  val monthFormat = SimpleDateFormat("MMMM")
                  return monthFormat.format(calendar.time) + "  " + messageDateDayOfMonth
              }
        }
        else{
            return sdf.format(messageDate)
        }
    }
    catch (e: Exception){
        return e.toString()
    }
}