package com.suzukiplan.emulator.nes.test

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.suzukiplan.emulator.nes.core.Logger
import com.suzukiplan.emulator.nes.core.NESView

class MainActivity : AppCompatActivity() {
    private var nesView: NESView? = null
    private var tickThread: Thread? = null
    private var active = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Logger.enabled = true
        nesView = findViewById(R.id.nes_view)
        active = true
        tickThread = Thread {
            while (active) {
                nesView?.tick()
            }
        }
        tickThread?.start()
    }

    override fun onDestroy() {
        active = false
        tickThread?.join()
        tickThread = null
        nesView?.destroy()
        nesView = null
        super.onDestroy()
    }
}
