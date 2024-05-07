package com.jessmobilesolutions.tradelink.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class PhoneNumberFormattingTextWatcher(private val editText: EditText) : TextWatcher {
    private var isFormatting: Boolean = false
    private val phoneNumberPattern = "(\\d{2})(\\d{1})(\\d{4})(\\d{4})".toRegex()

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(editable: Editable?) {
        if (isFormatting) {
            return
        }

        isFormatting = true

        val matched = phoneNumberPattern.matchEntire(editable.toString().replace(" ", ""))
        if (matched != null) {
            val formattedText = "(${matched.groupValues[1]}) ${matched.groupValues[2]} ${matched.groupValues[3]}-${matched.groupValues[4]}"
            editText.setText(formattedText)
            editText.setSelection(formattedText.length)
        }

        isFormatting = false
    }
}
