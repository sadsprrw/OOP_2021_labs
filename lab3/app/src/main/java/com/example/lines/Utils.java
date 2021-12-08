package com.example.lines;

import com.example.lines.model.Color;
import com.example.lines.model.LinesModel;
import com.example.lines.model.utils.Position;

public abstract class Utils {

    public static Integer getImageByPosition(LinesModel model, Position pos, boolean selected,
                                             boolean available) {
        if (model.isEmptyAt(pos)) {
            return available ? R.drawable.empty_cell_available : R.drawable.empty_cell;
        }
        Color color = model.getColorAt(pos);
        switch (color) {
            case RED:
                return selected ? R.drawable.red_selected : R.drawable.red_unselected;
            case ORANGE:
                return selected ? R.drawable.orange_selected : R.drawable.orange_unselected;
            case YELLOW:
                return selected ? R.drawable.yellow_selected : R.drawable.yellow_unselected;
            case GREEN:
                return selected ? R.drawable.green_selected : R.drawable.green_unselected;
            case BLUE:
                return selected ? R.drawable.blue_selected : R.drawable.blue_unselected;
            case INDIGO:
                return selected ? R.drawable.indigo_selected : R.drawable.indigo_unselected;
            case VIOLET:
                return selected ? R.drawable.violet_selected : R.drawable.violet_unselected;
            default:
                return null;
        }
    }

    public static Integer getFutureColorImage(Color color) {
        switch (color) {
            case RED:
                return R.drawable.red;
            case ORANGE:
                return R.drawable.orange;
            case YELLOW:
                return R.drawable.yellow;
            case GREEN:
                return R.drawable.green;
            case BLUE:
                return R.drawable.blue;
            case INDIGO:
                return R.drawable.indigo;
            case VIOLET:
                return R.drawable.violet;
            default:
                return null;
        }
    }
}