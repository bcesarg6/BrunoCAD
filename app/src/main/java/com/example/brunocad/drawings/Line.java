package com.example.brunocad.drawings;

import com.example.brunocad.utils.CADConstants;

import java.util.ArrayList;
import java.util.List;

public class Line extends Drawing {

    public Line(long id, float startX, float startY, float stopX, float stopY, int cor) {
        super(id, CADConstants.drawingTypes.LINE, cor);

        List<Float> values = new ArrayList<>();

        values.add(startX);
        values.add(startY);
        values.add(stopX);
        values.add(stopY);

        setValues(values);
        configPaint(true, true, true);
    }
}
