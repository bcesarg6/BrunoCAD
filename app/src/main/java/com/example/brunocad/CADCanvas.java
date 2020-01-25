package com.example.brunocad;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.brunocad.drawings.Drawing;

import java.util.ArrayList;
import java.util.List;

import static com.example.brunocad.utils.CADConstants.tiposDesenhos;

public class CADCanvas extends View {

    List<Drawing> drawings = new ArrayList<>();

    public CADCanvas(Context context) {
        super(context);
    }

    public CADCanvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CADCanvas(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void draw(List<Drawing> drawings) {
        this.drawings.clear();
        this.drawings.addAll(drawings);
        postInvalidate();
    }

    public void clearCanvas() {
        drawings.clear();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        for (Drawing d: drawings) {
            switch (d.getTipo()) {
                case tiposDesenhos.LINHA:

                    float startX = d.getPoints().get(0);
                    float startY = d.getPoints().get(1);
                    float stopX = d.getPoints().get(2);
                    float stopY = d.getPoints().get(3);

                    canvas.drawLine(startX, startY, stopX, stopY, d.getPaint());
                    break;

                case tiposDesenhos.TRIANGULO:

                    float p1x = d.getPoints().get(0);
                    float p1y = d.getPoints().get(1);

                    float p2x = d.getPoints().get(2);
                    float p2y = d.getPoints().get(3);

                    float p3x = d.getPoints().get(4);
                    float p3y = d.getPoints().get(5);

                    Path path = new Path();
                    path.setFillType(Path.FillType.EVEN_ODD);
                    path.moveTo(p1x, p1y);
                    path.lineTo(p2x, p2y);
                    path.lineTo(p3x, p3y);
                    path.close();

                    canvas.drawPath(path, d.getPaint());
                    break;

                case tiposDesenhos.RETANGULO:

                    float left = d.getPoints().get(0);
                    float top = d.getPoints().get(1);
                    float right = d.getPoints().get(2);
                    float bottom = d.getPoints().get(3);

                    canvas.drawRect(new RectF(left,top,right,bottom),d.getPaint());
                    break;

                case tiposDesenhos.CIRCULO:

                    float cx = d.getPoints().get(0);
                    float cy = d.getPoints().get(1);
                    float radius = d.getPoints().get(2);

                    canvas.drawCircle(cx, cy, radius, d.getPaint());
                    break;

                default:
                    break;
            }
        }

    }
}
