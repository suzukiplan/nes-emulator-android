package com.suzukiplan.emulator.nes.test

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.view.*
import com.suzukiplan.emulator.nes.core.NESView
import java.nio.ByteBuffer

class CaptureAudioDialog : DialogFragment(), SurfaceHolder.Callback, NESView.OnCaptureAudioListener {
    private var preview: SurfaceView? = null
    private var nesView: NESView? = null
    private var x = 0f
    private var y = 0f
    private var limitX = 0f
    private var heightCenter = 0f
    private var heightWeight = 0f
    private val paint = Paint()

    fun show(manager: FragmentManager, nesView: NESView?) {
        this.nesView = nesView
        isCancelable = true
        paint.color = Color.GREEN
        paint.strokeWidth = 4.0f
        show(manager, tag)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.dialog_capture_audio, container)
        preview = view?.findViewById(R.id.capture_preview)
        preview?.holder?.addCallback(this)
        nesView?.setOnCaptureAudioListener(this)
        return view
    }

    override fun onDestroyView() {
        nesView?.setOnCaptureAudioListener(null)
        super.onDestroyView()
    }

    //    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        x = 0f
        y = height / 2f
        heightWeight = y / 0x8000
        heightCenter = y
        limitX = width.toFloat()
    }

    override fun surfaceDestroyed(p0: SurfaceHolder?) {
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
    }

    override fun onCaptureAudio(pcm: ByteBuffer?) {
        val buffer = pcm?.asShortBuffer()?.asReadOnlyBuffer() ?: return
        val holder = preview?.holder ?: return
        val canvas = holder.lockCanvas() ?: return
        for (index in 0 until buffer.limit()) {
            val pos = buffer[index]
            val px = x
            x++
            if (limitX <= x) {
                nesView?.setOnCaptureAudioListener(null)
                break
            }
            val py = y
            y = pos * heightWeight + heightCenter
            canvas.drawLine(px, py, x, y, paint)
        }
        holder.unlockCanvasAndPost(canvas)
    }
}