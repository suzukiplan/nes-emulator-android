package com.suzukiplan.emulator.nes.test

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

class CaptureVideoDialog : DialogFragment() {
    private lateinit var capture: Bitmap

    fun show(manager: FragmentManager, capture: Bitmap) {
        this.capture = capture
        isCancelable = true
        show(manager, tag)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.dialog_capture_video, container)
        view?.findViewById<ImageView>(R.id.capture_preview)?.setImageBitmap(capture)
        return view
    }
}