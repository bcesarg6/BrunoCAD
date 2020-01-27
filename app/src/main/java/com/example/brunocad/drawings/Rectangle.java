package com.example.brunocad.drawings;

import java.util.ArrayList;
import java.util.List;

import static com.example.brunocad.utils.CADConstants.drawingTypes.RECTANGLE;
import static com.example.brunocad.utils.CADConstants.drawingTypes.RECTANGLE_STROKE;

public class Rectangle extends Drawing {

    public Rectangle(long id, float left, float top, float right, float bottom, int cor, boolean isFill) {
        super(id, isFill ? RECTANGLE : RECTANGLE_STROKE, cor);

        List<Float> values = new ArrayList<>();

        values.add(left);
        values.add(top);
        values.add(right);
        values.add(bottom);

        setValues(values);
        configPaint(true, isFill, true);
    }

    public void UpdateRectangle(float left, float top, float right, float bottom) {
        List<Float> values = new ArrayList<>();

        values.add(left);
        values.add(top);
        values.add(right);
        values.add(bottom);

        setValues(values);
    }
}
