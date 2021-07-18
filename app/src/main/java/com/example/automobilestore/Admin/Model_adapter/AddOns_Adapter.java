package com.example.automobilestore.Admin.Model_adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.automobilestore.Activity.CarDetails;
import com.example.automobilestore.R;
import com.example.automobilestore.adapter.Horizontal_Car_Adapter;
import com.example.automobilestore.model.HorizontalCarData;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AddOns_Adapter extends RecyclerView.Adapter<AddOns_Adapter.AddOnsViewHolder> {
    Context context;
    List<AddOns> AddOnsList;
    public AddOns_Adapter(Context context, List<AddOns> popularFoodList) {
        this.context = context;
        this.AddOnsList = popularFoodList;
    }
    @NonNull
    @NotNull
    @Override
    public AddOnsViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.add_ons_item, parent, false);
        return new AddOns_Adapter.AddOnsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AddOnsViewHolder holder, int position) {
        Picasso.get().load(AddOnsList.get(position).getImage()).fit().into(holder.image);
        holder.comapny.setText(AddOnsList.get(position).getComapny());
        holder.address.setText("$"+AddOnsList.get(position).getAddress());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//
//                Toast.makeText(context, "position"+position, Toast.LENGTH_SHORT).show();
                        TextInputLayout Comp,addr,link;
                        Comp= view.findViewById(R.id.add_comapny);
                        addr=view.findViewById(R.id.add_address);
                        link=view.findViewById(R.id.add_Link);
                        Comp.getEditText().setText(AddOnsList.get(position).getComapny());
                        addr.getEditText().setText(AddOnsList.get(position).getAddress());
                        link.getEditText().setText(AddOnsList.get(position).getUrl());

                    }
                });


            }
        });

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class AddOnsViewHolder extends RecyclerView.ViewHolder {


        ImageView image;
        TextView comapny, address;

        public AddOnsViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.add_ons_image);
            comapny = itemView.findViewById(R.id.item_company);
            address = itemView.findViewById(R.id.item_Address);



        }
    }
}
