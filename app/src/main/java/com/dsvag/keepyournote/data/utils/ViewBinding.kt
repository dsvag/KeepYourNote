package com.dsvag.keepyournote.data.utils

import android.app.Activity
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding

inline fun <V : ViewBinding> Activity.viewBinding(crossinline inflate: (LayoutInflater) -> V): Lazy<V> {
    return lazy(LazyThreadSafetyMode.NONE) { inflate(layoutInflater) }
}