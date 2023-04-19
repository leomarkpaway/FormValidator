package com.leomarkpaway.simple_input_form.common.util

import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.leomarkpaway.simple_input_form.common.constant.Constants.LAYOUT
import com.leomarkpaway.simple_input_form.common.constant.Constants.MESSAGE
import com.leomarkpaway.simple_input_form.common.constant.FormType.AGE
import com.leomarkpaway.simple_input_form.common.constant.FormType.DATE
import com.leomarkpaway.simple_input_form.common.constant.FormType.EMAIL
import com.leomarkpaway.simple_input_form.common.constant.FormType.MOBILE
import com.leomarkpaway.simple_input_form.common.constant.FormType.NAME

class LiveFormValidator(
    private val formLayout: List<TextInputLayout>,
    private val formFields: List<EditText>,
    private val validationRules: Map<TextInputEditText, String>,
    private val helperMessage: List<String>,
    private val gender: Array<Any>,
    private val submit: Button
) :
    TextWatcher {

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {
        validateForm()
    }

    private fun validateForm() {
        var isFormValid: Boolean
        var isNameValid = true
        var isEmailValid = true
        var isMobileValid = true
        var isAgeValid = true
        var isGenderValid = true

        for (field in formFields) {
            val rule = validationRules[field]
            if (rule != null) {
                when (field) {
                    formFields[NAME] -> {
                        isNameValid = field.text.matches(Regex(rule))
                        formLayout[NAME].helperText =
                            if (!isNameValid) helperMessage[NAME] else null
                    }
                    formFields[EMAIL] -> {
                        isEmailValid = Patterns.EMAIL_ADDRESS.matcher(field.text).matches()
                        formLayout[EMAIL].helperText =
                            if (!isEmailValid) helperMessage[EMAIL] else null
                    }
                    formFields[MOBILE] -> {
                        isMobileValid = field.text.matches(Regex(rule))
                        formLayout[MOBILE].helperText =
                            if (!isMobileValid) helperMessage[MOBILE] else null
                    }
                    formFields[DATE] -> {
                        isMobileValid = field.text.isNotBlank()
                        formLayout[DATE].helperText =
                            if (!isMobileValid) helperMessage[DATE] else null
                    }
                    formFields[AGE] -> {
                        if (field.text.isNotBlank()) {
                            isAgeValid = field.text.toString().toInt() >= 18
                            formLayout[AGE].helperText =
                                if (!isAgeValid) helperMessage[AGE] else null
                        }
                    }
                }

            }
            val inputLayout = (gender[LAYOUT] as TextInputLayout)

            if (!inputLayout.editText?.text.isNullOrBlank()) {
                inputLayout.helperText = null
                isGenderValid = true
            } else {
                inputLayout.helperText = gender[MESSAGE].toString()
                isGenderValid = false
            }

            isFormValid =
                !(isNameValid && isEmailValid && isMobileValid && isAgeValid && isGenderValid)
            if (isFormValid) {
                submit.isClickable = false
                submit.background.setTint(Color.GRAY)
            } else {
                submit.isClickable = true
                submit.background.setTint(Color.GREEN)
            }
        }
    }
}