package com.suzukiplan.emulator.nes.test

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.suzukiplan.emulator.nes.core.Logger
import com.suzukiplan.emulator.nes.core.NESKey
import com.suzukiplan.emulator.nes.core.NESView

class MainActivity : AppCompatActivity() {
    private var nesView: NESView? = null
    private var tickThread: Thread? = null
    private var active = false
    private val keyP1 = NESKey()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Logger.enabled = true
        nesView = findViewById(R.id.nes_view)
        active = true
        tickThread = Thread {
            nesView?.load(readAsset("snow-demo-by-tennessee-carmel-veilleux-pd.nes"))
            while (active) {
                nesView?.tick(keyP1.code, 0)
            }
        }
        tickThread?.start()
    }

    private fun readAsset(path: String): ByteArray {
        val inputStream = assets.open(path)
        val fileBytes = ByteArray(inputStream.available())
        inputStream.read(fileBytes)
        inputStream.close()
        return fileBytes
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
