package com.company;

public class RandomPlayer implements Player{

    public int move(int opponentLastMove, int xA, int xB, int xC) {
        return (int)(Math.random()*2) + 1;
    }

    public void reset() {}


}
