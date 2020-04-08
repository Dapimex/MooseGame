package com.company;

public class GreedyPlayer implements Player {

    public int move(int opponentLastMove, int xA, int xB, int xC) {
        int maxField = Math.max(xA, Math.max(xB, xC));
        if (maxField == xA) return 1;
        if (maxField == xB) return 2;
        return 3;
    }

    public void reset() {}

}
