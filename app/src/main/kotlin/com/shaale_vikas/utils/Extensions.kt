package com.shaale_vikas.utils

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.text.NumberFormat
import java.util.Locale

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.showIf(condition: Boolean) {
    visibility = if (condition) View.VISIBLE else View.GONE
}

fun androidx.appcompat.app.AppCompatActivity.toast(msg: String) =
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

fun Fragment.toast(msg: String) =
    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()

fun Double.formatAmount(): String = when {
    this >= 100_000 -> "₹%.1fL".format(this / 100_000)
    this >= 1_000 -> "₹%.1fK".format(this / 1_000)
    else -> "₹${this.toInt()}"
}