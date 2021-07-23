package com.example.automobilestore.Admin.Model_adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.automobilestore.Activity.UpdateAd;
import com.example.automobilestore.Activity.WebViewCustom;
import com.example.automobilestore.Admin.Post_AddOns;
import com.example.automobilestore.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static android.content.ContentValues.TAG;

public class AddOns_Adapter extends RecyclerView.Adapter<AddOns_Adapter.AddOnsViewHolder> {
    Context context;
    List<AddOns> AddOnsList;
   
    
    String str;
    public AddOns_Adapter(Context context, List<AddOns> popularFoodList,String str) {
        this.context = context;
        this.AddOnsList = popularFoodList;
        this.str=str;
    }
    @NonNull
    @NotNull
    @Override
    public AddOnsViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.add_ons_item, parent, false);

        return new AddOnsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AddOnsViewHolder holder, int position) {
        Picasso.get().load(AddOnsList.get(position).getImage()).fit().into(holder.image);
        holder.company.setText(AddOnsList.get(position).getCompany());
        holder.address.setText(AddOnsList.get(position).getAddress());
        Log.d(TAG, "onBindViewHolder: "+1);

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (str == "Admin") {
                    Intent i = new Intent(view.getContext(), Post_AddOns.class);
                    i.putExtra("id", AddOnsList.get(position).getId());
                    context.startActivity(i);
                }else if(str=="User"){


                    Intent i = new Intent(view.getContext(), WebViewCustom.class);
                    i.putExtra("Link", AddOnsList.get(position).getUrl());
                    context.startActivity(i);


                    
                }else{
                    Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                }


//                Comp= view.findViewById(R.id.add_c);
//                addr=view.findViewById(R.id.add_a);
//                link=view.findViewById(R.id.add_l);
//                Log.d(TAG, "onClick: "+AddOnsList.get(position).getAddress());
//
//                Comp.getEditText().setText(AddOnsList.get(position).getCompany());
//                addr.getEditText().setText(AddOnsList.get(position).getAddress());
//                link.getEditText().setText(AddOnsList.get(position).getUrl());
            }
        });

    }

    @Override
    public int getItemCount() {
        return AddOnsList.size();
    }

    public class AddOnsViewHolder extends RecyclerView.ViewHolder {

        View item;
        ImageView image;
        TextView company, address;

        public AddOnsViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.add_ons_image);
            company = itemView.findViewById(R.id.item_company);
            address = itemView.findViewById(R.id.item_Address);
            item = itemView;


        }
    }
}
