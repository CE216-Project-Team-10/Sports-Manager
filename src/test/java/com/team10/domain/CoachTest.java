package com.team10.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CoachTest {

    @Test
    void shouldCreateCoachWithCorrectValues() {
        Coach coach = new Coach("Kaya", 4);

        assertEquals("Kaya", coach.getName());
        assertEquals(4, coach.getTrainingBonus());
    }

    @Test
    void shouldThrowExceptionForBlankCoachName() {
        assertThrows(IllegalArgumentException.class,
                () -> new Coach("", 3));
    }

    @Test
    void shouldThrowExceptionForNegativeTrainingBonus() {
        assertThrows(IllegalArgumentException.class,
                () -> new Coach("Kaya", -1));
    }

    @Test
    void shouldTrainPlayerAndIncreaseSkill() {
        Coach coach = new Coach("Kaya", 3);
        Player player = new Player("Ali", "Midfielder", 70);

        coach.trainPlayer(player);

        assertEquals(73, player.getSkill());
    }

    @Test
    void shouldThrowExceptionWhenTrainingNullPlayer() {
        Coach coach = new Coach("Kaya", 3);

        assertThrows(IllegalArgumentException.class,
                () -> coach.trainPlayer(null));
    }
}