package com.team10.sports;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FootballSportTest {

    @Test
    void shouldReturnCorrectSportName() {
        FootballSport sport = new FootballSport();
        assertEquals("Football", sport.getSportName());
    }

    @Test
    void shouldReturnCorrectLineupSize() {
        FootballSport sport = new FootballSport();
        assertEquals(11, sport.getLineupSize());
    }

    @Test
    void shouldReturnCorrectSubstitutionLimit() {
        FootballSport sport = new FootballSport();
        assertEquals(5, sport.getSubstituteLimit());
    }

    @Test
    void shouldReturnCorrectPointsForWin() {
        FootballSport sport = new FootballSport();
        assertEquals(3, sport.getPointsForWin());
    }

    @Test
    void shouldReturnCorrectPointsForDraw() {
        FootballSport sport = new FootballSport();
        assertEquals(1, sport.getPointsForDraw());
    }

    @Test
    void shouldReturnCorrectPointsForLoss() {
        FootballSport sport = new FootballSport();
        assertEquals(0, sport.getPointsForLoss());
    }

    @Test
    void shouldAcceptValidPlayerCount() {
        FootballSport sport = new FootballSport();
        assertTrue(sport.isValidPlayerCount(11));
    }

    @Test
    void shouldRejectInvalidPlayerCount() {
        FootballSport sport = new FootballSport();
        assertFalse(sport.isValidPlayerCount(10));
    }

    @Test
    void shouldAcceptValidScore() {
        FootballSport sport = new FootballSport();
        assertTrue(sport.isValidScore(2, 1));
    }

    @Test
    void shouldRejectNegativeHomeScore() {
        FootballSport sport = new FootballSport();
        assertFalse(sport.isValidScore(-1, 2));
    }

    @Test
    void shouldRejectNegativeAwayScore() {
        FootballSport sport = new FootballSport();
        assertFalse(sport.isValidScore(2, -1));
    }

    @Test
    void shouldReturnWinPointsWhenScoredMore() {
        FootballSport sport = new FootballSport();
        assertEquals(3, sport.calculatePointsFromScore(3, 1));
    }

    @Test
    void shouldReturnDrawPointsWhenScoresAreEqual() {
        FootballSport sport = new FootballSport();
        assertEquals(1, sport.calculatePointsFromScore(2, 2));
    }

    @Test
    void shouldReturnLossPointsWhenScoredLess() {
        FootballSport sport = new FootballSport();
        assertEquals(0, sport.calculatePointsFromScore(0, 2));
    }

    @Test
    void shouldThrowExceptionForNegativeScoresInPointCalculation() {
        FootballSport sport = new FootballSport();
        assertThrows(IllegalArgumentException.class,
                () -> sport.calculatePointsFromScore(-1, 2));
    }
}