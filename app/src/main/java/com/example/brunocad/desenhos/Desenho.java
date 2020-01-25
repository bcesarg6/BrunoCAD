package com.example.brunocad.desenhos;

import android.graphics.Paint;

import java.util.List;

public class Desenho {

    private long id;
    private int tipo;
    private List<Float> pontos = null;
    private Paint paint;

    public Desenho(long id, int tipo, int cor) {
        this.id = id;
        this.tipo = tipo;
        this.paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.paint.setColor(cor);
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
