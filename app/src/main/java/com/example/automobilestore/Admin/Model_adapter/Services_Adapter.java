package com.example.automobilestore.Admin.Model_adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.automobilestore.Activity.WebViewCustom;

import com.example.automobilestore.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static android.content.ContentValues.TAG;

public class Services_Adapter extends RecyclerView.Adapter<Services_Adapter.ServicesViewHolder> {
    Context context;
    List<Services> AddOnsList;
    String str;
    public Services_Adapter(Context context, List<Services> addOnsList,String str) {
        this.context = context;
        AddOnsList = addOnsList;
        this.str=str;
    }

    @NonNull
    @NotNull
    @Override
    public ServicesViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.services_item, parent, false);
        return new ServicesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ServicesViewHolder holder, int position) {
        Log.d(TAG, "Services_Adapter: "+str);
        Picasso.get().load(AddOnsList.get(position).getImage()).fit().into(holder.image);
        holder.company.setText(AddOnsList.get(position).getCompany());
        holder.address.setText(AddOnsList.get(position).getAddress());
        Log.d(TAG, "onBindViewHolder: " + 1);
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (str == "Admin") {
                    Intent i = new Intent(view.getContext(), Post_Services.class);
                    i.putExtra("id", AddOnsList.get(position).getId());
                    context.startActivity(i);
                }else if(str=="User"){


                    Intent i = new Intent(view.getContext(), WebViewCustom.class);
                    i.putExtra("Link", AddOnsList.get(position).getUrl());
                    context.startActivity(i);

                }else{
                    Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }
    @Override
    public int getItemCount() {
        return AddOnsList.size();
    }

    public class ServicesViewHolder extends RecyclerView.ViewHolder {

        View item;
        ImageView image;
        TextView company, address;

        public ServicesViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.services_image);
            company = itemView.findViewById(R.id.services_company);
            address = itemView.findViewById(R.id.services_Address);
            item = itemView;


        }
    }
}
