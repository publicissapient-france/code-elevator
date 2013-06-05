package elevator.server;

import elevator.User;
import elevator.engine.ElevatorEngine;

import static java.lang.Math.abs;
import static java.lang.Math.max;

public class Score {

    private static final int EFFORT_TO_OPEN_AND_CLOSE_DOORS = 2;
    private static final int SCORE_COEF = 2;
    private static final int WAITING_TOP_SCORE = 2 * (ElevatorEngine.HIGHER_FLOOR - ElevatorEngine.LOWER_FLOOR);

    public int score;

    public Score() {
        score = 0;
    }

    public Score loose() {
        score -= 50;
        return this;
    }

    public Score success(User user) {
        score += score(user);
        return this;
    }

    int score(User user) {
        return max(0, bestEffort(user) - effectiveEffort(user)) + waitingScore(user);
    }

    int bestEffort(User user) {
        return abs(user.getFloorToGo() - user.getInitialFloor()) * SCORE_COEF + EFFORT_TO_OPEN_AND_CLOSE_DOORS;
    }

    private int effectiveEffort(User user) {
        return user.getTickToGo();
    }

    int waitingScore(User user) {
        return max(0, WAITING_TOP_SCORE - user.getTickToWait());
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
