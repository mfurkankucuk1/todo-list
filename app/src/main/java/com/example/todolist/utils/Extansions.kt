package com.example.todolist.utils

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

fun Fragment.customNavigate(navigateId: Int, bundle: Bundle?) {
    bundle?.let { _bundle ->
        findNavController().navigate(navigateId, _bundle)
    } ?: run {
        findNavController().navigate(navigateId)
    }
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.remove() {
    this.visibility = View.GONE
}