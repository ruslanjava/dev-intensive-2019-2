package ru.skillbranch.devintensive.extensions

import android.app.Activity
import android.graphics.Rect
import android.view.inputmethod.InputMethodManager

fun Activity.hideKeyboard() {
    if (isKeyboardOpen()) {
        var imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = this.currentFocus
        if (view != null) {
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}

fun Activity.isKeyboardOpen() : Boolean {
    val rect = Rect()
    val rootView = this.window.decorView.rootView
    rootView.getWindowVisibleDisplayFrame(rect)
    val heightDiff = (rootView.height - (rect.bottom - rect.top)).toDouble()
    val heightDiffPercent = (heightDiff / rootView.height)
    return heightDiffPercent > 0.2
}

fun Activity.isKeyboardClosed() : Boolean {
    val rect = Rect()
    val rootView = this.window.decorView.rootView
    rootView.getWindowVisibleDisplayFrame(rect)
    val heightDiff = (rootView.height - (rect.bottom - rect.top)) as Double
    val heightDiffPercent = (heightDiff / rootView.height)
    return heightDiffPercent <= 0.2
}