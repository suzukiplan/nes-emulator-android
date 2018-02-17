package com.suzukiplan.emulator.nes.core;

import android.graphics.Bitmap;

final class Emulator {
    static {
        System.loadLibrary("nes-core");
    }

    public static native long createContext();

    public static native void releaseContext(long contextId);

    public static native boolean loadRom(long contextId, byte[] rom);

    public static native void tick(long contextId, int key, Bitmap vram);

    public static native void multipleTicks(long contextId, int[] keys, Bitmap vram);

    public static native void reset(long contextId);

    public static native boolean beginCaptureAudio(long contextId);

    public static native byte[] getCaptureAudio(long contextId, int limit);

    public static native void endCaptureAudio(long contextId);
}
