package com.suzukiplan.emulator.nes.core;

final class Emulator {
    static {
        System.loadLibrary("nes-core");
    }

    public static native long createContext();

    public static native void releaseContext(long contextId);
}
