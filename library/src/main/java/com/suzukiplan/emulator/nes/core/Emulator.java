package com.suzukiplan.emulator.nes.core;

import android.graphics.Bitmap;

final class Emulator {
    static {
        System.loadLibrary("nes-core");
    }

    public static native long createContext();

    public static native void releaseContext(long contextId);

    public static native boolean loadRom(long contextId, byte[] rom);

    public static native void tick(long contextId, int key1, int key2, Bitmap vram);

    public static native void multipleTicks(long contextId, int[] key1, int[] key2, Bitmap vram);

    public static native void reset(long contextId);
}
