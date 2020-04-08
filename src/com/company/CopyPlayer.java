package com.company;

public class CopyPlayer implements Player {

    public int move(int opponentLastMove, int xA, int xB, int xC) {
        if (opponentLastMove == 0) return (int)(Math.random()*2) + 1;
        return opponentLastMove;
    }

    public void reset() {}

}
