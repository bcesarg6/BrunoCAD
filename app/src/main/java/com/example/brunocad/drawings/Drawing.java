package com.example.brunocad.drawings;

import android.graphics.Paint;

import java.util.List;

public class Drawing {

    private long id;
    private int tipo;
    private int cor;

    private List<Float> points = null;
    private Paint paint = null;

    public Drawing(long id, int tipo, int cor) {
        this.id = id;
        this.tipo = tipo;
        this.cor = cor;
    }

    public void configPaint(boolean isAntiAlias, boolean isFill, boolean isStroke) {
        paint = new Paint();
        paint.setColor(cor);

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

    public List<Float> getPoints() {
        return points;
    }

    public void setPoints(List<Float> points) {
        this.points = points;
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
}
