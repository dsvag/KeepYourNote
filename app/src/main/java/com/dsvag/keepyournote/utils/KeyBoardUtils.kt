package com.dsvag.keepyournote.utils

import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager

class KeyBoardUtils(private val inputMethodManager: InputMethodManager) {
    fun hideKeyBoard(view: View) {
        try {
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        } catch (e: Exception) {
            Log.e(TAG, "hideKeyboard: $e")
        }
    }

    fun showKeyBoard(view: View) {
        try {
            inputMethodManager.showSoftInput(view, 0)
        } catch (e: Exception) {
            Log.e(TAG, "showKeyboard: $e")
        }
    }

    companion object {
        private const val TAG = "KeyBoardUtils"
    }
}