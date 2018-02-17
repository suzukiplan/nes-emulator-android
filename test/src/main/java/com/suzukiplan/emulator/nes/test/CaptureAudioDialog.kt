package com.suzukiplan.emulator.nes.test

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.view.*
import com.suzukiplan.emulator.nes.core.NESAudioCaptureService
import com.suzukiplan.emulator.nes.core.NESView
import java.nio.ByteBuffer

class CaptureAudioDialog : DialogFragment(), SurfaceHolder.Callback, Runnable {
    private var nesView: NESView? = null
    private var preview: SurfaceView? = null
    private var thread: Thread? = null
    private var limitX = 0f
    private var heightCenter = 0f
    private var heightWeight = 0f
    private var alive = false

    fun show(manager: FragmentManager, nesView: NESView?) {
        this.nesView = nesView
        isCancelable = true
        show(manager, tag)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.dialog_capture_audio, container)
        preview = view?.findViewById(R.id.capture_preview)
        preview?.holder?.addCallback(this)
        return view
    }

    override fun onDestroyView() {
        nesView?.setOnCaptureAudioListener(null)
        super.onDestroyView()
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        heightWeight = height.toFloat() / 0x8000
        heightCenter = height / 2f
        limitX = width.toFloat()
        alive = true
        thread = Thread(this)
        thread?.start()
    }

    override fun surfaceDestroyed(p0: SurfaceHolder?) {
        alive = false
        thread?.join()
        thread = null
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
    }

    override fun run() {
        val holder = preview?.holder ?: return

        // read capture loop
        val capture = NESAudioCaptureService(nesView, 100)
        val input = capture.open() ?: return
        val raw = ByteArray((limitX * 2).toInt())
        var leftSize = raw.size
        while (alive && 0 < leftSize) {
            val readSize = input.read(raw, raw.size - leftSize, leftSize)
            leftSize -= readSize
        }
        capture.close()

        // write capture graph
        if (alive) {
            val canvas = holder.lockCanvas() ?: return
            val paint = Paint()
            paint.color = Color.GREEN
            paint.strokeWidth = 4.0f
            val buffer = ByteBuffer.wrap(raw, 0, raw.size).asShortBuffer()
            var x = 0f
            var y = heightCenter
            for (index in 0 until buffer.limit()) {
                val pos = buffer[index]
                val px = x
                x++
                if (limitX <= x) {
                    alive = false
                    break
                }
                val py = y
                y = pos * heightWeight + heightCenter
                canvas.drawLine(px, py, x, y, paint)
            }
            holder.unlockCanvasAndPost(canvas)
        }
    }
}