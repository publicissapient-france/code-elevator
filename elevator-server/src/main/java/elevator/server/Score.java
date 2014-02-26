package elevator.server;

import elevator.user.User;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.Observable;

import static java.lang.Math.*;

class Score extends Observable {

    Integer score;
    DateTime started = new DateTime();

    Score() {
        this(0);
    }

    Score(Integer initialValue) {
        score = initialValue;
    }

    Score loose() {
        score -= 10;
        return this;
    }

    Score success(User user) throws IllegalStateException {
        score += score(user);
        if (isYoungerThan15Minutes()) {
            setChanged();
            notifyObservers();
        }
        return this;
    }

    private boolean isYoungerThan15Minutes() {
        return started.plusMinutes(15).isAfterNow();
    }

    private Integer score(User user) throws IllegalStateException {
        Integer bestTickToGo = bestTickToGo(user.getInitialFloor(), user.getFloorToGo());
        if (user.getTickToGo() < bestTickToGo) {
            throw new IllegalStateException("when done, user have to wait at least minimum amount of ticks");
        }
        Integer score = 20
                - user.getTickToWait() / 2
                - user.getTickToGo()
                + bestTickToGo;
        return min(max(0, score), 20);
    }

    private Integer bestTickToGo(Integer floor, Integer floorToGo) {
        // elevator is OPEN at floor
        final Integer elevatorHasToCloseDoorsWhenAtFloor = 1;
        final Integer elevatorGoesStraightFromFloorToFloorToGo = abs(floorToGo - floor);
        final Integer elevatorHasToOpenDoorsWhenAtFloorToGo = 1;

        return elevatorHasToCloseDoorsWhenAtFloor
                + elevatorGoesStraightFromFloorToFloorToGo
                + elevatorHasToOpenDoorsWhenAtFloorToGo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Score score1 = (Score) o;

        return score.equals(score1.score);
    }

    @Override
    public int hashCode() {
        return score;
    }

    int getAverageScore() {
        DateTime now = new DateTime();
        return getAverageScore(now);
    }

    protected int getAverageScore(DateTime now) {
        long elapsed = (int) new Duration(started, now).getStandardSeconds();
        return (int) (score * (15f * 60 / elapsed));
    }

}
