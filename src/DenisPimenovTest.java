import com.company.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class DenisPimenovTest {


    // load players into the game (should be at least 5 players to play, more for evolution process)
    // this will not work without all the classes, but is customizable, so that you can load players you have
    private static ArrayList<Player> initPlayers() {
        ArrayList<Player> players = new ArrayList<>();
        // load 20 players of each type available
        for (int i = 0; i < 20; i++) players.add(new GreedyPlayer());
        for (int i = 0; i < 20; i++) players.add(new RandomPlayer());
        for (int i = 0; i < 20; i++) players.add(new CopyPlayer());
        for (int i = 0; i < 20; i++) players.add(new GentlePlayer());
        for (int i = 0; i < 20; i++) players.add(new DenisPimenovCode());
        return players;
    }

    // run the entire tournament
    private static void runTournament(ArrayList<Player> players) {
        int tournamentRounds = 100; // how many rounds will be played
        int gameCounter = 0;        // current move
        double[] scores = new double[players.size()];   // score list

        while (gameCounter < tournamentRounds) {
            gameCounter++;
            scores = new double[players.size()];
            // make each player to play with all other players once
            for (int i = 0; i < players.size() - 1; i++) {
                for (int j = i + 1; j < players.size(); j++) {
                    double[] results = runGame(players.get(i), players.get(j)); // run game between i-th and j-th player
                    // update scores per round
                    scores[i] += results[0];
                    scores[j] += results[1];
                }
            }

            // prevent evolution on the last round
            if (gameCounter == tournamentRounds) break;

            // sort players to find best and worst
            players = sortPlayers(players, scores);

            // remove 5 worst and copy 5 best players
            for (int i = 0; i < 5; i++) {
                players.remove(players.size() - 1 - i);
                try {
                    players.add(players.get(i).getClass().getDeclaredConstructor().newInstance());
                } catch (InstantiationException | IllegalAccessException |
                        NoSuchMethodException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

        // tournament results printed: players remain and their scores at the last round
        System.out.println("Played rounds: " + gameCounter);
        System.out.println();
        System.out.println("Leaderboard:");
        players = sortPlayers(players, scores);
        Object[] newScores = Arrays.stream(scores).boxed().toArray();
        Arrays.sort(newScores, Collections.reverseOrder());
        for (int i = 0; i < players.size(); i++) {
            System.out.println("    " + players.get(i).getClass().getName() + " : " + newScores[i]);
        }
    }

    // run individual game, return score of each of 2 players
    private static double[] runGame(Player player1, Player player2) {
        int rounds = 150;                   // amount of moves made in the game
        int player1LastMove = 0;
        int player2LastMove = 0;
        int[] fields = {1, 1, 1};           // represents current condition of the fields
        double[] results = new double[2];   // scores of players


        for (int move = 0; move < rounds; move++) {
            // get new move
            int player1CurrentMove = player1.move(player2LastMove, fields[0], fields[1], fields[2]);
            int player2CurrentMove = player2.move(player1LastMove, fields[0], fields[1], fields[2]);
            player1LastMove = player1CurrentMove;
            player2LastMove = player2CurrentMove;


            // update scores of mooses
            if (player1LastMove != player2LastMove) {
                results[0] += f(fields[player1LastMove - 1]) - f(0);
                results[1] += f(fields[player2LastMove - 1]) - f(0);
            }

            // update fields' values
            for (int i = 0; i < 3; i++) {
                if (player1LastMove == i + 1 || player2LastMove == i + 1) fields[i] = Math.max(0, fields[i] - 1);
                else fields[i]++;
            }
        }
        return results;
    }

    // sorts players from best to worst by score
    private static ArrayList<Player> sortPlayers(ArrayList<Player> players, double[] scores) {
        ArrayList<Player> sortedPlayers = new ArrayList<>();
        double[] scoresCopy = new double[scores.length];    // make copy to not clean the original scores
        System.arraycopy(scores, 0, scoresCopy, 0, scores.length);

        for (int i = 0; i < players.size(); i++) {
            double score = -1;
            int index = 0;
            // find maximum and add to sorted list, clean his score
            for (int j = 0; j < players.size(); j++) {
                if (scoresCopy[j] > score) {
                    score = scoresCopy[j];
                    index = j;
                }
            }
            sortedPlayers.add(players.get(index));
            scoresCopy[index] = 0;
        }
        return sortedPlayers;
    }

    // function f(x)
    private static double f(int x) {
        return (10*Math.exp(x))/(1+Math.exp(x));
    }

    // initialize players and run the tournament
    public static void main(String[] args) {
        ArrayList<Player> players = initPlayers();
        runTournament(players);
    }

}
