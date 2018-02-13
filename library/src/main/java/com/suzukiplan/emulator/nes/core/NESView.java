package com.suzukiplan.emulator.nes.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class NESView extends SurfaceView implements SurfaceHolder.Callback {
    private final Bitmap vram = Bitmap.createBitmap(256, 240, Bitmap.Config.RGB_565);
    private final Rect vramRect = new Rect(0, 0, 256, 240);
    private Rect viewRect = null;
    private final Paint paint = new Paint();
    private Long context = null;

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
            Logger.d("destroy nes-view");
            Emulator.releaseContext(context);
            context = null;
        }
    }

    public boolean load(byte[] rom) {
        Logger.d("loading rom: size=" + rom.length);
        return Emulator.loadRom(context, rom);
    }

    public void tick(int keyCodeP1, int keyCodeP2) {
        // 1フレーム描画されるまでCPUを回す
        Emulator.tick(context, keyCodeP1, keyCodeP2, vram);
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
        Emulator.reset(context);
    }

    public void capture(Canvas canvas, Rect rect) {
        canvas.drawBitmap(vram, vramRect, rect, paint);
    }
}
