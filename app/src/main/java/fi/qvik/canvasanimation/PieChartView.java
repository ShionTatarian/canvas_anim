package fi.qvik.canvasanimation;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Build.VERSION_CODES;
import android.support.annotation.ColorInt;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tommy on 28/09/16.
 */
public class PieChartView extends View {

    private final List<Integer> colors = new ArrayList<>();
    private final List<Integer> slices = new ArrayList<>();
    private RectF box, cutBox;
    private Paint paint;
    private Paint transparentPaint;
    private int lineWidth = 0;
    private int sum = 0;

    private int maxAngle = 360;

    public PieChartView(Context context) {
        super(context);
        init();
    }

    public PieChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PieChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = VERSION_CODES.LOLLIPOP)
    public PieChartView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        lineWidth = getResources().getDimensionPixelSize(R.dimen.margin_normal);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        paint.setStyle(Style.FILL_AND_STROKE);

        transparentPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        transparentPaint.setStyle(Style.FILL_AND_STROKE);
        transparentPaint.setColor(Color.TRANSPARENT);
        transparentPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int p = getResources().getDimensionPixelSize(R.dimen.margin_normal) / 8;
        box = new RectF(p, p, w - p, h - p);
        cutBox = new RectF(0, 0, w, h);
    }

    public void setMaxAngle(int angle) {
        this.maxAngle = angle;
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (sum <= 0) {
            return;
        }

        float start = -90;
        //draw slices
        for (int i = 0; i < slices.size(); i++) {
            paint.setColor(colors.get(i));
            float angle;
            angle = (360.0f / sum) * slices.get(i);
            canvas.drawArc(box, start, angle, true, paint);
            start += angle;
        }

        // cut center
        canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2, (canvas.getWidth() / 2) - lineWidth, transparentPaint);
        // animation cut
        canvas.drawArc(cutBox, -90 + maxAngle, 360 - maxAngle, true, transparentPaint);
    }

    public void animatePieChart() {
        animateToMaxAngle(360);
    }

    public void animateToMaxAngle(int angle) {
        if (angle <= 0) {
            return;
        }
        ObjectAnimator anim = ObjectAnimator.ofPropertyValuesHolder(this,
                PropertyValuesHolder.ofInt("maxAngle", 0, angle)
        );
        anim.setDuration(800 * angle / 360);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.start();
    }

    public void clear() {
        slices.clear();
        colors.clear();

        postInvalidate();
    }

    public void addAngle(int angle, @ColorInt int angleColor) {
        slices.add(angle);
        colors.add(angleColor);


        postInvalidate();
    }

    @Override
    public void postInvalidate() {
        int s = 0;
        for (int slice : slices) {
            s += slice;
        }
        sum = s;
        super.postInvalidate();
    }
}
