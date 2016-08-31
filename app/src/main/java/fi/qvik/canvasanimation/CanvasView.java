package fi.qvik.canvasanimation;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.os.Build.VERSION_CODES;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

public class CanvasView extends View {

    private static final String TAG = "CanvasView";

    private Paint primaryPaint = new Paint();
    private Paint backgroundPaint = new Paint();

    private float innerCircleSize = 0;
    private float circleSmall = 0;
    private float circleLarge = 0;
    private float circleSize = 0;

    private Rect rect = new Rect();
    private Paint imagePaint = new Paint();
    private Bitmap image;
    private Paint shadowPaint = new Paint(0);

    public CanvasView(Context context) {
        super(context);
        init();
    }

    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CanvasView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(VERSION_CODES.LOLLIPOP)
    public CanvasView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        Resources r = getResources();
        this.image = BitmapFactory.decodeResource(r, R.drawable.ic_brightness_auto_black_48dp);

        backgroundPaint.setColor(Color.WHITE);
        backgroundPaint.setStyle(Style.FILL);
        backgroundPaint.setColorFilter(new PorterDuffColorFilter(Color.WHITE, Mode.SRC_IN));
        backgroundPaint.setAntiAlias(true);

        primaryPaint.setColor(Color.BLUE);
        primaryPaint.setStyle(Style.FILL);
        primaryPaint.setAntiAlias(true);

        imagePaint.setColorFilter(new PorterDuffColorFilter(primaryPaint.getColor(), Mode.SRC_IN));
        imagePaint.setAntiAlias(true);

        shadowPaint.setColor(0xff101010);
        shadowPaint.setMaskFilter(new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL));
    }

    public void setImage(@DrawableRes int imageRes) {
        this.image = BitmapFactory.decodeResource(getResources(), imageRes);
        postInvalidate();
    }

    public void setImage(Bitmap bitmap) {
        this.image = bitmap;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int size = Math.min(w, h);
        Log.d(TAG, "size: " + size);

        circleSmall = size * 0.75f;
        circleLarge = size * 0.90f;
        Log.d(TAG, "circleSmall: " + circleSmall);
        Log.d(TAG, "circleLarge: " + circleLarge);

        setCircleSize(isSelected() ? circleLarge : circleSmall);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int halfWidth = getWidth() / 2;
        float halfCircle = circleSize / 2;

        canvas.drawCircle(halfWidth, halfWidth, halfCircle, primaryPaint);

        float whiteCircle = circleSize * 0.47f;
        canvas.drawCircle(halfWidth, halfWidth, whiteCircle, backgroundPaint);

        float smallCircle = innerCircleSize * 0.46f;
        canvas.drawCircle(halfWidth, halfWidth, smallCircle, primaryPaint);

        if (image != null) {
            canvas.drawBitmap(image, null, rect, imagePaint);
        }
    }

    public void setCircleSize(float circle) {
        this.circleSize = circle;
        int halfWidth = getWidth() / 2;
        int s = (int) (circleSize * 0.75f);
        int x1 = halfWidth - s / 2;
        int y1 = halfWidth - s / 2;
        int x2 = x1 + s;
        int y2 = y1 + s;
        rect.set(x1, y1, x2, y2);

        postInvalidate();
    }

    public void setInnerCircleSize(float innerCircle) {
        this.innerCircleSize = innerCircle;

        postInvalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        setSelected(!isSelected());

        return super.onTouchEvent(event);
    }

    @Override
    public void setSelected(boolean selected) {
        boolean wasSelected = isSelected();

        int colorFrom = wasSelected ? backgroundPaint.getColor() : primaryPaint.getColor();
        int colorTo = wasSelected ? primaryPaint.getColor() : backgroundPaint.getColor();

        float circleFrom = wasSelected ? circleLarge : circleSmall;
        float circleTo = wasSelected ? circleSmall : circleLarge;
        ObjectAnimator pulseAnim = ObjectAnimator.ofPropertyValuesHolder(this,
                PropertyValuesHolder.ofFloat("circleSize", circleFrom, circleTo),
                PropertyValuesHolder.ofFloat("innerCircleSize", wasSelected ? circleLarge : 0, wasSelected ? 0 : circleLarge),
                PropertyValuesHolder.ofObject("bitmapColor", new ArgbEvaluator(), colorFrom, colorTo)
        );
        pulseAnim.setDuration(300);
        pulseAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        pulseAnim.start();

        super.setSelected(selected);
    }

    @Override
    public void setBackgroundColor(@ColorInt int color) {
        backgroundPaint.setColor(color);
        backgroundPaint.setColorFilter(new PorterDuffColorFilter(color, Mode.SRC_IN));
        if (isSelected()) {
            imagePaint.setColorFilter(new PorterDuffColorFilter(color, Mode.SRC_IN));
        }
        postInvalidate();
    }

    public void setColor(@ColorInt int color) {
        primaryPaint.setColor(color);
        if (!isSelected()) {
            imagePaint.setColorFilter(new PorterDuffColorFilter(color, Mode.SRC_IN));
        }
        postInvalidate();
    }

    public void setBitmapColor(@ColorInt int color) {
        imagePaint.setColorFilter(new PorterDuffColorFilter(color, Mode.SRC_IN));
        postInvalidate();
    }

}
