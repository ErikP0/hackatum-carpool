package de.hackatum2018.sixtcarpool.utils

import android.text.Editable
import android.text.TextWatcher

class TextChangedListener(private val onTextChangedListener: (Editable?) -> Unit) :
    TextWatcher {
    override fun afterTextChanged(p0: Editable?) {
        onTextChangedListener(p0)
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
}