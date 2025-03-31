package com.example.enursery.core.utils

import android.widget.EditText

fun EditText.getTrimmed(): String = this.text.toString().trim()
fun EditText.getDoubleOrNull(): Double? = this.text.toString().toDoubleOrNull()
fun EditText.getIntOrNull(): Int? = this.text.toString().toIntOrNull()
