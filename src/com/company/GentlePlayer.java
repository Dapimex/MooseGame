package com.company;

public class GentlePlayer implements Player {

    public int move(int opponentLastMove, int xA, int xB, int xC) {
        int maxField = Math.max(xA, Math.max(xB, xC));
        if (maxField == xA) {
            if (xB > xC) return 2;
            else return 3;
        }
        if (maxField == xB) {
            if (xA > xC) return 1;
            else return 3;
        }
        else {
            if (xA > xB) return 1;
            else return 2;
        }
    }

    public void reset() {}

}
