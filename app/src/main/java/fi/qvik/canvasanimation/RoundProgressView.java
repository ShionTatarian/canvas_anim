package fi.qvik.canvasanimation;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Build.VERSION_CODES;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by Tommy on 28/09/16.
 */
public class RoundProgressView extends View {

    private RectF box, cutBox;
    private Paint paint;
    private Paint transparentPaint;
    private int lineWidth = 0;

    private int progress = 360;

    public RoundProgressView(Context context) {
        super(context);
        init();
    }

    public RoundProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        readAttributes(context, attrs);
    }

    public RoundProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        readAttributes(context, attrs);
    }

    @RequiresApi(api = VERSION_CODES.LOLLIPOP)
    public RoundProgressView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
        readAttributes(context, attrs);
    }

    private void readAttributes(Context ctx, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray a = null;
        try {
            a = ctx.obtainStyledAttributes(attrs, R.styleable.RoundProgressView, 0, 0);
            if (a == null) {
                return;
            }

            for (int i = 0; i < a.getIndexCount(); i++) {
                final int attr = a.getIndex(i);
                switch (attr) {
                    case R.styleable.RoundProgressView_progressColor:
                        setColor(a.getColor(attr, ctx.getResources().getColor(R.color.primary)));
                        break;
                    case R.styleable.RoundProgressView_progress:
                        setProgress(a.getFloat(attr, 1f));
                        break;
                    case R.styleable.RoundProgressView_progressWidth:
                        setProgressWidth(a.getDimensionPixelSize(attr, ctx.getResources().getDimensionPixelSize(R.dimen.margin_normal)));
                        break;
                }
            }
        } finally {
            if (a != null) {
                a.recycle();
            }
        }
    }

    private void init() {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        lineWidth = getResources().getDimensionPixelSize(R.dimen.margin_normal);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        paint.setStyle(Style.STROKE);
        paint.setColor(Color.GREEN);
        paint.setStyle(Style.FILL_AND_STROKE);

        transparentPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        transparentPaint.setStyle(Style.FILL_AND_STROKE);
        transparentPaint.setColor(Color.TRANSPARENT);
        transparentPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int p = getResources().getDimensionPixelSize(R.dimen.margin_one);
        box = new RectF(p, p, w - p, h - p);
        cutBox = new RectF(0, 0, w, h);
    }

    public void setProgress(@FloatRange(from = 0f, to = 1f) float progress) {
        this.progress = (int) (360 * progress);
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawArc(box, 0, 360, true, paint);

        // cut center
        int x = canvas.getWidth() / 2;
        int y = canvas.getHeight() / 2;
        float radius = (box.width() / 2) - lineWidth;
        canvas.drawCircle(x, y, radius, transparentPaint);

        // animation cut
        canvas.drawArc(cutBox, -90 + progress, 360 - progress, true, transparentPaint);
    }

    public void animateProgress() {
        animateProgress((float) progress / 360f);
    }

    public void animateProgress(@FloatRange(from = 0f, to = 1f) float progress) {
        if (progress <= 0) {
            return;
        }
        ObjectAnimator anim = ObjectAnimator.ofPropertyValuesHolder(this,
                PropertyValuesHolder.ofFloat("progress", 0, progress)
        );
        anim.setDuration((long) (800 * progress));
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.start();
    }

    public void setColor(@ColorInt int color) {
        paint.setColor(color);
        postInvalidate();
    }

    public void setProgressWidth(int progressWidth) {
        lineWidth = progressWidth;
    }
}
