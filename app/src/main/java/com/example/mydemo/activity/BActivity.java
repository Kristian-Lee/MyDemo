package com.example.mydemo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.mydemo.R;

public class BActivity extends AppCompatActivity {

    private Button mBtnJumpA;
    private TextView mTvTitle;
    private TextView mTvContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b);
        mTvTitle = findViewById(R.id.tv_title);
        mTvContent = findViewById(R.id.tv_value);
        mBtnJumpA = findViewById(R.id.btn_jumpA);
        Bundle bundle = getIntent().getExtras();
        mTvTitle.setText(bundle.getString("title"));
        Log.d("Tips", "成功运行至第一个settext");
        int value = bundle.getInt("value");
        Log.d("Tips", ""+ value);
        mTvContent.setText(String.valueOf(value));
        mBtnJumpA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BActivity.this, AActivity.class);
                startActivity(intent);
            }
        });
    }
}