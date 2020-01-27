package com.example.brunocad.drawings;

import java.util.ArrayList;
import java.util.List;

import static com.example.brunocad.utils.CADConstants.drawingTypes.CIRCLE;
import static com.example.brunocad.utils.CADConstants.drawingTypes.CIRCLE_STROKE;

public class Circle extends Drawing {

    private float radius;

    public Circle(long id, float centerX, float centerY, float radius, int cor, boolean isFill) {
        super(id, isFill ? CIRCLE : CIRCLE_STROKE, cor);

        List<Float> values = new ArrayList<>();

        values.add(centerX);
        values.add(centerY);
        values.add(radius);

        setValues(values);
        configPaint(true, isFill, true);

        this.radius = radius;
    }

    public float getRadius() {
        return radius;
    }
}
