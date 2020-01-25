package com.example.brunocad.drawings;

import com.example.brunocad.utils.CADConstants;

import java.util.ArrayList;
import java.util.List;

public class Circle extends Drawing {

    public Circle(long id, float centerX, float centerY, float radius, int cor) {
        super(id, CADConstants.tiposDesenhos.CIRCULO, cor);

        List<Float> points = new ArrayList<>();

        points.add(centerX);
        points.add(centerY);
        points.add(radius);

        setPoints(points);
        configPaint(true, true, true);
    }
}
