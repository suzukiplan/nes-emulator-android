package com.suzukiplan.emulator.nes.core;

class JNI {
    static {
        System.loadLibrary("nes-core");
    }

    public native String stringFromJNI();
}
