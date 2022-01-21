package com.example.healer.utils

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.view.View.OnTouchListener
import android.widget.EditText
import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

class HideKeyboard {

    //setup an OnTouchListener for all edittext in a view
    @SuppressLint("ClickableViewAccessibility")
    fun setupUI(view: View, context: Context) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (view !is EditText) {
            view.setOnTouchListener { _, _ ->
                hideKeyboardFrom(view, context)
                false
            }
        }

        //If a layout container, iterate over children and seed recursion.
        if (view is ViewGroup) {
            for (i in 0 until (view).childCount) {
                val innerView: View = (view).getChildAt(i)
                setupUI(innerView,context)
            }
        }
    }

    //hide keyboard from activity
    private fun hideSoftKeyboard(activity: Activity) {
        val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputMethodManager.isAcceptingText) {
            inputMethodManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
        }
    }

    //hide keyboard from fragment
    private fun hideKeyboardFrom(view: View,context: Context) {
        val inputMethodManager = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputMethodManager.isAcceptingText) {
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }


}