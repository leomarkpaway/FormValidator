package com.leomarkpaway.simple_input_form.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.leomarkpaway.simple_input_form.R
import com.leomarkpaway.simple_input_form.common.constant.Constants.EDIT
import com.leomarkpaway.simple_input_form.common.constant.Constants.LAYOUT
import com.leomarkpaway.simple_input_form.common.constant.Constants.MESSAGE
import com.leomarkpaway.simple_input_form.common.constant.FormType.AGE
import com.leomarkpaway.simple_input_form.common.constant.FormType.DATE
import com.leomarkpaway.simple_input_form.common.constant.FormType.EMAIL
import com.leomarkpaway.simple_input_form.common.constant.FormType.MOBILE
import com.leomarkpaway.simple_input_form.common.constant.FormType.NAME
import com.leomarkpaway.simple_input_form.common.enums.Gender
import com.leomarkpaway.simple_input_form.common.util.LiveFormValidator
import com.leomarkpaway.simple_input_form.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var selectedDate: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        datePicker()
        genderSpinner()
        setupFormValidation()

    }

    private fun setupFormValidation() = with(binding) {
        val fullName = arrayOf(ilFullName, edtFullName, getString(R.string.helper_full_name))
        val emailAddress =
            arrayOf(ilEmailAddress, edtEmailAddress, getString(R.string.helper_email_address))
        val mobileNumber =
            arrayOf(ilMobileNumber, edtMobileNumber, getString(R.string.helper_mobile_number))
        val dateOfBirth =
            arrayOf(ilDateOfBirth, edtDateOfBirth, getString(R.string.helper_required))
        val age = arrayOf(ilAge, edtAge, getString(R.string.helper_age))
        val gender = arrayOf(ilGender, actGender, getString(R.string.helper_required))

        val formLayout = listOf(
            (fullName[LAYOUT] as TextInputLayout),
            (emailAddress[LAYOUT] as TextInputLayout),
            (mobileNumber[LAYOUT] as TextInputLayout),
            (dateOfBirth[LAYOUT] as TextInputLayout),
            (age[LAYOUT] as TextInputLayout),
        )

        val formEdit = listOf(
            (fullName[EDIT] as TextInputEditText),
            (emailAddress[EDIT] as TextInputEditText),
            (mobileNumber[EDIT] as TextInputEditText),
            (dateOfBirth[EDIT] as TextInputEditText),
            (age[EDIT] as TextInputEditText),
        )

        val helperMessage = listOf(
            (fullName[MESSAGE] as String),
            (emailAddress[MESSAGE] as String),
            (mobileNumber[MESSAGE] as String),
            (dateOfBirth[MESSAGE] as String),
            (age[MESSAGE] as String),
        )

        val validationRules = mapOf(
            formEdit[NAME] to "^[a-zA-Z\\s,.'-]+$",
            formEdit[EMAIL] to "",
            formEdit[MOBILE] to "^((\\+)?(\\d{2}[-\\s]?)?(\\d{10}))$",
            formEdit[DATE] to "",
            formEdit[AGE] to "",
        )

        val formValidator =
            LiveFormValidator(
                formLayout,
                formEdit,
                validationRules,
                helperMessage,
                gender,
                btnSubmit
            )

        for (field in formEdit) {
            field.addTextChangedListener(formValidator)
        }

    }

    private fun datePicker() = with(binding) {
        val datePicker = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            selectedDate.set(Calendar.YEAR, year)
            selectedDate.set(Calendar.MONTH, monthOfYear)
            selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            setAge(this, selectedDate.time)
            edtDateOfBirth.setText(
                getString(
                    R.string.default_date_format,
                    selectedDate.get(Calendar.DAY_OF_MONTH),
                    (selectedDate.get(Calendar.MONTH) + 1),
                    selectedDate.get(Calendar.YEAR)
                )
            )
        }

        edtDateOfBirth.setOnClickListener {
            // display the date picker
            DatePickerDialog(
                this@MainActivity, datePicker,
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun setAge(binding: ActivityMainBinding, birthdate: Date) {
        val now = Calendar.getInstance()
        val dob = Calendar.getInstance()
        dob.time = birthdate

        var age = now.get(Calendar.YEAR) - dob.get(Calendar.YEAR)
        if (now.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) age--
        binding.edtAge.setText(age.toString())
    }

    private fun genderSpinner() = with(binding) {
        val items = listOf(Gender.MALE.value, Gender.FEMALE.value)
        val adapter = ArrayAdapter(this@MainActivity, R.layout.item_gender, items)
        (actGender as AutoCompleteTextView).setAdapter(adapter)
        actGender.setOnClickListener { actGender.showDropDown() }
        actGender.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val value = when (position) {
                    0 -> ilGender.editText?.text.toString()
                    1 -> ilGender.editText?.text.toString()
                    else -> null
                }
            }
    }

}