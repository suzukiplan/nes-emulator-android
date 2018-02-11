package com.suzukiplan.emulator.nes.core;

import android.graphics.Bitmap;

final class Emulator {
    static {
        System.loadLibrary("nes-core");
    }

    public static native long createContext();

    public static native void releaseContext(long contextId);

    public static native void loadRom(long contextId, byte[] rom);

    public static native void tick(long contextId, int key1, int key2, Bitmap vram);
}
