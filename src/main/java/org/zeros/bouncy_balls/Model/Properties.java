package org.zeros.bouncy_balls.Model;

public class Properties {
    private static int GAME_HEIGHT =700;

    private static int GAME_WIDTH =700;

    private static double FRAME_RATE =60;

    public static double getFRAME_RATE() {
        return FRAME_RATE;
    }

    public static int getGAME_HEIGHT() {
        return GAME_HEIGHT;
    }

    public static void setGAME_HEIGHT(int GAME_HEIGHT) {
        Properties.GAME_HEIGHT = GAME_HEIGHT;
    }

    public static int getGAME_WIDTH() {
        return GAME_WIDTH;
    }

    public static void setGAME_WIDTH(int GAME_WIDTH) {
        Properties.GAME_WIDTH = GAME_WIDTH;
    }


}
