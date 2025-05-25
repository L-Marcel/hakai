package app.hakai.backend.models;

public enum Difficulty {
    EASY,
    NORMAL,
    HARD;


    public static int mapDifficultyEnumToInt(Difficulty difficulty) {
        return switch (difficulty) {
            case EASY -> 1;
            case NORMAL -> 2;
            case HARD -> 3;
        };
    }
}