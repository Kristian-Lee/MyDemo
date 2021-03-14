package com.example.mydemo.fragment;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mydemo.R;
public class FragmentActivity extends AppCompatActivity implements com.example.mydemo.fragment.AFragment.Change {

    private com.example.mydemo.fragment.AFragment aFragment;
    private Button mBtnChange;
    private com.example.mydemo.fragment.BFragment bFragment;
    private TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        aFragment = com.example.mydemo.fragment.AFragment.newInstance("newInstance()构造成功");
//        aFragment = new AFragment("有参构造成功");
        //标记这个Fragment，方便复用
        getSupportFragmentManager().beginTransaction().add(R.id.fragment, aFragment, "a").commitAllowingStateLoss();
        mBtnChange = findViewById(R.id.btn_change);
        mBtnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bFragment = new com.example.mydemo.fragment.BFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, bFragment).commitAllowingStateLoss();
            }
        });
        mTextView = findViewById(R.id.tv_fragment);
    }

    @Override
    public void changeText(String text) {
        mTextView.setText(text);
    }
}