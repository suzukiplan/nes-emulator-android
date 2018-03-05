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

    /**
     * player key status
     */
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

    /**
     * set the key code for `NESView#tick` or `NESView#ticks`
     */
    public void setCode(int code) {
        int code1 = code & 0xff;
        int code2 = (code & 0x00ff) >> 8;
        player1.up = 0 != (code1 & MASK_UP);
        player1.down = 0 != (code1 & MASK_DOWN);
        player1.left = 0 != (code1 & MASK_LEFT);
        player1.right = 0 != (code1 & MASK_RIGHT);
        player1.a = 0 != (code1 & MASK_A);
        player1.b = 0 != (code1 & MASK_B);
        player1.select = 0 != (code1 & MASK_SELECT);
        player1.start = 0 != (code1 & MASK_START);
        player2.up = 0 != (code2 & MASK_UP);
        player2.down = 0 != (code2 & MASK_DOWN);
        player2.left = 0 != (code2 & MASK_LEFT);
        player2.right = 0 != (code2 & MASK_RIGHT);
        player2.a = 0 != (code2 & MASK_A);
        player2.b = 0 != (code2 & MASK_B);
        player2.select = 0 != (code2 & MASK_SELECT);
        player2.start = 0 != (code2 & MASK_START);
    }
}
