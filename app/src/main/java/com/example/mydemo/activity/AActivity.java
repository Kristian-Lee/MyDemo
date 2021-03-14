package com.example.mydemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mydemo.R;
public class AActivity extends AppCompatActivity {

    private Button mBtnJumpB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a);
        mBtnJumpB = findViewById(R.id.btn_jumpB);
        mBtnJumpB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("title", "犬来八荒");
                bundle.putInt("value", 1995);
                Intent intent = new Intent(AActivity.this, BActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
