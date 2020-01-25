package com.example.brunocad.drawings;

import com.example.brunocad.utils.CADConstants;

import java.util.ArrayList;
import java.util.List;

public class Line extends Drawing {

    public Line(long id, float startX, float startY, float stopX, float stopY, int cor) {
        super(id, CADConstants.drawingTypes.LINE, cor);

        List<Float> points = new ArrayList<>();

        points.add(startX);
        points.add(startY);
        points.add(stopX);
        points.add(stopY);

        setPoints(points);
        configPaint(true, true, true);
    }
}
