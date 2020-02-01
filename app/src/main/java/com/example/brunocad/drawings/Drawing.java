package com.example.brunocad.drawings;

import android.graphics.Paint;
import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

public class Drawing {

    private long id;
    private int type;
    private int color;
    private float angle;
    private float scale;

    private List<Point> points = new ArrayList<>();
    private List<Float> values = null;
    private Paint paint = null;

    Drawing(long id, int type, int color) {
        this.id = id;
        this.type = type;
        this.color = color;
        this.angle = 0f;
        this.scale = 1f;
    }

    void configPaint(boolean isAntiAlias, boolean isFill, boolean isStroke) {
        paint = new Paint();
        paint.setColor(color);
        paint.setStrokeWidth(6f);

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

    public void setColor(int color) {
        this.paint.setColor(color);
    }

    public int getType() {
        return type;
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

    public float getAngle() {
        angle = angle%360f;
        return angle;
    }

    public void addAngle(float angle) {
        this.angle += angle;
    }

    public float getScale() {
        return scale;
    }

    public void applyScale(float scale) {
        this.scale *= scale;
    }
}
