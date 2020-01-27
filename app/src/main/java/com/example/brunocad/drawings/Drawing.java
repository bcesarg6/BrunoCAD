package com.example.brunocad.drawings;

import android.graphics.Paint;
import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

public class Drawing {

    private long id;
    private int tipo;
    private int cor;

    private List<Point> points = new ArrayList<>();
    private List<Float> values = null;
    private Paint paint = null;

    public Drawing(long id, int tipo, int cor) {
        this.id = id;
        this.tipo = tipo;
        this.cor = cor;
    }

    public void configPaint(boolean isAntiAlias, boolean isFill, boolean isStroke) {
        paint = new Paint();
        paint.setColor(cor);
        paint.setStrokeWidth(9f);

        if (isAntiAlias) paint.setFlags(Paint.ANTI_ALIAS_FLAG);

        if (isFill && isStroke) paint.setStyle(Paint.Style.FILL_AND_STROKE);
        else if (isFill) paint.setStyle(Paint.Style.FILL);
        else  paint.setStyle(Paint.Style.STROKE);
    }

    public void setStrokeWidth(float width) {
        if (paint != null) {
            paint.setStrokeWidth(width);
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Float> getValues() {
        return values;
    }

    public void setValues(List<Float> values) {
        this.values = values;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setCor(int cor) {
        this.paint.setColor(cor);
    }

    public int getTipo() {
        return tipo;
    }

    public void addPoint(Point point) {
        points.add(point);
    }

    public List<Point> getPoints() {
        return points;
    }

    public void clearPoints() {
        points.clear();
    }
}
