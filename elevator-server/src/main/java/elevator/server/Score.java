package elevator.server;

import elevator.User;

import static java.lang.Math.abs;
import static java.lang.Math.max;

public class Score {
    private static final int EFFORT_TO_OPEN_AND_CLOSE_DOORS = 2;
    private static final int SCORE_COEF = 2;
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
        return new Score(score + score(user));
    }

    int score(User user) {
        return max(0, bestEffort(user) - effectiveEffort(user));
    }

    int bestEffort(User user) {
        return abs(user.getFloorToGo() - user.getFloor()) * SCORE_COEF + EFFORT_TO_OPEN_AND_CLOSE_DOORS;
    }

    private int effectiveEffort(User user) {
        return user.getTickToGo();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Score score1 = (Score) o;

        return score == score1.score;
    }

    @Override
    public int hashCode() {
        return score;
    }
}
