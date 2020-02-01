package com.example.brunocad.drawings;

import java.util.ArrayList;
import java.util.List;

import static com.example.brunocad.utils.CADConstants.drawingTypes.TRIANGLE;
import static com.example.brunocad.utils.CADConstants.drawingTypes.TRIANGLE_STROKE;

public class Triangle extends Drawing {

    public Triangle(long id,
                    float p1x, float p1y,
                    float p2x, float p2y,
                    float p3x, float p3y,
                    int cor, boolean isFill) {

        super(id, isFill ? TRIANGLE : TRIANGLE_STROKE, cor);

        List<Float> values = new ArrayList<>();

        values.add(p1x);
        values.add(p1y);
        values.add(p2x);
        values.add(p2y);
        values.add(p3x);
        values.add(p3y);

        setValues(values);
        configPaint(true, isFill, true);
    }

    public void updateTriangle(float p1x, float p1y,
                               float p2x, float p2y,
                               float p3x, float p3y) {

        List<Float> values = new ArrayList<>();

        values.add(p1x);
        values.add(p1y);
        values.add(p2x);
        values.add(p2y);
        values.add(p3x);
        values.add(p3y);

        setValues(values);
    }
}
