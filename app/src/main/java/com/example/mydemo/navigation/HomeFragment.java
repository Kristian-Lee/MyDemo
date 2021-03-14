package com.example.mydemo.navigation;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mydemo.R;
import com.example.mydemo.recycleview.MyRecycleViewAdapter;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private MyRecycleViewAdapter adapter;
    private RecyclerView recyclerView;
    private List<String> nameList;
    private List<String> contentList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.rv);
        nameList = new ArrayList<>();
        contentList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            nameList.add("犬来" + i + "荒");
            contentList.add("这是第"+i+"个测试");
        }
        adapter = new MyRecycleViewAdapter(getActivity(), nameList, contentList, R.drawable.cg);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        return view;
    }
}