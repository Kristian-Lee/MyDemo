package com.example.mydemo.recycleview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mydemo.R;

import java.util.List;

import cn.gavinliu.android.lib.shapedimageview.ShapedImageView;

public class MyRecycleViewAdapter extends RecyclerView.Adapter<MyRecycleViewAdapter.MyViewHolder> {

    private Context context;
    private List<String> nameList;
    private List<String> contentList;
    private View inflater;
    private int resId;

    public MyRecycleViewAdapter(Context context, List<String> nameList, List<String> contentList, int resId) {
        this.context = context;
        this.nameList = nameList;
        this.contentList = contentList;
        this.resId = resId;
    }

    @NonNull
    @Override
    public MyRecycleViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(context).inflate(R.layout.layout_list_item, parent, false);
        return new MyViewHolder(inflater);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecycleViewAdapter.MyViewHolder holder, int position) {
        holder.tvName.setText(nameList.get(position));
        holder.tvContent.setText(contentList.get(position));
        holder.imageView.setImageResource(resId);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "第" + position + "个item被点击", Toast.LENGTH_SHORT).show();
            }
        });
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(context, "第" + position + "个item被长按", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return nameList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvContent;
        ShapedImageView imageView;
        CardView cardView;
        public MyViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tv_name);
            tvContent = view.findViewById(R.id.tv_content);
            imageView = view.findViewById(R.id.iv);
            cardView = view.findViewById(R.id.card);
        }
    }

}
