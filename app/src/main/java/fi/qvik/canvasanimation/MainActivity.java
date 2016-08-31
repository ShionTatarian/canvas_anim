package fi.qvik.canvasanimation;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private CanvasView canvasView;
    private PulsingView pulsingView;
    private TextView text;
    private ImageView transitionImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        transitionImage = (ImageView) findViewById(R.id.transition_image_view);
        pulsingView = (PulsingView) findViewById(R.id.pulsing_view);
        pulsingView.startPulseAnimation(2000);
        canvasView = (CanvasView) findViewById(R.id.canvasView);
        text = (TextView) findViewById(R.id.text);

    }

    public void onRandomClick(View view) {
        Random r = new Random();

        canvasView.setColor(Color.rgb(r.nextInt(255), r.nextInt(255), r.nextInt(255)));


        int freq = 200 * (1 + r.nextInt(10));
        text.setText(String.format("Pulse frequency: %d", freq));
        pulsingView.startPulseAnimation(freq);

        testTransitionDrawable();
    }

    private void testTransitionDrawable() {

        Drawable backgrounds[] = new Drawable[2];
        backgrounds[0] = transitionImage.getDrawable();
        backgrounds[1] = ActivityCompat.getDrawable(this, getRandomDrawableResource());
//        transitionImage.setImageResource(android.R.color.transparent);

        TransitionDrawable crossfader = new TransitionDrawable(backgrounds);
        crossfader.setCrossFadeEnabled(true);
        transitionImage.setImageDrawable(crossfader);

        crossfader.startTransition(400);
    }

    @DrawableRes
    private int getRandomDrawableResource() {
        int res;
        switch (new Random().nextInt(10)) {
            default:
            case 0:
                res = R.drawable.ic_brightness_auto_black_48dp;
                break;
            case 1:
                res = R.drawable.ic_cloud_off_black_48dp;
                break;
            case 2:
                res = R.drawable.ic_color_lens_black_48dp;
                break;
            case 3:
                res = R.drawable.ic_content_cut_black_48dp;
                break;
            case 4:
                res = R.drawable.ic_euro_symbol_black_48dp;
                break;
            case 5:
                res = R.drawable.ic_fingerprint_black_48dp;
                break;
            case 6:
                res = R.drawable.ic_pets_black_48dp;
                break;
            case 7:
                res = R.drawable.ic_wallpaper_black_48dp;
                break;
            case 8:
                res = R.drawable.ic_ring_volume_black_48dp;
                break;
            case 9:
                res = R.drawable.ic_open_with_black_48dp;
                break;
        }

        return res;
    }


}
