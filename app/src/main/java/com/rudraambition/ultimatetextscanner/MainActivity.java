package com.rudraambition.ultimatetextscanner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rudraambition.ultimatetextscanner.R;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

          getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

            progressBar = (ProgressBar) findViewById(R.id.progressBar);
            textView = (TextView) findViewById(R.id.textView);

            progressBar.setMax(100);
            progressBar.setScaleY(1f);
            progressAnimation();
        }

        public void progressAnimation ()
        {
            ProgressbarAnim progressbarAnim = new ProgressbarAnim(this, progressBar, textView, 0f, 100f);
            progressbarAnim.setDuration(4000);
            progressBar.setAnimation(progressbarAnim);
        }
    }