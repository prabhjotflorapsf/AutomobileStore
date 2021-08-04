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

import com.example.automobilestore.Activity.UpdateAd;
import com.example.automobilestore.Activity.WebViewCustom;
import com.example.automobilestore.Admin.Post_Services;
import com.example.automobilestore.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static android.content.ContentValues.TAG;

public class Report_Adapter  extends RecyclerView.Adapter<Report_Adapter.ServicesViewHolder>{
    Context context;
    List<Report_List> ReportList;

    public Report_Adapter(Context context, List<Report_List> ReportList) {
        Log.d(TAG, "Report_Adapter: ");
        this.context = context;
        this.ReportList = ReportList;

    }

    @NonNull
    @NotNull
    @Override
    public ServicesViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.report_list_item, parent, false);
        Log.d(TAG, "onCreateViewHolder: ");
        return new Report_Adapter.ServicesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ServicesViewHolder holder, int position) {

        Picasso.get().load(ReportList.get(position).getImage()).fit().into(holder.image);
        holder.reason.setText("Reason to Report: \n"+ReportList.get(position).getReason());
        holder.user.setText("Reported By: \n"+ReportList.get(position).getUser());
        Log.d(TAG, "onBindViewHolder: " + 1);
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), UpdateAd.class);
                i.putExtra("id", ReportList.get(position).getId());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return ReportList.size();
    }

    public class ServicesViewHolder extends RecyclerView.ViewHolder {
        View item;
        ImageView image;
        TextView reason, user;

        public ServicesViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.report_image);
            reason = itemView.findViewById(R.id.item_reason);
            user = itemView.findViewById(R.id.item_user);
            item = itemView;
    }
}
}
