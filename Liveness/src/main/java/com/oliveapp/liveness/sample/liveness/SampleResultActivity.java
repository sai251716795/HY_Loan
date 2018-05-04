package com.oliveapp.liveness.sample.liveness;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;


public class SampleResultActivity extends Activity {
    public static final String TAG = SampleResultActivity.class.getSimpleName();

    private TextView mOliveappResultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getResources().getIdentifier("activity_sample_result", "layout", getPackageName()));

        mOliveappResultTextView = (TextView) findViewById(getResources().getIdentifier("oliveappResultTextView", "id", getPackageName()));

        Boolean isSuccess = getIntent().getBooleanExtra("is_success", false);

        if (isSuccess) {
            mOliveappResultTextView.setText(getText(getResources().getIdentifier("oliveapp_liveness_detection_pass_hint", "string", getPackageName())));
        } else {
            mOliveappResultTextView.setText(getText(getResources().getIdentifier("oliveapp_liveness_detection_fail_hint", "string", getPackageName())));
        }
    }
}
