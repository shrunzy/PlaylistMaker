package com.practicum.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doOnTextChanged

import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class SearchActivity : AppCompatActivity() {

    private var searchQuery: String = ""

    companion object {
        private const val KEY_SEARCH_QUERY = "SEARCH_QUERY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_search)
        val searchLayout = findViewById<TextInputLayout>(R.id.search_layout)
        val editText = findViewById<TextInputEditText>(R.id.et_search)
            searchLayout.isEndIconVisible = false

        //выход из активити по кнопке назад
        findViewById<ImageView>(R.id.search_back).setOnClickListener {
            finish()
        }

        searchLayout.setEndIconOnClickListener {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            editText.text?.clear()
            searchLayout.isEndIconVisible = false
            currentFocus?.windowToken?.let { windowToken ->
                inputMethodManager?.hideSoftInputFromWindow(windowToken, 0)
            }
        }

        //editText.addTextChangedListener(object : TextWatcher {
        //    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        //    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        //        val hasText = !s.isNullOrEmpty()

        //        searchLayout.isEndIconVisible = !s.isNullOrEmpty()

        //        searchLayout.isEndIconVisible = hasText

        //        searchQuery = s?.toString() ?: ""

        //    }

        //    override fun afterTextChanged(s: Editable?) {

        //    }
        //})

        editText.doOnTextChanged { text, start, before, count ->
            val hasText = !text.isNullOrEmpty()
            searchLayout.isEndIconVisible = hasText

            searchQuery = text?.toString() ?: ""


        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_SEARCH_QUERY, searchQuery)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        val restoredText = savedInstanceState.getString(KEY_SEARCH_QUERY, "")

        if (restoredText.isNotEmpty()) {
            val editText = findViewById<TextInputEditText>(R.id.et_search)
            editText.setText(restoredText+" restored_text")

            // Ставим курсор в конец текста
            editText.setSelection(restoredText.length)
        }
    }


}
