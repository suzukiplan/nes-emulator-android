package com.suzukiplan.emulator.nes.core;

public class Emulator {
    private final JNI jni = new JNI();

    public String stringFromJNI() {
        return jni.stringFromJNI();
    }
}
