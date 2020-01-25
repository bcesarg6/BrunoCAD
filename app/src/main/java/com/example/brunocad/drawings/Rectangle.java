package com.example.brunocad.drawings;

import com.example.brunocad.utils.CADConstants;

import java.util.ArrayList;
import java.util.List;

public class Rectangle extends Drawing {

    public Rectangle(long id, float left, float top, float right, float bottom, int cor) {
        super(id, CADConstants.tiposDesenhos.RETANGULO, cor);

        List<Float> points = new ArrayList<>();

        points.add(left);
        points.add(top);
        points.add(right);
        points.add(bottom);

        setPoints(points);
        configPaint(true, true, true);
    }
}
