package com.example.lines.model;

public class Cell {
    private Color color;
    private boolean isEmpty;

    public Cell() {
        color = null;
        isEmpty = true;
    }

    public Color getColor() {
        return isEmpty ? null : color;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void put(Color color) {
        this.color = color;
        isEmpty = false;
    }

    public Color take() {
        isEmpty = true;
        return color;
    }
}
