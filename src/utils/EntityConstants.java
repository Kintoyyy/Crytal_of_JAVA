package utils;

public final class EntityConstants {
    // ANIMATIONS
    public static final int ATTACK = 0;
    public static final int IDLE = 1;
    public static final int RUNNING = 2;

    public static final int SKILL_1 = 3;
    public static final int SKILL_2 = 4;
    public static final int SKILL_3 = 5;
    public static final int SKILL_4 = 6;
    public static final int HEALING = 7;
    public static final int LEVEL_UP = 8;
    public static final int LEVEL_DOWN = 9;

    // MOVEMENTS
    public static final String DOWN = "down";
    public static final String UP = "up";
    public static final String LEFT = "left";
    public static final String RIGHT = "right";

    public static int getSpriteAmount(int player_action) {
        return switch (player_action) {
            case IDLE, RUNNING -> 6;
            case SKILL_1, SKILL_2, SKILL_3, SKILL_4 -> 1;
            case HEALING, LEVEL_UP, LEVEL_DOWN, ATTACK  -> 4;
            default -> 1;
        };
    }
}