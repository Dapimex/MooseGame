package com.company;

import java.util.ArrayList;
import java.util.List;

public class DenisPimenovCode implements Player {

    /*
    Common idea: keep tracking of the opponent's moves and calculate probabilities
    of each of his move based on the entire history. Calculate the expected payoffs
    for each of your moves, given the current mixed strategy of the opponent and
    possible payoffs from each field. Choose the best move by expected payoffs.
    Choose random between equal moves in order to be unpredictable and prevent unnecessary collisions.
    This also prevents from collisions with player with the same strategy
     */

    // count opponent's moves in each fields and overall
    private int counterA = 0;
    private int counterB = 0;
    private int counterC = 0;
    private int counterAll = 0;

    public void reset() {
        counterA = 0;
        counterB = 0;
        counterC = 0;
        counterAll = 0;
    }

    public int move(int opponentLastMove, int xA, int xB, int xC) {

        // update counter's after opponent's move
        switch (opponentLastMove) {
            case 1: {
                counterA++;
                counterAll++;
                break;
            }
            case 2: {
                counterB++;
                counterAll++;
                break;
            }
            case 3: {
                counterC++;
                counterAll++;
                break;
            }
            case 0: {
                counterA = 1;
                counterB = 1;
                counterC = 1;
                counterAll = 3;
                break;
            }
            default: break;
        }

        // count probabilities of opponent moving to each of the fields
        double probabilityA = (double) counterA / counterAll;
        double probabilityB = (double) counterB / counterAll;
        double probabilityC = (double) counterC / counterAll;

        // calculate expected outcome as potential outcome of the field multiplied
        // on probability of opponent not to step on the same field
        double expectedOutcomeA = (probabilityB + probabilityC) * calculateOutcome(xA);
        double expectedOutcomeB = (probabilityA + probabilityC) * calculateOutcome(xB);
        double expectedOutcomeC = (probabilityA + probabilityB) * calculateOutcome(xC);

        // find maximum value and save all fields with maximum value
        double maxValue = Math.max(expectedOutcomeA, Math.max(expectedOutcomeB, expectedOutcomeC));
        List<Integer> randomizer = new ArrayList<>();
        if (maxValue == expectedOutcomeA) randomizer.add(1);
        if (maxValue == expectedOutcomeB) randomizer.add(2);
        if (maxValue == expectedOutcomeC) randomizer.add(3);

        // choose random field out of the best (if there are more than 1 equal fields)
        return randomizer.get((int) (Math.random()*(randomizer.size() - 0.001)));
    }

    // function f(x) - f(0) = potential outcome of the field
    private double calculateOutcome(int x) {
        return f(x) - f(0);
    }

    // function f(x)
    private double f(int x) {
        return (10*Math.exp(x))/(1+Math.exp(x));
    }

}
