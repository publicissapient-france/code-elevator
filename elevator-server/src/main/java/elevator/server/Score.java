package elevator.server;

import elevator.User;

public class Score {
    public static final int EFFORT_TO_OPEN_AND_CLOSE_DOORS = 2;
    public static final int GAME_COEF = 2;
    public final int score;

    public Score() {
        score = 0;
    }

    private Score(int score) {
        this.score = score;
    }

    public Score loose() {
        return new Score(this.score - 1000);
    }

    public Score reset() {
        return new Score();
    }

    public Score success(User user) {
        System.out.println(String.format("Best effort:%d, effective:%d", bestEffort(user), effectiveEffort(user)));
        return new Score(score + Math.abs(bestEffort(user) - effectiveEffort(user)));
    }

    int bestEffort(User user) {
        return (Math.max(0, (user.getFloorToGo() - user.getFloor()) + EFFORT_TO_OPEN_AND_CLOSE_DOORS) * GAME_COEF);
    }

    private int effectiveEffort(User user) {
        return user.getTickToGo();
    }
}
