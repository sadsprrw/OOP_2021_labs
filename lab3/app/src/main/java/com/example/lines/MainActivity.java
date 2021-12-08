package com.example.lines;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.lines.model.Color;
import com.example.lines.model.LinesModel;
import com.example.lines.model.exceptions.InvalidFieldSizeException;
import com.example.lines.model.utils.Position;

import java.util.List;
import java.util.Set;

public class MainActivity extends Activity {
    private LinesModel model;
    private Integer selectedCell;
    private CellAdapter cellAdapter;
    private FutureColorsAdapter futureColorsAdapter;
    private TextView score;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final int size = 9;
        try {
            model = new LinesModel(size);
        } catch (InvalidFieldSizeException e) {
            e.printStackTrace();
        }
        selectedCell = null;

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Button restart = (Button) findViewById(R.id.restart);
        View.OnClickListener restartClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.restartGame();
                updateCells(null);
                updateFutureColors();
                score.setText(getString(R.string.score));
            }
        };
        restart.setOnClickListener(restartClickListener);

        GridView futureColorsGrid = (GridView) findViewById(R.id.future);
        futureColorsAdapter = new FutureColorsAdapter(this, model);
        futureColorsGrid.setNumColumns(LinesModel.NEXT_COLORS_NUM);
        futureColorsGrid.setAdapter(futureColorsAdapter);

        GridView cellsGrid = (GridView) findViewById(R.id.cells);
        cellAdapter = new CellAdapter(this, model);
        cellsGrid.setNumColumns(size);
        cellsGrid.setAdapter(cellAdapter);

        GridView.OnItemClickListener cellsOnItemClickListener = new GridView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Position fieldPos = new Position(position / model.getSize(),
                        position % model.getSize());
                if (selectedCell == null || !model.isEmptyAt(fieldPos)) {
                    if (!model.isEmptyAt(fieldPos)) {
                        selectedCell = position;
                        Set<Position> available = model.getPossibleMoves(fieldPos);
                        updateCells(available);
                    }
                } else {
                    Position selectedPos = new Position(selectedCell / model.getSize(),
                            selectedCell % model.getSize());
                    Set<Position> available = model.getPossibleMoves(selectedPos);
                    if (available.contains(fieldPos)) {
                        model.makeMove(selectedPos, fieldPos);
                        if (model.isGameOver()) {
                            model.restartGame();
                        }
                        updateFutureColors();
                    }
                    selectedCell = null;
                    updateCells(null);
                    String score_str = "Score: " + model.getScore();
                    score.setText(score_str);
                }
            }
        };

        cellsGrid.setOnItemClickListener(cellsOnItemClickListener);

        score = (TextView) findViewById(R.id.score);
    }

    private void updateCells(Set<Position> available) {
        for (int i = 0; i < model.getSize(); i++) {
            for (int j = 0; j < model.getSize(); j++) {
                int index = i * model.getSize() + j;
                Position pos = new Position(i, j);
                cellAdapter.imageIds[index] = Utils.getImageByPosition(model, pos,
                        selectedCell != null && index == selectedCell,
                        available != null && available.contains(pos));
            }
        }
        cellAdapter.notifyDataSetChanged();
    }

    private void updateFutureColors() {
        List<Color> futureColors = model.getNextColors();
        for (int i = 0; i < LinesModel.NEXT_COLORS_NUM; i++) {
            futureColorsAdapter.imageIds[i] = Utils.getFutureColorImage(futureColors.get(i));
        }
        futureColorsAdapter.notifyDataSetChanged();
    }
}