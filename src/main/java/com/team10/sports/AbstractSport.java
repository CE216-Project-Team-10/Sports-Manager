package com.team10.sports;

public abstract class AbstractSport implements Sport {

    private final String sportName;
    private final int lineupSize;
    private final int substituteLimit;
    private final int pointsForWin;
    private final int pointsForDraw;
    private final int pointsForLoss;

    public AbstractSport(String sportName, int lineupSize, int substituteLimit,
                         int pointsForWin, int pointsForDraw, int pointsForLoss) {
        this.sportName = sportName;
        this.lineupSize = lineupSize;
        this.substituteLimit = substituteLimit;
        this.pointsForWin = pointsForWin;
        this.pointsForDraw = pointsForDraw;
        this.pointsForLoss = pointsForLoss;
    }

    @Override
    public String getSportName() {
        return sportName;
    }

    @Override
    public int getLineupSize() {
        return lineupSize;
    }

    @Override
    public int getSubstituteLimit() {
        return substituteLimit;
    }

    @Override
    public int getPointsForWin() {
        return pointsForWin;
    }

    @Override
    public int getPointsForDraw() {
        return pointsForDraw;
    }

    @Override
    public int getPointsForLoss() {
        return pointsForLoss;
    }
}