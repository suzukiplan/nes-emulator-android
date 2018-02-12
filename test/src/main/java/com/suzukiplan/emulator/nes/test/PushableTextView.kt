package com.suzukiplan.emulator.nes.test

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPropertyAnimatorCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.TextView

class PushableTextView(context: Context, attrs: AttributeSet) : TextView(context, attrs) {
    private var animation: ViewPropertyAnimatorCompat? = null
    var onPushChanged: ((pushing: Boolean) -> Unit)? = null

    init {
        this.isClickable = true
        this.isFocusable = true
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (isEnabled) when (event?.action) {
            MotionEvent.ACTION_DOWN -> startPushAnimation()
            MotionEvent.ACTION_BUTTON_PRESS -> startPushAnimation()
            MotionEvent.ACTION_POINTER_DOWN -> startPushAnimation()
            MotionEvent.ACTION_UP -> endPushAnimation()
            MotionEvent.ACTION_BUTTON_RELEASE -> endPushAnimation()
            MotionEvent.ACTION_POINTER_UP -> endPushAnimation()
            MotionEvent.ACTION_CANCEL -> endPushAnimation()
        }
        return super.onTouchEvent(event)
    }

    private fun startPushAnimation() {
        onPushChanged?.invoke(true)
        animation?.cancel()
        animation = ViewCompat.animate(this)
                .setDuration(120L)
                .alpha(0.2f)
                .withEndAction { animation = null }
        animation?.start()
    }

    private fun endPushAnimation() {
        onPushChanged?.invoke(false)
        animation?.cancel()
        animation = ViewCompat.animate(this)
                .setDuration(120L)
                .alpha(1.0f)
                .withEndAction { animation = null }
        animation?.start()
    }
}