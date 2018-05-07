package com.sai.legukey;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.legusafekeyboard.SafeEditText;

public class MainActivity extends AppCompatActivity {
    private SafeEditText mEditText1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEditText1 = (SafeEditText) findViewById(R.id.legu_text_password1);

        Button button = (Button)findViewById(R.id.button_get_password);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, mEditText1.getString(), Toast.LENGTH_SHORT).show();
            }
        });

        Button button4 = (Button)findViewById(R.id.button_gethash);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, mEditText1.getHash(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
