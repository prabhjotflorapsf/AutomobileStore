package com.example.automobilestore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.automobilestore.R;
import com.example.automobilestore.model.VerticalCarData;

import java.util.List;


public class Vertical_Car_Adapter extends RecyclerView.Adapter<Vertical_Car_Adapter.VerticalViewHolder> {

    Context context;
    List<VerticalCarData> asiaFoodList;



    public Vertical_Car_Adapter(Context context, List<VerticalCarData> asiaFoodList) {
        this.context = context;
        this.asiaFoodList = asiaFoodList;
    }

    @NonNull
    @Override
    public VerticalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.veritcal_item, parent, false);
        return new VerticalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VerticalViewHolder holder, int position) {

        holder.carv_image.setImageResource(asiaFoodList.get(position).getImageUrl());
        holder.v_name.setText(asiaFoodList.get(position).getName());
        holder.v_amount.setText(asiaFoodList.get(position).getPrice());
        holder.Condition_tv.setText(asiaFoodList.get(position).getCondition());

    }

    @Override
    public int getItemCount() {
        return asiaFoodList.size();
    }


    public static final class VerticalViewHolder extends RecyclerView.ViewHolder{


        ImageView carv_image;
        TextView v_amount, v_name, Condition_tv;

        public VerticalViewHolder(@NonNull View itemView) {
            super(itemView);

            carv_image = itemView.findViewById(R.id.carv_image);
            v_amount = itemView.findViewById(R.id.v_amount);
            v_name = itemView.findViewById(R.id.v_name);
            Condition_tv = itemView.findViewById(R.id.Condition_tv);



        }
    }

}
