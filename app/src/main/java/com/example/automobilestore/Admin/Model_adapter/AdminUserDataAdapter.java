package com.example.automobilestore.Admin.Model_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.automobilestore.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AdminUserDataAdapter extends RecyclerView.Adapter<AdminUserDataAdapter.AdminUserDataViewHolder> {

    Context context;
    ArrayList<AdminUserData> list;

    public AdminUserDataAdapter(Context context, ArrayList<AdminUserData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @NotNull
    @Override
    public AdminUserDataViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_user , parent , false);
        return new AdminUserDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdminUserDataViewHolder holder, int position) {
        AdminUserData adminUserData = list.get(position);
        holder.Name.setText(adminUserData.getName());
        holder.Email.setText(adminUserData.getEmail());
        holder.Phone.setText(adminUserData.getPhone());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class AdminUserDataViewHolder extends RecyclerView.ViewHolder {

        TextView Name , Email , Phone;
        public AdminUserDataViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            Name = itemView.findViewById(R.id.userName);
            Email = itemView.findViewById(R.id.userEmail);
            Phone = itemView.findViewById(R.id.userPhoneNumber);
        }
    }

}
