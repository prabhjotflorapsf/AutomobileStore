package com.example.automobilestore.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.automobilestore.model.WishlistModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.WishlistViewHolder> {
    Context context;
    List<WishlistModel> wishlist;
    FirebaseFirestore db;
    FirebaseUser curUser;
    FirebaseAuth auth;
    String userid = null;


    public WishlistAdapter(Context context, List<WishlistModel> wishlist) {
        this.context = context;
        this.wishlist = wishlist;
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        curUser = auth.getCurrentUser();
        if (curUser != null) {
            userid = curUser.getUid();
        }

    }

    @NonNull
    @Override
    public WishlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.wishlist_item, parent, false);
        return new WishlistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final WishlistViewHolder holder, final int position) {
        Picasso.get().load(wishlist.get(position).getImage()).fit().into(holder.CarImage);


        String Transmission;


        holder.price.setText(wishlist.get(position).getPrice() + "$");
        holder.Model.setText(wishlist.get(position).getModel());
        holder.Conditon.setText(wishlist.get(position).getCondition());
        holder.Year.setText(wishlist.get(position).getYear());
        holder.Address.setText(wishlist.get(position).getAddress());
        holder.Transmission.setText(wishlist.get(position).getTransmission());

        holder.wishlistIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Query productIdRef = db.collection("Wishlist")
                        .whereEqualTo("Filter", userid + "_" + wishlist.get(position).getDocid());

                productIdRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {


                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            document.getReference().delete();
                            wishlist.remove(position);
                            notifyItemRemoved(position);
                            Toast.makeText(view.getContext(), "Removed from Wishlist", Toast.LENGTH_SHORT).show();
                            notifyItemRangeChanged(position, wishlist.size());
                        }
                    }


//

                });
            }
        });
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), CarDetails.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", wishlist.get(position).getDocid());
                i.putExtras(bundle);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return wishlist.size();
    }

    public static final class WishlistViewHolder extends RecyclerView.ViewHolder {

        ImageView CarImage, wishlistIcon;
        TextView price, Conditon, Year, Address, Model,Transmission;
        View item;

        public WishlistViewHolder(@NonNull View itemView) {
            super(itemView);
            CarImage = itemView.findViewById(R.id.wishlistimage);
            price = itemView.findViewById(R.id.wishlistPrice);
            Conditon = itemView.findViewById(R.id.Conditionwishlist);
            Year = itemView.findViewById(R.id.Yearwishlist);
            Address = itemView.findViewById(R.id.addresswishlist);
            Model = itemView.findViewById(R.id.Modelwishlist);
            wishlistIcon = itemView.findViewById(R.id.wishlisticon);
            Transmission=itemView.findViewById(R.id.Transmissionwishlist);
            item = itemView;
        }
    }
}
