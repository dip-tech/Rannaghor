package com.example.rannaghor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class NearbyRestaurentRecyclerAdapter extends RecyclerView.Adapter<NearbyRestaurentRecyclerAdapter.NearbyRestaurentRecyclerViewHolder> {
    // ===================DECLARING OBJECTS AND VARIABLES============================
    Context context;
    ArrayList<RestaurentModel>nearbyRestaurantList=new ArrayList<>();

    // ===================CONSTRUCTOR============================
    public NearbyRestaurentRecyclerAdapter(Context context, ArrayList<RestaurentModel> nearbyRestaurantList) {
        this.context = context;
        this.nearbyRestaurantList = nearbyRestaurantList;
    }

    // ===================OVERRIDE METHOD FOR ON CREATE VIEW HOLDER============================
    @NonNull
    @Override
    public NearbyRestaurentRecyclerAdapter.NearbyRestaurentRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.nearby_restaurant_viewholder,parent,false);
        return new NearbyRestaurentRecyclerViewHolder(view);
    }

    //===================OVERRIDE METHOD FOR ON BIND VIEW HOLDER============================
    @Override
    public void onBindViewHolder(@NonNull NearbyRestaurentRecyclerAdapter.NearbyRestaurentRecyclerViewHolder holder, int position) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference photoReference= storageReference.child("img_restaurants/"+nearbyRestaurantList.get(position).getRESTAURENT_IMG());

        final long ONE_MEGABYTE = 1024*1024;
        photoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                holder.nearbyRestaurantImage.setImageBitmap(bmp);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
//                Toast.makeText(context.getApplicationContext(), "No Such file or Path found!!", Toast.LENGTH_LONG).show();
            }
        });
        holder.nearbyRestaurantName.setText(nearbyRestaurantList.get(position).getRESTAURENT_NAME());
        holder.nearbyRestaurantTags.setText(nearbyRestaurantList.get(position).getTAGS());
        holder.resID=nearbyRestaurantList.get(position).getRESTAURENT_ID();
    }
    //===================GET ITEM COUNT============================
    @Override
    public int getItemCount() {
        if(nearbyRestaurantList.size()>=4)
            return 4;
        else
            return nearbyRestaurantList.size();
    }
    //===================NREARBY RESTAURANTS VIEWHOLDER CLASS============================
    public class NearbyRestaurentRecyclerViewHolder extends RecyclerView.ViewHolder {
        //==================DECLARING OBJECTS AND VARIABLS======================
        String resID;
        ImageView nearbyRestaurantImage;
        TextView nearbyRestaurantName;
        TextView nearbyRestaurantTags;
        CardView cardViewRestaurant;
        public NearbyRestaurentRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            //=================ATTATCHING VIEW OBJECTS======================
            nearbyRestaurantImage=(ImageView) itemView.findViewById(R.id.iv_nearby_restaurant);
            nearbyRestaurantName=(TextView) itemView.findViewById(R.id.tv_res_nearby_name);
            nearbyRestaurantTags=(TextView) itemView.findViewById(R.id.tv_res_nearby_tags);
            cardViewRestaurant=(CardView) itemView.findViewById(R.id.cv_view);

            //====================== SET ONCLICK LISTENER ON CARDVIEW ====================
            cardViewRestaurant.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent itemListIntent=new Intent(context,ItemListActivity.class);
                    itemListIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    itemListIntent.putExtra("option","2");
                    itemListIntent.putExtra("restaurantID",resID);
                    itemListIntent.putExtra("restaurantName",nearbyRestaurantName.getText().toString());
                    itemListIntent.putExtra("restaurantTag",nearbyRestaurantTags.getText().toString());
                    context.startActivity(itemListIntent);
                }
            });


        }
    }
}
