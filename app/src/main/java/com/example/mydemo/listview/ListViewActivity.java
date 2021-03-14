package com.example.mydemo.listview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mydemo.R;
import com.gyf.immersionbar.ImmersionBar;

public class ListViewActivity extends AppCompatActivity {

    private AlphaAnimation mShowAnim, mHiddenAmin;//控件的显示和隐藏动画
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        mShowAnim = new AlphaAnimation(0.0f, 1.0f);
        mShowAnim.setDuration(300);

        //控件隐藏的动画
        mHiddenAmin = new AlphaAnimation(1.0f, 0.0f);
        mHiddenAmin.setDuration(300);
        listView = findViewById(R.id.lv);

        //消除item分割线
        listView.setDivider(null);
        listView.setAdapter(new MyListAdapter(ListViewActivity.this));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ListViewActivity.this,
                        "第"+ position + "个item被点击", Toast.LENGTH_SHORT).show();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ListViewActivity.this,
                        "第"+ position + "个item被长按", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        ImmersionBar.with(this)
                .statusBarColor(R.color.white)
                .statusBarDarkFont(true, 0.2f)
                .init();
    }
}