package com.suzukiplan.emulator.nes.core;

public class NESKey {
    private static final int MASK_A = 1;
    private static final int MASK_B = 2;
    private static final int MASK_SELECT = 4;
    private static final int MASK_START = 8;
    private static final int MASK_UP = 16;
    private static final int MASK_DOWN = 32;
    private static final int MASK_LEFT = 64;
    private static final int MASK_RIGHT = 128;

    public boolean up = false;
    public boolean down = false;
    public boolean left = false;
    public boolean right = false;
    public boolean a = false;
    public boolean b = false;
    public boolean select = false;
    public boolean start = false;

    public int getCode() {
        int code = up ? MASK_UP : 0;
        code += down ? MASK_DOWN : 0;
        code += left ? MASK_LEFT : 0;
        code += right ? MASK_RIGHT : 0;
        code += a ? MASK_A : 0;
        code += b ? MASK_B : 0;
        code += select ? MASK_SELECT : 0;
        code += start ? MASK_START : 0;
        return code;
    }
}
