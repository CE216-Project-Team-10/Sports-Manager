package com.team10.domain;

import com.team10.sports.FootballSport;
import com.team10.sports.Sport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LeagueTest {

    private Sport football;
    private Team teamA;
    private Team teamB;
    private Team teamC;
    private Team teamD;
    private List<Team> teams;

    @BeforeEach
    void setUp() {
        football = new FootballSport();

        teamA = new Team("Team A");
        teamB = new Team("Team B");
        teamC = new Team("Team C");
        teamD = new Team("Team D");

        teams = new ArrayList<>();
        teams.add(teamA);
        teams.add(teamB);
        teams.add(teamC);
        teams.add(teamD);

        // Her takıma 11 oyuncu ekle
        for (Team team : teams) {
            team.setCoach(new Coach(team.getName() + " Coach", 5));
            for (int i = 1; i <= 11; i++) {
                team.addPlayer(new Player(team.getName() + " P" + i, "Forward", 70));
            }
        }
    }

    // --- Constructor Testleri ---

    @Test
    void shouldCreateLeagueSuccessfully() {
        League league = new League(teams, football);

        assertNotNull(league);
        assertFalse(league.isLeagueFinished());
        assertEquals(4, league.getTeams().size());
    }

    @Test
    void shouldThrowExceptionForNullTeams() {
        assertThrows(IllegalArgumentException.class,
                () -> new League(null, football));
    }

    @Test
    void shouldThrowExceptionForTooFewTeams() {
        List<Team> singleTeam = new ArrayList<>();
        singleTeam.add(teamA);

        assertThrows(IllegalArgumentException.class,
                () -> new League(singleTeam, football));
    }

    @Test
    void shouldThrowExceptionForNullSport() {
        assertThrows(IllegalArgumentException.class,
                () -> new League(teams, null));
    }

    // --- Hafta ve Fikstür Testleri ---

    @Test
    void shouldStartAtWeekOne() {
        League league = new League(teams, football);
        assertEquals(1, league.getCurrentWeek());
    }

    @Test
    void shouldAdvanceWeekAfterPlayNextWeek() {
        League league = new League(teams, football);
        int before = league.getCurrentWeek();
        league.playNextWeek();
        assertEquals(before + 1, league.getCurrentWeek());
    }

    @Test
    void shouldFinishAfterAllWeeksPlayed() {
        League league = new League(teams, football);

        while (!league.isLeagueFinished()) {
            league.playNextWeek();
        }

        assertTrue(league.isLeagueFinished());
    }

    @Test
    void shouldThrowExceptionWhenPlayingAfterLeagueFinished() {
        League league = new League(teams, football);

        while (!league.isLeagueFinished()) {
            league.playNextWeek();
        }

        assertThrows(IllegalStateException.class, league::playNextWeek);
    }

    @Test
    void shouldPlayCorrectNumberOfWeeks() {
        // 4 takım → ilk tur: 3 hafta, ikinci tur: 3 hafta → toplam 6 hafta
        League league = new League(teams, football);
        int weekCount = 0;

        while (!league.isLeagueFinished()) {
            league.playNextWeek();
            weekCount++;
        }

        assertEquals(6, weekCount);
    }

    // --- Standings Testleri ---

    @Test
    void shouldReturnStandingsForAllTeams() {
        League league = new League(teams, football);
        List<TeamRecord> standings = league.getSortedStandings();

        assertEquals(4, standings.size());
    }

    @Test
    void shouldHaveCorrectMatchesPlayedAfterFullSeason() {
        League league = new League(teams, football);

        while (!league.isLeagueFinished()) {
            league.playNextWeek();
        }

        // 4 takımlı ligde her takım (4-1)*2 = 6 maç oynar
        for (TeamRecord record : league.getSortedStandings()) {
            assertEquals(6, record.getMatchesPlayed());
        }
    }

    @Test
    void shouldRankTeamsByPointsDescending() {
        League league = new League(teams, football);

        while (!league.isLeagueFinished()) {
            league.playNextWeek();
        }

        List<TeamRecord> sorted = league.getSortedStandings();

        for (int i = 0; i < sorted.size() - 1; i++) {
            assertTrue(sorted.get(i).getPoints() >= sorted.get(i + 1).getPoints());
        }
    }

    @Test
    void shouldHaveConsistentWinsDrawsLosses() {
        League league = new League(teams, football);

        while (!league.isLeagueFinished()) {
            league.playNextWeek();
        }

        for (TeamRecord record : league.getSortedStandings()) {
            assertEquals(record.getMatchesPlayed(),
                    record.getWins() + record.getDraws() + record.getLosses());
        }
    }

    @Test
    void shouldHaveCorrectTotalGoalsSymmetry() {
        // Tüm takımların attığı toplam gol == yendiği toplam gol
        League league = new League(teams, football);

        while (!league.isLeagueFinished()) {
            league.playNextWeek();
        }

        int totalScored = 0;
        int totalConceded = 0;

        for (TeamRecord record : league.getSortedStandings()) {
            totalScored += record.getScored();
            totalConceded += record.getConceded();
        }

        assertEquals(totalScored, totalConceded);
    }

    @Test
    void shouldHaveNonNegativePointsForAllTeams() {
        League league = new League(teams, football);

        while (!league.isLeagueFinished()) {
            league.playNextWeek();
        }

        for (TeamRecord record : league.getSortedStandings()) {
            assertTrue(record.getPoints() >= 0);
        }
    }

    // --- İyileşme Testi ---

    @Test
    void shouldRecoverInjuredPlayersEachWeek() {
        League league = new League(teams, football);

        // Takım A'nın ilk oyuncusunu sakatlayalım
        Player injuredPlayer = teamA.getPlayers().get(0);
        injuredPlayer.injureForMatches(3);

        assertEquals(3, injuredPlayer.getInjuryMatches());

        league.playNextWeek(); // 1 hafta geçti

        assertEquals(2, injuredPlayer.getInjuryMatches());
    }
}