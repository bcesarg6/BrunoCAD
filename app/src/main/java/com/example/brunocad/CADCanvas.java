package com.example.brunocad;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.brunocad.desenhos.Desenho;
import com.example.brunocad.utils.CADConstants;

import java.util.ArrayList;
import java.util.List;

import static com.example.brunocad.utils.CADConstants.tiposDesenhos;

public class CADCanvas extends View {

    List<Desenho> desenhos = new ArrayList<>();

    public CADCanvas(Context context) {
        super(context);
    }

    public CADCanvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CADCanvas(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void addDesenho(Desenho desenho) {
        desenhos.add(desenho);
        postInvalidate();
    }

    public void clearCanvas() {
        desenhos.clear();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        for (Desenho d: desenhos) {
            switch (d.getTipo()) {
                case tiposDesenhos.LINHA:

                    float startX = d.getPontos().get(0);
                    float startY = d.getPontos().get(1);
                    float stopX = d.getPontos().get(2);
                    float stopY = d.getPontos().get(3);

                    canvas.drawLine(startX, startY, stopX, stopY, d.getPaint());
                    break;

                default:
                    break;
            }
        }

    }
}
