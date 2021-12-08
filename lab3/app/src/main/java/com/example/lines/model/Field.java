package com.example.lines.model;

import com.example.lines.model.utils.Position;

import java.util.ArrayList;
import java.util.List;

public class Field {
    private final List<List<Cell>> cells;

    public Field(int size) {
        cells = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            cells.add(new ArrayList<Cell>(size) {
                {
                    for (int j = 0; j < size; j++) {
                        add(new Cell());
                    }
                }
            });
        }
    }

    public int getSize() {
        return cells.size();
    }

    public Cell get(int i, int j) {
        return cells.get(i).get(j);
    }

    public Cell get(Position pos) {
        return cells.get(pos.i).get(pos.j);
    }

    public void clear() {
        for (int i = 0; i < cells.size(); i++) {
            for (int j = 0; j < cells.size(); j++) {
                cells.get(i).get(j).take();
            }
        }
    }

    public boolean isEmpty() {
        for (int i = 0; i < cells.size(); i++) {
            for (int j = 0; j < cells.size(); j++) {
                if (!cells.get(i).get(j).isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    public List<Position> getFreePositions() {
        List<Position> result = new ArrayList<>();
        for (int i = 0; i < cells.size(); i++) {
            for (int j = 0; j < cells.size(); j++) {
                if (cells.get(i).get(j).isEmpty()) {
                    result.add(new Position(i, j));
                }
            }
        }
        return result;
    }
}