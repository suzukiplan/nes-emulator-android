package com.suzukiplan.emulator.nes.core;

public class Emulator {
    static {
        System.loadLibrary("nes-core");
    }

    public static native String stringFromJNI();

    public static native long createContext();

    public static native void releaseContext(long contextId);
}
