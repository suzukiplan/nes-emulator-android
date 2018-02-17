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

public class NESView extends SurfaceView implements SurfaceHolder.Callback {
    private final Bitmap vram = Bitmap.createBitmap(256, 240, Bitmap.Config.RGB_565);
    private final Rect vramRect = new Rect(0, 0, 256, 240);
    private final Object locker = new Object();
    private Rect viewRect = null;
    private final Paint paint = new Paint();
    private Long context = null;
    private OnCaptureAudioListener onCaptureAudioListener = null;
    private Timer captureTimer = null;

    public interface OnCaptureAudioListener {
        void onCaptureAudio(byte[] pcm);
    }

    public NESView(Context context) {
        super(context);
        init();
    }

    public NESView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NESView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Logger.d("create nes-view");
        getHolder().addCallback(this);
        context = Emulator.createContext();
        paint.setAntiAlias(false);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Logger.d("surface created");
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        Logger.d("surface changed: format=" + format + ", width=" + width + ", height=" + height);
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

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        viewRect = null;
    }

    @Override
    protected void onDetachedFromWindow() {
        destroy();
        super.onDetachedFromWindow();
    }

    public void destroy() {
        if (null != context) {
            setOnCaptureAudioListener(null);
            Logger.d("destroy nes-view");
            Emulator.releaseContext(context);
            context = null;
        }
    }

    public boolean load(@Nullable byte[] rom) {
        if (null == context || null == rom) return false;
        Logger.d("loading rom: size=" + rom.length);
        return Emulator.loadRom(context, rom);
    }

    public void tick(int keyCode) {
        if (null == context) return;
        // 1フレーム描画されるまでCPUを回す
        synchronized (locker) {
            Emulator.tick(context, keyCode, vram);
        }
        // vramの内容をアスペクト比を保った状態で拡大しつつ画面に描画
        SurfaceHolder holder = getHolder();
        if (null == holder) {
            Logger.w("cannot get the holder");
            return;
        }
        Canvas canvas = holder.lockCanvas();
        if (null == canvas) {
            Logger.w("cannot lock the holder-canvas");
            return;
        }
        canvas.drawColor(0xff000000);
        if (null == viewRect) {
            Logger.w("surface has not initialized");
            return;
        }
        canvas.drawBitmap(vram, vramRect, viewRect, paint);
        holder.unlockCanvasAndPost(canvas);
    }

    public void ticks(@NonNull int[] keyCodes) {
        if (null == context) return;
        // nフレーム描画されるまでCPUを回す
        synchronized (locker) {
            Emulator.multipleTicks(context, keyCodes, vram);
        }
        // vramの内容をアスペクト比を保った状態で拡大しつつ画面に描画
        SurfaceHolder holder = getHolder();
        if (null == holder) {
            Logger.w("cannot get the holder");
            return;
        }
        Canvas canvas = holder.lockCanvas();
        if (null == canvas) {
            Logger.w("cannot lock the holder-canvas");
            return;
        }
        canvas.drawColor(0xff000000);
        if (null == viewRect) {
            Logger.w("surface has not initialized");
            return;
        }
        canvas.drawBitmap(vram, vramRect, viewRect, paint);
        holder.unlockCanvasAndPost(canvas);
    }

    public void reset() {
        if (null == context) return;
        Emulator.reset(context);
    }

    public void capture(Canvas canvas, Rect rect) {
        synchronized (locker) {
            canvas.drawBitmap(vram, vramRect, rect, paint);
        }
    }

    public void setOnCaptureAudioListener(@Nullable OnCaptureAudioListener listener) {
        setOnCaptureAudioListener(listener, 200, null);
    }

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
            Logger.d("beginning capture audio");
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
            Logger.d("ending capture audio");
            Emulator.endCaptureAudio(context);
        }
    }
}
