package com.team10.sports;

public class FootballSport extends AbstractSport {

    public FootballSport() {
        super("Football", 11, 5, 3, 1, 0, 2); // 2 yarı
    }

    @Override
    public boolean isValidPlayerCount(int playerCount) {
        return playerCount == getLineupSize();
    }

    @Override
    public boolean isValidScore(int homeScore, int awayScore) {
        return homeScore >= 0 && awayScore >= 0;
    }

    @Override
    public int calculatePointsFromScore(int scored, int conceded) {
        if (!isValidScore(scored, conceded)) {
            throw new IllegalArgumentException("Scores cannot be negative.");
        }
        if (scored > conceded) return getPointsForWin();
        if (scored == conceded) return getPointsForDraw();
        return getPointsForLoss();
    }
}