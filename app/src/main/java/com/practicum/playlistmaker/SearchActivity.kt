package com.practicum.playlistmaker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class SearchActivity : AppCompatActivity() {

    private var searchQuery: String = ""

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
            editText.text?.clear()
            searchLayout.isEndIconVisible = false
        }

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                val hasText = !s.isNullOrEmpty()

                searchLayout.isEndIconVisible = !s.isNullOrEmpty()

                searchLayout.isEndIconVisible = hasText

                searchQuery = s?.toString() ?: ""

            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("SEARCH_QUERY", searchQuery)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        val restoredText = savedInstanceState.getString("SEARCH_QUERY", "")

        if (restoredText.isNotEmpty()) {
            val editText = findViewById<TextInputEditText>(R.id.et_search)
            editText.setText(restoredText+" restored_text")

            // Ставим курсор в конец текста
            editText.setSelection(restoredText.length)
        }
    }

}
