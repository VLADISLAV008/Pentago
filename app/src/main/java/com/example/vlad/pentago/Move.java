package com.example.vlad.pentago;

public class Move {
    private int kordX, kordY;
    private boolean rotation;
    private boolean clockwise, left, top;

    Move(int kordX, int kordY, boolean rotation, boolean clockwise, boolean left, boolean top) {
        this.kordX = kordX;
        this.kordY = kordY;
        this.rotation = rotation;
        this.clockwise = clockwise;
        this.left = left;
        this.top = top;
    }

    public int get_KordY() {
        return kordY;
    }

    public boolean get_Rotation() {
        return rotation;
    }

    public boolean get_Clockwise() {
        return clockwise;
    }

    public boolean get_Top() {
        return top;
    }

    public int get_KordX() {
        return kordX;
    }

    public boolean get_Left() {
        return left;
    }
}
