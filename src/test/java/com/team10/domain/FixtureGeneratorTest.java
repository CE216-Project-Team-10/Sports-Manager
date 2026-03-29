package com.team10.domain;

import com.team10.sports.FootballSport;
import com.team10.sports.Sport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FixtureGeneratorTest {

    private Sport football;

    @BeforeEach
    void setUp() {
        football = new FootballSport();
    }

    // --- Hata Durumu Testleri ---

    @Test
    void shouldThrowExceptionForNullTeamList() {
        assertThrows(IllegalArgumentException.class,
                () -> FixtureGenerator.generateFixture(null, football));
    }

    @Test
    void shouldThrowExceptionForSingleTeam() {
        List<Team> teams = new ArrayList<>();
        teams.add(new Team("Only One"));

        assertThrows(IllegalArgumentException.class,
                () -> FixtureGenerator.generateFixture(teams, football));
    }

    @Test
    void shouldThrowExceptionForEmptyTeamList() {
        List<Team> teams = new ArrayList<>();

        assertThrows(IllegalArgumentException.class,
                () -> FixtureGenerator.generateFixture(teams, football));
    }

    // --- Çift Sayıda Takım Testleri ---

    @Test
    void shouldGenerateCorrectWeekCountForFourTeams() {
        // 4 takım → (4-1)*2 = 6 hafta
        List<Team> teams = createTeams(4);
        List<List<Match>> fixture = FixtureGenerator.generateFixture(teams, football);

        assertEquals(6, fixture.size());
    }

    @Test
    void shouldGenerateCorrectMatchesPerWeekForFourTeams() {
        // 4 takım → her hafta 2 maç
        List<Team> teams = createTeams(4);
        List<List<Match>> fixture = FixtureGenerator.generateFixture(teams, football);

        for (List<Match> week : fixture) {
            assertEquals(2, week.size());
        }
    }

    @Test
    void shouldGenerateCorrectWeekCountForTwoTeams() {
        // 2 takım → (2-1)*2 = 2 hafta
        List<Team> teams = createTeams(2);
        List<List<Match>> fixture = FixtureGenerator.generateFixture(teams, football);

        assertEquals(2, fixture.size());
    }

    @Test
    void shouldGenerateCorrectMatchesPerWeekForTwoTeams() {
        // 2 takım → her hafta 1 maç
        List<Team> teams = createTeams(2);
        List<List<Match>> fixture = FixtureGenerator.generateFixture(teams, football);

        for (List<Match> week : fixture) {
            assertEquals(1, week.size());
        }
    }

    // --- Tek Sayıda Takım Testleri (BYE) ---

    @Test
    void shouldHandleOddNumberOfTeams() {
        // 3 takım → BYE eklenir → 4'e tamamlanır → 6 hafta
        List<Team> teams = createTeams(3);
        List<List<Match>> fixture = FixtureGenerator.generateFixture(teams, football);

        assertNotNull(fixture);
        assertEquals(6, fixture.size());
    }

    @Test
    void shouldNotContainByeMatchesForOddTeams() {
        // BYE takımı içeren maçlar fikstüre eklenmemeli
        List<Team> teams = createTeams(3);
        List<List<Match>> fixture = FixtureGenerator.generateFixture(teams, football);

        for (List<Match> week : fixture) {
            for (Match match : week) {
                assertNotEquals("BYE", match.getHomeTeam().getName());
                assertNotEquals("BYE", match.getAwayTeam().getName());
            }
        }
    }

    // --- Maç İçerik Testleri ---

    @Test
    void shouldNotHaveTeamPlayingItself() {
        List<Team> teams = createTeams(4);
        List<List<Match>> fixture = FixtureGenerator.generateFixture(teams, football);

        for (List<Match> week : fixture) {
            for (Match match : week) {
                assertNotEquals(match.getHomeTeam(), match.getAwayTeam());
            }
        }
    }

    @Test
    void shouldHaveEachPairPlayTwice() {
        // Her takım çifti bir kez ev sahibi, bir kez deplasman olarak oynamalı
        List<Team> teams = createTeams(4);
        List<List<Match>> fixture = FixtureGenerator.generateFixture(teams, football);

        // Her takımın toplam maç sayısı: (n-1)*2 = 6
        for (Team team : teams) {
            int matchCount = 0;
            for (List<Match> week : fixture) {
                for (Match match : week) {
                    if (match.getHomeTeam().equals(team) || match.getAwayTeam().equals(team)) {
                        matchCount++;
                    }
                }
            }
            assertEquals(6, matchCount);
        }
    }

    @Test
    void shouldNotHaveDuplicateMatchesInSameWeek() {
        // Aynı haftada bir takım birden fazla maç oynamamalı
        List<Team> teams = createTeams(4);
        List<List<Match>> fixture = FixtureGenerator.generateFixture(teams, football);

        for (List<Match> week : fixture) {
            Set<Team> teamsThisWeek = new HashSet<>();
            for (Match match : week) {
                assertFalse(teamsThisWeek.contains(match.getHomeTeam()),
                        "Team plays twice in same week: " + match.getHomeTeam().getName());
                assertFalse(teamsThisWeek.contains(match.getAwayTeam()),
                        "Team plays twice in same week: " + match.getAwayTeam().getName());
                teamsThisWeek.add(match.getHomeTeam());
                teamsThisWeek.add(match.getAwayTeam());
            }
        }
    }

    @Test
    void shouldHaveBothHomeAndAwayForEachPair() {
        // Her çift hem ev hem deplasman oynamalı (ters maç da olmalı)
        List<Team> teams = createTeams(4);
        List<List<Match>> fixture = FixtureGenerator.generateFixture(teams, football);

        for (int i = 0; i < teams.size(); i++) {
            for (int j = i + 1; j < teams.size(); j++) {
                Team t1 = teams.get(i);
                Team t2 = teams.get(j);

                boolean t1Home = false, t2Home = false;

                for (List<Match> week : fixture) {
                    for (Match match : week) {
                        if (match.getHomeTeam().equals(t1) && match.getAwayTeam().equals(t2)) t1Home = true;
                        if (match.getHomeTeam().equals(t2) && match.getAwayTeam().equals(t1)) t2Home = true;
                    }
                }

                assertTrue(t1Home, t1.getName() + " never plays at home vs " + t2.getName());
                assertTrue(t2Home, t2.getName() + " never plays at home vs " + t1.getName());
            }
        }
    }

    @Test
    void shouldReturnNonNullFixture() {
        List<Team> teams = createTeams(4);
        List<List<Match>> fixture = FixtureGenerator.generateFixture(teams, football);

        assertNotNull(fixture);
        for (List<Match> week : fixture) {
            assertNotNull(week);
        }
    }

    // --- Yardımcı Metod ---

    private List<Team> createTeams(int count) {
        List<Team> teams = new ArrayList<>();
        String[] names = {"Eagles", "Lions", "Falcons", "Panthers", "Tigers", "Wolves"};
        for (int i = 0; i < count; i++) {
            teams.add(new Team(names[i]));
        }
        return teams;
    }
}