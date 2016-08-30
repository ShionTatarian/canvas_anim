package fi.qvik.canvasanimation;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private CanvasView canvasView;
    private PulsingView pulsingView;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        pulsingView = (PulsingView) findViewById(R.id.pulsing_view);
        pulsingView.startPulseAnimation(2000);
        canvasView = (CanvasView) findViewById(R.id.canvasView);
        text = (TextView) findViewById(R.id.text);

    }

    public void onRandomClick(View view) {
        Random r = new Random();

        canvasView.setColor(Color.rgb(r.nextInt(255), r.nextInt(255), r.nextInt(255)));

        switch (r.nextInt(10)) {
            default:
            case 0:
                canvasView.setImage(R.drawable.ic_brightness_auto_black_48dp);
                break;
            case 1:
                canvasView.setImage(R.drawable.ic_cloud_off_black_48dp);
                break;
            case 2:
                canvasView.setImage(R.drawable.ic_color_lens_black_48dp);
                break;
            case 3:
                canvasView.setImage(R.drawable.ic_content_cut_black_48dp);
                break;
            case 4:
                canvasView.setImage(R.drawable.ic_euro_symbol_black_48dp);
                break;
            case 5:
                canvasView.setImage(R.drawable.ic_fingerprint_black_48dp);
                break;
            case 6:
                canvasView.setImage(R.drawable.ic_pets_black_48dp);
                break;
            case 7:
                canvasView.setImage(R.drawable.ic_wallpaper_black_48dp);
                break;
            case 8:
                canvasView.setImage(R.drawable.ic_ring_volume_black_48dp);
                break;
            case 9:
                canvasView.setImage(R.drawable.ic_open_with_black_48dp);
                break;
        }

        int freq = 200 * (1 + r.nextInt(10));
        text.setText(String.format("Pulse frequency: %d", freq));
        pulsingView.startPulseAnimation(freq);

    }

}
