package com.oliveapp.liveness.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.oliveapp.liveness.R;
import com.oliveapp.liveness.sample.liveness.SampleStartActivity;
import com.oliveapp.liveness.sample.register.SampleChooseCameraActivity;

public class SampleLaunchActivity extends Activity {
    public static final String TAG = SampleLaunchActivity.class.getSimpleName();

    private Button mCameraButton;
    private Button mLivenessButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 如果有设置全局包名的需要, 在这里进行设置
//        PackageNameManager.setPackageName();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_launch);

        mCameraButton = (Button) findViewById(R.id.startButton);
        mLivenessButton = (Button) findViewById(R.id.livenessButton);

        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SampleChooseCameraActivity.class);
                startActivity(i);
            }
        });

        mLivenessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SampleStartActivity.class);
                startActivity(i);
            }
        });
    }

}
