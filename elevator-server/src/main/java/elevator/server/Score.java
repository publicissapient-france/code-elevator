package elevator.server;

import elevator.user.User;

import java.time.Duration;
import java.time.LocalDateTime;

import static java.lang.Math.*;
import static java.time.LocalDateTime.now;

class Score {

    Integer score;
    LocalDateTime started = now();

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
        return this;
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
        return getAverageScore(now());
    }

    int getAverageScore(LocalDateTime now) {
        long elapsed = Duration.between(started, now).getSeconds();
        return (int) (score * (15f * 60 / elapsed));
    }

}
