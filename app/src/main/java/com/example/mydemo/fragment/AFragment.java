package com.example.mydemo.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.mydemo.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private TextView mTextView;
    private Button mBtnChangeFragment;
    private Button mBtnChangeText;
    private Button mBtnChangeActivityText;
    private BFragment bFragment;
    private Change change;

    public AFragment() {
        // Required empty public constructor
    }
    public AFragment(String text) {
        mParam1 = text;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment AFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AFragment newInstance(String param1) {
        AFragment fragment = new AFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_a, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTextView = view.findViewById(R.id.fragment_a_tv);
        if(mParam1 != null) {
            mTextView.setText(mParam1);
        }
        mBtnChangeFragment = view.findViewById(R.id.btnChangeToBFragment);
        mBtnChangeFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bFragment == null) {
                    bFragment = new BFragment();
                }
                Fragment fragment = getFragmentManager().findFragmentByTag("a");
                if (fragment != null) {
                    getFragmentManager().beginTransaction().hide(fragment).add(R.id.fragment, bFragment)
                            .addToBackStack(null).commitAllowingStateLoss();
                }
                else {
                    getFragmentManager().beginTransaction().replace(R.id.fragment, bFragment)
                            .addToBackStack(null).commitAllowingStateLoss();
                }

            }
        });
        mBtnChangeText = view.findViewById(R.id.btnChangeText);
        mBtnChangeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextView.setText("文字已改变");
            }
        });
        mBtnChangeActivityText = view.findViewById(R.id.btnChangeActivityText);
        mBtnChangeActivityText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change.changeText("已改变Activity文字");
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            change = (Change) context;
        } catch (ClassCastException e) {
            Log.d("error", "activity必须实现Change接口");
        }
    }

    public interface Change {
        void changeText(String text);
    }
}