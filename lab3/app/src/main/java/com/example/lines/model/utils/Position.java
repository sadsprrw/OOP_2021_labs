package com.example.lines.model.utils;

import java.util.Objects;

public class Position implements Comparable<Position> {
    public int i;
    public int j;

    public Position(int i, int j) {
        this.i = i;
        this.j = j;
    }

    @Override
    public int compareTo(Position o) {
        return (this.i != o.i) ? (this.i - o.i) : (this.j - o.j);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return i == position.i &&
                j == position.j;
    }

    @Override
    public int hashCode() {
        return Objects.hash(i, j);
    }
}