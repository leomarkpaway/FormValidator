package com.leomarkpaway.simple_input_form.model

import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

data class FormModel(
    val inputLayout: TextInputLayout,
    val editText: TextInputEditText,
    val helperMassage: String
)
