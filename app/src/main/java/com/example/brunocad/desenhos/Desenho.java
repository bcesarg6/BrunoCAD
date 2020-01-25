package com.example.brunocad.desenhos;

import android.graphics.Paint;

import java.util.List;

public class Desenho {

    private long id;
    private int tipo;
    private int cor;

    private List<Float> pontos = null;
    private Paint paint = null;

    public Desenho(long id, int tipo, int cor) {
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

    public List<Float> getPontos() {
        return pontos;
    }

    public void setPontos(List<Float> pontos) {
        this.pontos = pontos;
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
