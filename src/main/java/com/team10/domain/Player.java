package com.team10.domain;

public class Player {

    private final String name;
    private final String position;
    private int skill;
    private int injuryWeeks;

    public Player(String name, String position, int skill) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Player name cannot be null or blank.");
        }
        if (position == null || position.trim().isEmpty()) {
            throw new IllegalArgumentException("Player position cannot be null or blank.");
        }
        if (skill < 0) {
            throw new IllegalArgumentException("Skill cannot be negative.");
        }

        this.name = name;
        this.position = position;
        this.skill = skill;
        this.injuryWeeks = 0;
    }

    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public int getSkill() {
        return skill;
    }

    public int getInjuryWeeks() {
        return injuryWeeks;
    }

    public boolean isAvailable() {
        return injuryWeeks == 0;
    }

    public boolean isInjured() {
        return injuryWeeks > 0;
    }

    public void improveSkill(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Skill improvement cannot be negative.");
        }
        skill += amount;
    }

    public void injureForWeeks(int weeks) {
        if (weeks <= 0) {
            throw new IllegalArgumentException("Injury weeks must be positive.");
        }
        injuryWeeks = weeks;
    }

    public void recoverOneWeek() {
        if (injuryWeeks > 0) {
            injuryWeeks--;
        }
    }
}