package com.team10.domain;

import com.team10.sports.Sport;
import java.util.ArrayList;
import java.util.List;

public class FixtureGenerator {

    /**
     * ev sahibi ve deplasman lig fikstürü oluştur
     */
    public static List<List<Match>> generateFixture(List<Team> teams, Sport sport) {
        if (teams == null || teams.size() < 2) {
            throw new IllegalArgumentException("At least 2 teams are required to generate a fixture.");
        }

        List<Team> fixtureTeams = new ArrayList<Team>(teams);

        // Takım sayısı tek ise Bye geçme durumu için sahte bir takım ekleme
        // Dökümanda 20 takım diyor ama algoritmamız her duruma hazır
        if (fixtureTeams.size() % 2 != 0) {
            fixtureTeams.add(new Team("BYE"));
        }

        int numTeams = fixtureTeams.size();
        int totalWeeks = numTeams - 1;
        int matchesPerWeek = numTeams / 2;

        List<List<Match>> firstHalfFixture = new ArrayList<List<Match>>();
        List<List<Match>> secondHalfFixture = new ArrayList<List<Match>>();

        // İlk yarı Round-Robin
        for (int week = 0; week < totalWeeks; week++) {
            List<Match> weekMatches = new ArrayList<Match>();
            List<Match> secondHalfWeekMatches = new ArrayList<Match>();

            for (int match = 0; match < matchesPerWeek; match++) {
                int homeIndex = (week + match) % (numTeams - 1);
                int awayIndex = (numTeams - 1 - match + week) % (numTeams - 1);

                // Son takım sabit Round-Robin
                if (match == 0) {
                    awayIndex = numTeams - 1;
                }

                Team homeTeam = fixtureTeams.get(homeIndex);
                Team awayTeam = fixtureTeams.get(awayIndex);

                // Eğer takımlardan biri BYE ise o hafta maç yapmaz
                if (!homeTeam.getName().equals("BYE") && !awayTeam.getName().equals("BYE")) {
                    // İlk yarı
                    weekMatches.add(new Match(homeTeam, awayTeam, sport));
                    // İkinci yarı (yer değiştirirler)
                    secondHalfWeekMatches.add(new Match(awayTeam, homeTeam, sport));
                }
            }
            firstHalfFixture.add(weekMatches);
            secondHalfFixture.add(secondHalfWeekMatches);
        }

        // İlk yarı ve ikinci yarıyı birleştir
        List<List<Match>> fullSeasonFixture = new ArrayList<List<Match>>();
        fullSeasonFixture.addAll(firstHalfFixture);
        fullSeasonFixture.addAll(secondHalfFixture);

        return fullSeasonFixture;
    }
}