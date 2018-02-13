package com.suzukiplan.emulator.nes.test

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.suzukiplan.emulator.nes.core.Logger
import com.suzukiplan.emulator.nes.core.NESKey
import com.suzukiplan.emulator.nes.core.NESView

class MainActivity : AppCompatActivity() {
    private var nesView: NESView? = null
    private var tickThread: Thread? = null
    private var active = false
    private val keyP1 = NESKey()

    @SuppressLint("ShowToast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Logger.enabled = true

        // key input procedure
        findViewById<View>(R.id.capture).setOnClickListener {
            val captureBitmap = Bitmap.createBitmap(256, 240, Bitmap.Config.RGB_565)
            val captureCanvas = Canvas(captureBitmap)
            val rect = Rect(0, 0, 256, 240)
            nesView?.capture(captureCanvas, rect)
            CaptureDialog().show(supportFragmentManager, captureBitmap)
        }
        findViewById<View>(R.id.reset).setOnClickListener { nesView?.reset() }
        findViewById<PushableTextView>(R.id.up).onPushChanged = { pushing -> keyP1.up = pushing }
        findViewById<PushableTextView>(R.id.down).onPushChanged = { pushing -> keyP1.down = pushing }
        findViewById<PushableTextView>(R.id.left).onPushChanged = { pushing -> keyP1.left = pushing }
        findViewById<PushableTextView>(R.id.right).onPushChanged = { pushing -> keyP1.right = pushing }
        findViewById<PushableTextView>(R.id.a).onPushChanged = { pushing -> keyP1.a = pushing }
        findViewById<PushableTextView>(R.id.b).onPushChanged = { pushing -> keyP1.b = pushing }
        findViewById<PushableTextView>(R.id.select).onPushChanged = { pushing -> keyP1.select = pushing }
        findViewById<PushableTextView>(R.id.start).onPushChanged = { pushing -> keyP1.start = pushing }

        // start emulator
        nesView = findViewById(R.id.nes_view)
        active = true
        tickThread = Thread {
            if (true != nesView?.load(readAsset("snow-demo-by-tennessee-carmel-veilleux-pd.nes"))) {
                runOnUiThread { Toast.makeText(this, "Load failed!", Toast.LENGTH_LONG) }
                return@Thread
            }
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
