package fi.qvik.canvasanimation;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

/**
 * Created by Tommy on 29/8/2016.
 */

public class PulsingView extends ImageView {

    private static final String TAG = "PulsingView";
    private ValueAnimator pulseAnim;
    private int duration = 0;

    public PulsingView(Context context) {
        super(context);
        init();
    }

    public PulsingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PulsingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(VERSION_CODES.LOLLIPOP)
    public PulsingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        Context appContext = getContext().getApplicationContext();
        if (appContext instanceof Application) {
            registerActivityListener((Application) appContext);
        }
        setImageResource(R.drawable.pulse);
    }

    public void startPulseAnimation(int duration) {
        if (duration == 0) {
            pause();
            return;
        }
        this.duration = duration;
        pulseAnim = ObjectAnimator.ofPropertyValuesHolder(PulsingView.this,
                PropertyValuesHolder.ofFloat("scaleX", 0f, 1f),
                PropertyValuesHolder.ofFloat("scaleY", 0f, 1f),
                PropertyValuesHolder.ofFloat("alpha", 1f, 0.5f)
        );
        pulseAnim.setDuration(duration);
        pulseAnim.setInterpolator(new OvershootInterpolator());
        pulseAnim.setRepeatCount(ObjectAnimator.INFINITE);
        pulseAnim.setRepeatMode(ObjectAnimator.REVERSE);
        pulseAnim.start();
    }

    private Activity getActivity() {
        Context context = getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    private boolean isMyActivity(Activity activity) {
        Activity myActivity = getActivity();
        return myActivity != null && myActivity.equals(activity);
    }

    private void registerActivityListener(Application app) {
        if (app == null) {
            return;
        }
        app.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {
            }

            @Override
            public void onActivityStarted(Activity activity) {
            }

            @Override
            public void onActivityResumed(Activity activity) {
                if (isMyActivity(activity)) {
                    resume();
                }
            }

            @Override
            public void onActivityPaused(Activity activity) {
                if (isMyActivity(activity)) {
                    pause();
                }
            }

            @Override
            public void onActivityStopped(Activity activity) {
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                if (isMyActivity(activity)) {
                    Context appContext = getContext().getApplicationContext();
                    if (appContext != null && appContext instanceof Application) {
                        ((Application) appContext).unregisterActivityLifecycleCallbacks(this);
                    }
                }
            }
        });
    }

    private void pause() {
        Log.d(TAG, "onPause");
        if (pulseAnim == null) {
            return;
        }
        pulseAnim.end();
        pulseAnim = null;
    }

    private void resume() {
        Log.d(TAG, "onResume");
        startPulseAnimation(this.duration);
    }

}
