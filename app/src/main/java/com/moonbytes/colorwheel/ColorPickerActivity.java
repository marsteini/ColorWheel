package com.moonbytes.colorwheel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ColorPickerActivity extends AppCompatActivity {
    private ColorPickerView colorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_picker);

        colorView = (ColorPickerView) findViewById(R.id.colorPicker);
    }

    @Override
    protected void onResume() {
        super.onResume();
        colorView.onResumeView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        colorView.stop();
    }
}
