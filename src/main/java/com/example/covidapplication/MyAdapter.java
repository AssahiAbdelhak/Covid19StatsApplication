package com.example.covidapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    Context context;
    List<CountryModule> list;
    ViewGroup parent;
    EditText et;
    ImageView flag;

    public MyAdapter(Context context, List<CountryModule> list, EditText et,ImageView flag) {
        this.context = context;
        this.list = list;
        this.et = et;
        this.flag = flag;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.parent = parent;
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.country,parent,false));
    }



    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.countryName.setText(list.get(position).getCountryName());
        Picasso.get().load(list.get(position).getCountryFlag()).into(holder.flag);
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(holder.countryName.getText());
                parent.removeAllViewsInLayout();
                et.setText(holder.countryName.getText());
                Picasso.get().load(list.get(position).getCountryFlag()).into(flag);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    protected class MyViewHolder extends RecyclerView.ViewHolder{
        TextView countryName;
        ImageView flag;
        LinearLayout parent;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent);
            flag = itemView.findViewById(R.id.flag);
            countryName = itemView.findViewById(R.id.text);
        }
    }
}
