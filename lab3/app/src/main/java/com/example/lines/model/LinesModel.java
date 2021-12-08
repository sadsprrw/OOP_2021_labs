package com.example.lines.model;

import com.example.lines.model.exceptions.InvalidFieldSizeException;
import com.example.lines.model.strategies.ComputerStrategy;
import com.example.lines.model.strategies.RandomComputerStrategy;
import com.example.lines.model.utils.Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LinesModel {
    public static final int MIN_CELLS_IN_CHAIN = 5;
    public static final int NEXT_COLORS_NUM = 3;
    private final Field field;
    private List<Color> nextColors;
    private final ComputerStrategy computerStrategy;
    private long score;

    private enum Chain {
        VERTICAL,
        HORIZONTAL,
        DIAGONAL,
        ANTIDIAGONAL;
    }

    public LinesModel(int fieldSize) throws InvalidFieldSizeException {
        if (fieldSize < 5) {
            throw new InvalidFieldSizeException("Field should be at least of size 5. Provided: " +
                    fieldSize);
        }

        field = new Field(fieldSize);
        nextColors = new ArrayList<>(NEXT_COLORS_NUM);
        this.computerStrategy = new RandomComputerStrategy();
        score = 0;

        nextColors = computerStrategy.generateNextColors();
        putNextColorsAndGenerateNew();
    }

    public LinesModel(int fieldSize, ComputerStrategy computerStrategy)
            throws InvalidFieldSizeException {
        if (fieldSize < 5) {
            throw new InvalidFieldSizeException("Field should be at least of size 5. Provided: " +
                    fieldSize);
        }

        field = new Field(fieldSize);
        nextColors = new ArrayList<>(NEXT_COLORS_NUM);
        this.computerStrategy = computerStrategy;
        score = 0;

        nextColors = computerStrategy.generateNextColors();
        putNextColorsAndGenerateNew();
    }

    public boolean isEmptyAt(Position pos) {
        return field.get(pos).isEmpty();
    }

    public Color getColorAt(Position pos) {
        return field.get(pos).getColor();
    }

    public List<Color> getNextColors() {
        return nextColors;
    }

    public long getScore() {
        return score;
    }

    public int getSize() {
        return field.getSize();
    }

    public List<Position> getFreePositions() {
        return field.getFreePositions();
    }

    public boolean isGameOver() {
        for (int i = 0; i < field.getSize(); i++) {
            for (int j = 0; j < field.getSize(); j++) {
                if (field.get(i, j).isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void restartGame() {
        field.clear();
        putNextColorsAndGenerateNew();
        score = 0;
    }

    private void putNextColorsAndGenerateNew() {
        List<Position> freePositions = computerStrategy.choosePositions(field.getFreePositions());
        for (int i = 0; i < NEXT_COLORS_NUM; i++) {
            field.get(freePositions.get(i)).put(nextColors.get(i));
        }
        nextColors = computerStrategy.generateNextColors();
    }

    public Set<Position> getPossibleMoves(Position current) {
        Set<Position> result = new HashSet<>();
        if (field.get(current).isEmpty()) {
            return result;
        }
        return DFS(current);
    }

    private Set<Position> DFS(Position start) {
        Set<Position> result = new HashSet<>();
        List<List<Boolean>> visited = new ArrayList<>(field.getSize());

        for (int i = 0; i < field.getSize(); i++) {
            visited.add(new ArrayList<Boolean>(field.getSize()) {
                {
                    for (int j = 0; j < field.getSize(); j++) {
                        add(false);
                    }
                }
            });
        }

        DFSProcedure(start.i, start.j, visited);
        for (int i = 0; i < visited.size(); i++) {
            for (int j = 0; j < visited.size(); j++) {
                if (visited.get(i).get(j)) {
                    if (i != start.i || j != start.j) {
                        result.add(new Position(i, j));
                    }
                }
            }
        }

        return result;
    }

    private void DFSProcedure(int i, int j, List<List<Boolean>> visited) {
        visited.get(i).set(j, true);
        //visit bottom cell
        if (i + 1 < field.getSize() && field.get(i + 1, j).isEmpty() &&
                !visited.get(i + 1).get(j)) {
            DFSProcedure(i + 1, j, visited);
        }
        //visit right cell
        if (j + 1 < field.getSize() && field.get(i, j + 1).isEmpty() &&
                !visited.get(i).get(j + 1)) {
            DFSProcedure(i, j + 1, visited);
        }
        //visit top cell
        if (i - 1 >= 0 && field.get(i - 1, j).isEmpty() && !visited.get(i - 1).get(j)) {
            DFSProcedure(i - 1, j, visited);
        }
        //visit left cell
        if (j - 1 >= 0 && field.get(i, j - 1).isEmpty() && !visited.get(i).get(j - 1)) {
            DFSProcedure(i, j - 1, visited);
        }
    }

    public boolean makeMove(Position from, Position to) {
        if (field.get(from).isEmpty() || !field.get(to).isEmpty()) {
            return false;
        }
        if (!getPossibleMoves(from).contains(to)) {
            return false;
        }
        Color toMove = field.get(from).take();
        field.get(to).put(toMove);
        if (destroyChains()) {
            return true;
        }
        putNextColorsAndGenerateNew();
        destroyChains();
        if (field.isEmpty()) {
            putNextColorsAndGenerateNew();
        }
        return true;
    }

    private boolean destroyChains() {
        Set<Cell> cellsToDestroy = new HashSet<>();
        int cellsNum = field.getSize() * field.getSize();
        Map<Cell, Set<Chain>> checked = new HashMap<>(cellsNum);
        List<Integer> chainsLengths = new ArrayList<>();

        for (int i = 0; i < field.getSize(); i++) {
            for (int j = 0; j < field.getSize(); j++) {
                checked.put(field.get(i, j), new HashSet<>());
            }
        }

        for (int i = 0; i < field.getSize(); i++) {
            for (int j = 0; j < field.getSize(); j++) {
                if (!field.get(i, j).isEmpty()) {
                    for (Chain direction : Chain.values()) {
                        if (!checked.get(field.get(i, j)).contains(direction)) {
                            destructionProcedure(cellsToDestroy, checked, chainsLengths, i, j,
                                    direction, 1);
                        }
                    }
                }
            }
        }

        if (cellsToDestroy.isEmpty()) {
            return false;
        }
        for (Cell current : cellsToDestroy) {
            current.take();
        }
        for (Integer length : chainsLengths) {
            int pointsForCell = length - MIN_CELLS_IN_CHAIN + 1;
            score += pointsForCell * length;
        }
        return true;
    }

    private boolean destructionProcedure(Set<Cell> cellsToDestroy, Map<Cell, Set<Chain>> checked,
                                         List<Integer> chainsLengths, int i, int j, Chain direction,
                                         int count) {
        boolean fullChain = false;
        Cell current = field.get(i, j);
        checked.get(current).add(direction);

        switch (direction) {
            case HORIZONTAL:
                if (j + 1 < field.getSize() && current.getColor() ==
                        field.get(i, j + 1).getColor()) {
                    fullChain = destructionProcedure(cellsToDestroy, checked, chainsLengths,
                            i, j + 1, direction, count + 1);
                } else {
                    //the end of chain
                    if (count >= MIN_CELLS_IN_CHAIN) {
                        fullChain = true;
                        chainsLengths.add(count);
                    }
                }
                break;
            case VERTICAL:
                if (i + 1 < field.getSize() && current.getColor() ==
                        field.get(i + 1, j).getColor()) {
                    fullChain = destructionProcedure(cellsToDestroy, checked, chainsLengths,
                            i + 1, j, direction, count + 1);
                } else {
                    //the end of chain
                    if (count >= MIN_CELLS_IN_CHAIN) {
                        fullChain = true;
                        chainsLengths.add(count);
                    }
                }
                break;
            case DIAGONAL:
                if (i + 1 < field.getSize() && j + 1 < field.getSize() &&
                        current.getColor() == field.get(i + 1, j + 1).getColor()) {
                    fullChain = destructionProcedure(cellsToDestroy, checked, chainsLengths,
                            i + 1, j + 1, direction, count + 1);
                } else {
                    //the end of chain
                    if (count >= MIN_CELLS_IN_CHAIN) {
                        fullChain = true;
                        chainsLengths.add(count);
                    }
                }
                break;
            case ANTIDIAGONAL:
                if (i + 1 < field.getSize() && j - 1 >= 0 && current.getColor() ==
                        field.get(i + 1, j - 1).getColor()) {
                    fullChain = destructionProcedure(cellsToDestroy, checked, chainsLengths,
                            i + 1, j - 1, direction, count + 1);
                } else {
                    //the end of chain
                    if (count >= MIN_CELLS_IN_CHAIN) {
                        fullChain = true;
                        chainsLengths.add(count);
                    }
                }
                break;
            default:
                break;
        }

        if (fullChain) {
            cellsToDestroy.add(current);
        }

        return fullChain;
    }
}
