package com.almurshid.yemeni

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textview.MaterialTextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val textView = MaterialTextView(this).apply {
            text = getString(R.string.app_name)
            textSize = 28f
            setPadding(32, 32, 32, 32)
        }

        setContentView(textView)
    }
}
