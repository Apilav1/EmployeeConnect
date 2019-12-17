package com.employeeconnect.extensions

import java.util.regex.Matcher
import java.util.regex.Pattern

fun String.isEmailValid(email: String): Boolean{

    val regExpn = ("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$")

    val inputStr: CharSequence = email

    val pattern: Pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE)
    val matcher: Matcher = pattern.matcher(inputStr)

    return matcher.matches()
}

fun String.isPasswordValid(password: String): Boolean{
    return password.length > 7
}

fun String.isGithubProfileValide(githubUser: String): Boolean{
    val url = "https://api.github.com/users/"
    val COMPLETE_URL = url + githubUser
    var githubJsonStr: String?= null
//    doAsync {
//        val githubJsonStr = URL(COMPLETE_URL).readText()
//    }

    //TODO: async
    return true//githubJsonStr?.contains("Not Found")
}