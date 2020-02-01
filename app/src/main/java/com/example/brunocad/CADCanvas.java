package com.example.brunocad;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.brunocad.drawings.Drawing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.brunocad.utils.CADConstants.drawingTypes;

public class CADCanvas extends View {

    private tapListener listener = null;
    List<Drawing> drawings = new ArrayList<>();
    Map<Long, Region> regionMap = new HashMap<>();

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
        regionMap.clear();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (Drawing d: drawings) {

            switch (d.getType()) {

                case drawingTypes.LINE:
                    drawLine(canvas, d);
                    break;

                case drawingTypes.TRIANGLE_STROKE:
                case drawingTypes.TRIANGLE:
                    drawTriangle(canvas, d);
                    break;

                case drawingTypes.RECTANGLE_STROKE:
                case drawingTypes.RECTANGLE:
                    drawRectangle(canvas, d);
                    break;

                case drawingTypes.CIRCLE_STROKE:
                case drawingTypes.CIRCLE:
                    drawCircle(canvas, d);
                    break;

                default:
                    break;
            }
        }
    }

    private void drawCircle(Canvas canvas, Drawing d) {
        float cx = d.getValues().get(0);
        float cy = d.getValues().get(1);
        float radius = d.getValues().get(2);

        canvas.drawCircle(cx, cy, radius, d.getPaint());
    }

    private void drawRectangle(Canvas canvas, Drawing d) {
        float left = d.getValues().get(0);
        float top = d.getValues().get(1);
        float right = d.getValues().get(2);
        float bottom = d.getValues().get(3);

        RectF rect = new RectF(left,top,right,bottom);

        if (d.getAngle() != 0f) {

            canvas.save();
            canvas.rotate(d.getAngle(), rect.centerX(), rect.centerY());

            canvas.drawRect(rect, d.getPaint());

            canvas.restore();

        } else canvas.drawRect(rect, d.getPaint());
    }

    private void drawTriangle(Canvas canvas, Drawing d) {
        float p1x = d.getValues().get(0);
        float p1y = d.getValues().get(1);

        float p2x = d.getValues().get(2);
        float p2y = d.getValues().get(3);

        float p3x = d.getValues().get(4);
        float p3y = d.getValues().get(5);

        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(p1x, p1y);
        path.lineTo(p2x, p2y);
        path.lineTo(p3x, p3y);
        path.close();

        Matrix scaleMatrix = new Matrix();

        RectF bounds = new RectF();
        path.computeBounds(bounds, true);

        scaleMatrix.setScale(d.getScale(), d.getScale(), bounds.centerX(), bounds.centerY());
        path.transform(scaleMatrix);

        if (d.getAngle() != 0f) {

            canvas.save();
            canvas.rotate(d.getAngle(), bounds.centerX(), bounds.centerY());

            Region clip = new Region(0, 0, canvas.getWidth(), canvas.getHeight());

            Region r = new Region();
            r.setPath(path, clip);

            regionMap.put(d.getId(), r);

            canvas.drawPath(path, d.getPaint());

            canvas.restore();

        } else {

            Region clip = new Region(0, 0, canvas.getWidth(), canvas.getHeight());

            Region r = new Region();
            r.setPath(path, clip);

            regionMap.put(d.getId(), r);

            canvas.drawPath(path, d.getPaint());
        }
    }

    private void drawLine(Canvas canvas, Drawing d) {
        float startX = d.getValues().get(0);
        float startY = d.getValues().get(1);
        float stopX = d.getValues().get(2);
        float stopY = d.getValues().get(3);

        canvas.drawLine(startX, startY, stopX, stopY, d.getPaint());
    }

    public Region getDrawingRegion(Long id) {
        return regionMap.containsKey(id) ? regionMap.get(id) : null;
    }

    public void setListener(tapListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getActionMasked() == MotionEvent.ACTION_DOWN ||
            event.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN) return true;

        if (listener != null) {
            int pointerIndex = event.getActionIndex();

            if (event.getActionMasked() == MotionEvent.ACTION_UP ||
                    event.getActionMasked() == MotionEvent.ACTION_POINTER_UP) {

                int x = (int) event.getX(pointerIndex);
                int y = (int) event.getY(pointerIndex);

                listener.onTapCanvas(x,y);
            }
        }

        return super.onTouchEvent(event);
    }

    public interface tapListener {
        void onTapCanvas(int x, int y);
    }
}
