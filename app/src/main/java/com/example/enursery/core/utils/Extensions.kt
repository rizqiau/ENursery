package com.example.enursery.core.utils

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun EditText.getTrimmed(): String = this.text.toString().trim()
fun EditText.getDoubleOrNull(): Double? = this.text.toString().trim().toDoubleOrNull()
fun EditText.getIntOrNull(): Int? = this.text.toString().trim().toIntOrNull()

fun EditText.isValidEmail(): Boolean =
    Patterns.EMAIL_ADDRESS.matcher(this.text.toString().trim()).matches()

fun EditText.isValidPassword(minLength: Int = 6): Boolean =
    this.text.toString().trim().length >= minLength

fun EditText.afterTextChanged(after: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            after(editable?.toString().orEmpty())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
    })
}

fun Int.toFormatted(): String {
    return NumberFormat.getInstance(Locale("in", "ID")).format(this)
}

fun Double.toFormatted(): String {
    return NumberFormat.getInstance(Locale("in", "ID")).format(this)
}

fun Date.formatToIndo(): String {
    val locale = Locale("in", "ID")
    val formatter = SimpleDateFormat("dd MMMM yyyy", locale)
    return formatter.format(this)
}

fun String.toIndoDate(): String {
    return try {
        val parser = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = parser.parse(this)
        date?.formatToIndo() ?: this
    } catch (e: Exception) {
        this
    }
}

// --- View Extensions ---
fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(windowToken, 0)
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}


fun String.canAddPlot(): Boolean {
    return this in RoleConstants.ROLE_CAN_ADD_PLOT
}