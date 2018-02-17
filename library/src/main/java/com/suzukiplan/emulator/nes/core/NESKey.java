package com.suzukiplan.emulator.nes.core;

/**
 * key code calculator
 */
public class NESKey {
    private static final int MASK_A = 1;
    private static final int MASK_B = 2;
    private static final int MASK_SELECT = 4;
    private static final int MASK_START = 8;
    private static final int MASK_UP = 16;
    private static final int MASK_DOWN = 32;
    private static final int MASK_LEFT = 64;
    private static final int MASK_RIGHT = 128;
    /**
     * key data of the player 1
     */
    public final Player player1 = new Player();
    /**
     * key data of the player 2
     */
    public final Player player2 = new Player();

    public static class Player {
        /**
         * up button
         */
        public boolean up = false;
        /**
         * down button
         */
        public boolean down = false;
        /**
         * left button
         */
        public boolean left = false;
        /**
         * right button
         */
        public boolean right = false;
        /**
         * A button
         */
        public boolean a = false;
        /**
         * B button
         */
        public boolean b = false;
        /**
         * select button
         */
        public boolean select = false;
        /**
         * start button
         */
        public boolean start = false;
    }

    /**
     * get the key code for `NESView#tick` or `NESView#ticks`
     *
     * @return key code
     */
    public int getCode() {
        int code1 = player1.up ? MASK_UP : 0;
        code1 += player1.down ? MASK_DOWN : 0;
        code1 += player1.left ? MASK_LEFT : 0;
        code1 += player1.right ? MASK_RIGHT : 0;
        code1 += player1.a ? MASK_A : 0;
        code1 += player1.b ? MASK_B : 0;
        code1 += player1.select ? MASK_SELECT : 0;
        code1 += player1.start ? MASK_START : 0;
        int code2 = player2.up ? MASK_UP : 0;
        code2 += player2.down ? MASK_DOWN : 0;
        code2 += player2.left ? MASK_LEFT : 0;
        code2 += player2.right ? MASK_RIGHT : 0;
        code2 += player2.a ? MASK_A : 0;
        code2 += player2.b ? MASK_B : 0;
        code2 += player2.select ? MASK_SELECT : 0;
        code2 += player2.start ? MASK_START : 0;
        return code1 + code2 * 256;
    }
}
