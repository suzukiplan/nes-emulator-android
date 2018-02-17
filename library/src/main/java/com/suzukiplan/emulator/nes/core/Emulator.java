package com.suzukiplan.emulator.nes.core;

import android.graphics.Bitmap;

final class Emulator {
    static {
        System.loadLibrary("nes-core");
    }

    static native long createContext();

    static native void releaseContext(long contextId);

    static native boolean loadRom(long contextId, byte[] rom);

    static native void tick(long contextId, int key, Bitmap vram);

    static native void multipleTicks(long contextId, int[] keys, Bitmap vram);

    static native void reset(long contextId);

    static native boolean beginCaptureAudio(long contextId);

    static native byte[] getCaptureAudio(long contextId, int limit);

    static native void endCaptureAudio(long contextId);
}
