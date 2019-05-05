package com.example.vlad.pentago;

public class Field {

    private int player;
    private int countOfBall;
    public int colourOfCell[][] = new int[6][6];

    Field() {
        for (int i = 0; i < 6; i++)
            for (int j = 0; j < 6; j++)
                colourOfCell[i][j] = 0;
        player = 1;
        countOfBall = 0;
    }

    boolean isWin(int colourOfPlayer) {

        for (int i = 0; i < 6; i++)
            for (int j = 0; j < 2; j++) {
                int countOfTheSame = 0;
                if (colourOfPlayer == colourOfCell[i][j])
                    for (int t = j; t < j + 5; t++)
                        if (colourOfCell[i][j] == colourOfCell[i][t]) countOfTheSame++;
                if (countOfTheSame == 5) return true;
            }
        for (int i = 0; i < 2; i++)
            for (int j = 0; j < 6; j++) {
                int countOfTheSame = 0;
                if (colourOfPlayer == colourOfCell[i][j])
                    for (int t = i; t < i + 5; t++)
                        if (colourOfCell[i][j] == colourOfCell[t][j]) countOfTheSame++;
                if (countOfTheSame == 5) return true;
            }
        for (int i = 0; i < 2; i++)
            for (int j = 0; j < 2; j++) {
                int countOfTheSame = 0;
                if (colourOfPlayer == colourOfCell[i][j])
                    for (int t = 0; t < 5; t++)
                        if (colourOfCell[i][j] == colourOfCell[i + t][j + t]) countOfTheSame++;
                if (countOfTheSame == 5) return true;
            }
        for (int i = 4; i < 6; i++)
            for (int j = 0; j < 2; j++) {
                int countOfTheSame = 0;
                if (colourOfPlayer == colourOfCell[i][j])
                    for (int t = 0; t < 5; t++)
                        if (colourOfCell[i][j] == colourOfCell[i - t][j + t]) countOfTheSame++;
                if (countOfTheSame == 5) return true;
            }
        return false;
    }

    void rotation_field(int kordX_ofLeftTop_cell, int kordY_ofLeftTop_cell, boolean clockwise) {
        int small_field[][] = new int[3][3];
        for (int i = kordX_ofLeftTop_cell; i < kordX_ofLeftTop_cell + 3; i++)
            for (int j = kordY_ofLeftTop_cell; j < kordY_ofLeftTop_cell + 3; j++) {
                if (clockwise) small_field[j % 3][2 - i % 3] = colourOfCell[i][j];
                else small_field[2 - j % 3][i % 3] = colourOfCell[i][j];
            }
        for (int i = kordX_ofLeftTop_cell; i < kordX_ofLeftTop_cell + 3; i++)
            for (int j = kordY_ofLeftTop_cell; j < kordY_ofLeftTop_cell + 3; j++)
                colourOfCell[i][j] = small_field[i % 3][j % 3];
    }

    public int getPlayer() {
        return player;
    }

    public void setPlayer() {
        if(player == 1) player =2;
        else player =1;
    }

    public int getCountOfBall() {
        return countOfBall;
    }

    public void incCountOfBall() {
        this.countOfBall++;
    }
}
