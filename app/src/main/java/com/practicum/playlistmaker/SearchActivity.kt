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

            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }


}
