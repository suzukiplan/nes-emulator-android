package com.suzukiplan.emulator.nes.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * NES emulator view
 */
public class NESView extends SurfaceView implements SurfaceHolder.Callback {
    private final Bitmap vram = Bitmap.createBitmap(256, 240, Bitmap.Config.RGB_565);
    private final Rect vramRect = new Rect(0, 0, 256, 240);
    private final Object locker = new Object();
    private Rect viewRect = null;
    private final Paint paint = new Paint();
    private Long context = null;
    private OnCaptureAudioListener onCaptureAudioListener = null;
    private Timer captureTimer = null;

    /**
     * interface for capture the audio
     */
    public interface OnCaptureAudioListener {
        /**
         * fires when the audio data (pcm) was captured
         *
         * @param pcm audio data (44100Hz, 16bit, mono)
         */
        void onCaptureAudio(byte[] pcm);
    }

    /**
     * create NESView
     *
     * @param context context
     */
    public NESView(Context context) {
        super(context);
        init();
    }

    /**
     * create NESView
     *
     * @param context context
     * @param attrs   attribute set
     */
    public NESView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * create NESView
     *
     * @param context      context
     * @param attrs        attribute set
     * @param defStyleAttr default style attribute
     */
    public NESView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        NESLogger.d("create nes-view");
        getHolder().addCallback(this);
        context = Emulator.createContext();
        paint.setAntiAlias(false);
    }

    /**
     * surface created
     *
     * @param surfaceHolder surface holder
     */
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        NESLogger.d("surface created");
    }

    /**
     * surface changed
     *
     * @param surfaceHolder surface holder
     * @param format        format
     * @param width         width
     * @param height        height
     */
    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        NESLogger.d("surface changed: format=" + format + ", width=" + width + ", height=" + height);
        double xDiv = width / 256.0;
        double yDiv = height / 240.0;
        if (xDiv < yDiv) {
            int h = (int) (240 * xDiv);
            int y = (height - h) / 2;
            viewRect = new Rect(0, y, width, y + h);
        } else {
            int w = (int) (256 * yDiv);
            int x = (width - w) / 2;
            viewRect = new Rect(x, 0, x + w, height);
        }
    }

    /**
     * surface destroyed
     *
     * @param surfaceHolder surface holder
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        viewRect = null;
    }

    @Override
    protected void onDetachedFromWindow() {
        destroy();
        super.onDetachedFromWindow();
    }

    /**
     * explicit destroy function (this function will called when detaching the NESView from the window)
     */
    public void destroy() {
        if (null != context) {
            setOnCaptureAudioListener(null);
            NESLogger.d("destroy nes-view");
            Emulator.releaseContext(context);
            context = null;
        }
    }

    /**
     * load ROM file
     *
     * @param rom binary of the ROM file
     * @return true = succeed, false = failed
     */
    public boolean load(@Nullable byte[] rom) {
        if (null == context || null == rom) return false;
        NESLogger.d("loading rom: size=" + rom.length);
        return Emulator.loadRom(context, rom);
    }

    /**
     * execute 1 frame
     *
     * @param keyCode key code (you can calculate using NESKey)
     */
    public void tick(int keyCode) {
        if (null == context) return;
        // 1フレーム描画されるまでCPUを回す
        synchronized (locker) {
            Emulator.tick(context, keyCode, vram);
        }
        // vramの内容をアスペクト比を保った状態で拡大しつつ画面に描画
        SurfaceHolder holder = getHolder();
        if (null == holder) {
            NESLogger.w("cannot get the holder");
            return;
        }
        Canvas canvas = holder.lockCanvas();
        if (null == canvas) {
            NESLogger.w("cannot lock the holder-canvas");
            return;
        }
        canvas.drawColor(0xff000000);
        if (null == viewRect) {
            NESLogger.w("surface has not initialized");
            return;
        }
        canvas.drawBitmap(vram, vramRect, viewRect, paint);
        holder.unlockCanvasAndPost(canvas);
    }

    /**
     * execute multiple frames
     *
     * @param keyCodes key codes of the every frame
     */
    public void ticks(@NonNull int[] keyCodes) {
        if (null == context) return;
        // nフレーム描画されるまでCPUを回す
        synchronized (locker) {
            Emulator.multipleTicks(context, keyCodes, vram);
        }
        // vramの内容をアスペクト比を保った状態で拡大しつつ画面に描画
        SurfaceHolder holder = getHolder();
        if (null == holder) {
            NESLogger.w("cannot get the holder");
            return;
        }
        Canvas canvas = holder.lockCanvas();
        if (null == canvas) {
            NESLogger.w("cannot lock the holder-canvas");
            return;
        }
        canvas.drawColor(0xff000000);
        if (null == viewRect) {
            NESLogger.w("surface has not initialized");
            return;
        }
        canvas.drawBitmap(vram, vramRect, viewRect, paint);
        holder.unlockCanvasAndPost(canvas);
    }

    /**
     * send H/W reset to the emulator
     */
    public void reset() {
        if (null == context) return;
        Emulator.reset(context);
    }

    /**
     * capturing a video frame
     *
     * @param canvas canvas of the capturing video frame
     * @param rect   draw position of the canvas (recommended aspect rate is 16:15 = width:height)
     */
    public void capture(Canvas canvas, Rect rect) {
        synchronized (locker) {
            canvas.drawBitmap(vram, vramRect, rect, paint);
        }
    }

    /**
     * set the audio capture listener
     *
     * @param listener audio capture listener
     */
    public void setOnCaptureAudioListener(@Nullable OnCaptureAudioListener listener) {
        setOnCaptureAudioListener(listener, 200, null);
    }

    /**
     * set the audio capture listener
     *
     * @param listener audio capture listener
     * @param interval capturing interval
     * @param limit    size of the limit (NOTE: recommended parameter is `null` because the C heap will increasing if the specified value was too small.)
     */
    public void setOnCaptureAudioListener(@Nullable OnCaptureAudioListener listener, int interval, @Nullable Integer limit) {
        if (null == context) return;
        if (null == captureTimer && null == listener) return;
        if (null != captureTimer) {
            captureTimer.cancel();
            captureTimer.purge();
            captureTimer = null;
        }
        onCaptureAudioListener = listener;
        if (null != listener) {
            NESLogger.d("beginning capture audio");
            Emulator.beginCaptureAudio(context);
            final int limitSize = limit != null ? limit : (int) (interval / 1000.0 * 88200 * 2);
            captureTimer = new Timer();
            captureTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    byte[] result = Emulator.getCaptureAudio(context, limitSize);
                    if (null != result) {
                        onCaptureAudioListener.onCaptureAudio(result);
                    }
                }
            }, 0, interval);
        } else {
            NESLogger.d("ending capture audio");
            Emulator.endCaptureAudio(context);
        }
    }
}
