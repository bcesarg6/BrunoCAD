package com.example.brunocad.drawings;

import com.example.brunocad.utils.CADConstants;

import java.util.ArrayList;
import java.util.List;

public class Triangle extends Drawing {

    public Triangle(long id,
                    float p1x, float p1y,
                    float p2x, float p2y,
                    float p3x, float p3y,
                    int cor, boolean isFill) {

        super(id, CADConstants.drawingTypes.TRIANGLE, cor);

        List<Float> points = new ArrayList<>();

        points.add(p1x);
        points.add(p1y);
        points.add(p2x);
        points.add(p2y);
        points.add(p3x);
        points.add(p3y);

        setPoints(points);
        configPaint(true, isFill, true);
    }
}
