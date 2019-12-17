package com.employeeconnect.extensions

import android.view.View
import android.view.ViewGroup



 fun ViewGroup?.revealAndDropAnimation(views: List<View>){

    val animationDuration = 2000L
    val animationTranslationY = 32.0f

    views.forEach{
        it.alpha = 0.0f
        it.animate().alpha(1.0f).translationY(animationTranslationY).setDuration(animationDuration)
    }

}
