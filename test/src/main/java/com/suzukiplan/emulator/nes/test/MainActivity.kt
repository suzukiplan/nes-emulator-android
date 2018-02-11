package com.suzukiplan.emulator.nes.test

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.suzukiplan.emulator.nes.core.Emulator

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val textView = findViewById<TextView>(R.id.text)
        textView.text = Emulator.stringFromJNI()
    }
}
