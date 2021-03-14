package com.example.mydemo.recycleview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mydemo.R;
import com.example.mydemo.SearchViewActivity;
import com.example.mydemo.navigation.HomeFragment;
import com.example.mydemo.navigation.MessageFragment;
import com.example.mydemo.navigation.MyFragment;
import com.ferfalk.simplesearchview.SimpleSearchView;
import com.ferfalk.simplesearchview.utils.DimensUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.gyf.immersionbar.ImmersionBar;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.List;

public class RecycleViewActivity extends AppCompatActivity {

    public static final int EXTRA_REVEAL_CENTER_PADDING = 40;
    private SimpleSearchView searchView;
    private List<Fragment> fragments;
    private SparseIntArray items;
    private int previousPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        searchView = findViewById(R.id.searchView);

        BottomNavigationViewEx bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.enableAnimation(true);
        bottomNavigationView.enableShiftingMode(true);
        bottomNavigationView.enableItemShiftingMode(true);
        bottomNavigationView.setIconSize(20);
        bottomNavigationView.setTextSize(10);
        items = new SparseIntArray(3);
        items.put(R.id.navigation_home, 0);
        items.put(R.id.navigation_message, 1);
        items.put(R.id.navigation_my, 2);
        fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new MessageFragment());
        fragments.add(new MyFragment());

        //实例化viewpager的适配器并设置
        ViewPagerAdapter sectionsPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
        ViewPager viewPager = findViewById(R.id.container);
        viewPager.setAdapter(sectionsPagerAdapter);

//        NavController navController = Navigation.findNavController();
//        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        //设置viewpager滚动监听器，viewpager滚动时，底部导航也跟着变化
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                previousPosition = position;
                bottomNavigationView.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //设置底部导航监听器，底部导航变化时，viewpager也跟着变化
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

//                switch (item.getItemId()) {
//                    case R.id.navigation_home:
//                        viewPager.setCurrentItem(0);
//                    case R.id.navigation_message:
//                        viewPager.setCurrentItem(1);
//                    case R.id.navigation_my:
//                        viewPager.setCurrentItem(2);
//                }
                int position = items.get(item.getItemId());
                if (previousPosition != position) {
                    // only set item when item changed
                    previousPosition = position;
                    //false表示关闭切换动效
                    viewPager.setCurrentItem(position, false);
                }
                return true;
            }
        });

        //状态栏沉浸
        ImmersionBar.with(this)
                .statusBarView(R.id.view)
                .init();
    }



    //导入并设置菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        setupSearchView(menu);
        return true;
    }

    private void setupSearchView(Menu menu) {
        //找到并设置开启searchview的item
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        searchView.setBackIconColor(Color.parseColor("#42A5F5"));
        // Adding padding to the animation because of the hidden menu item
        Point revealCenter = searchView.getRevealAnimationCenter();
        revealCenter.x -= DimensUtils.convertDpToPx(EXTRA_REVEAL_CENTER_PADDING, this);
    }

    //监听区分searchview的返回和其他按键返回
    @Override
    public void onBackPressed() {
        if (searchView.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (searchView.onActivityResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    //viewpager适配器，根据位置返回对应的fragment
    public class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments;
        ViewPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}