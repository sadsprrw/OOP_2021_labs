package com.example.lines.model.strategies;

import com.example.lines.model.Color;
import com.example.lines.model.utils.Position;

import java.util.List;

public interface ComputerStrategy {
    List<Color> generateNextColors();
    List<Position> choosePositions(List<Position> freePositions);
}