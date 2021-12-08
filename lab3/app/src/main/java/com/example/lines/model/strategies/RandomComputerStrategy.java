package com.example.lines.model.strategies;

import com.example.lines.model.Color;
import com.example.lines.model.LinesModel;
import com.example.lines.model.utils.Position;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomComputerStrategy implements ComputerStrategy {
    private final Random generator;

    public RandomComputerStrategy() {
        generator = new Random();
    }

    @Override
    public List<Color> generateNextColors() {
        List<Color> result = new ArrayList<>(LinesModel.NEXT_COLORS_NUM);
        Color[] colors = Color.values();
        for (int i = 0; i < LinesModel.NEXT_COLORS_NUM; i++) {
            result.add(colors[generator.nextInt(colors.length)]);
        }
        return result;
    }

    @Override
    public List<Position> choosePositions(List<Position> freePositions) {
        List<Position> copy = new ArrayList<Position>() {
            {
                addAll(freePositions);
            }
        };
        Collections.shuffle(copy);
        int posNum = Math.min(freePositions.size(), LinesModel.NEXT_COLORS_NUM);
        List<Position> result = new ArrayList<>(posNum);
        for (int i = 0; i < posNum; i++) {
            result.add(copy.get(i));
        }
        return result;
    }
}