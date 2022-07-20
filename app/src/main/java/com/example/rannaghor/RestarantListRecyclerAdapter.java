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

public class RestarantListRecyclerAdapter extends RecyclerView.Adapter<RestarantListRecyclerAdapter.RestaurantListRecyclerViewHolder> {
    //=================================== DECLARING OBJECTS AND VARIABLES ==============================
    Context context;
    String itemCatID;
    ArrayList<RestaurentModel>restaurentList =new ArrayList<>();
    //================================== REQUIRED CONSTRUCTOR =========================================


    public RestarantListRecyclerAdapter(Context context, ArrayList<RestaurentModel> restaurentList,String itemCatID) {
        this.context = context;
        this.itemCatID=itemCatID;
        this.restaurentList = restaurentList;
    }

    //================================== ON CRREATE VIEWHOLDER METHOD =================================
    @NonNull
    @Override
    public RestarantListRecyclerAdapter.RestaurantListRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.restaurent_list_view_holder,parent,false);
        return new RestaurantListRecyclerViewHolder(view);
    }

    //================================ ON BIND VIEWHOLDER METHOD ====================================
    @Override
    public void onBindViewHolder(@NonNull RestarantListRecyclerAdapter.RestaurantListRecyclerViewHolder holder, int position) {
        holder.restaurantID= restaurentList.get(position).getRESTAURENT_ID();
        holder.restaurantName.setText(restaurentList.get(position).getRESTAURENT_NAME());
        holder.restaurantTag=restaurentList.get(position).getTAGS();
        if(holder.restaurantID.startsWith("B")){
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            StorageReference photoReference= storageReference.child("img_brands/"+restaurentList.get(position).getRESTAURENT_IMG());

            final long ONE_MEGABYTE = 1024*1024;
            photoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    holder.restaurantLogo.setImageBitmap(bmp);

                     }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
//                Toast.makeText(context.getApplicationContext(), "No Such file or Path found!!", Toast.LENGTH_LONG).show();
                }
            });

        }
        if(holder.restaurantID.startsWith("R")){
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            StorageReference photoReference= storageReference.child("img_restaurants/"+restaurentList.get(position).getRESTAURENT_IMG());

            final long ONE_MEGABYTE = 1024*1024;
            photoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    holder.restaurantLogo.setImageBitmap(bmp);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
//                Toast.makeText(context.getApplicationContext(), "No Such file or Path found!!", Toast.LENGTH_LONG).show();
                }
            });

        }
    }

    //=============================== GET TOTAL ITEM COUNT =========================================
    @Override
    public int getItemCount() {
        return restaurentList.size();
    }


    //============================== VIEWHOLDER CLASS FOR CREATING VIEW ============================
    public class RestaurantListRecyclerViewHolder extends RecyclerView.ViewHolder {
        //========================== DECLARING OBJECTS AND VARIABLE FOR VIEW HOLDER ======================
        ImageView restaurantLogo;
        TextView restaurantName;
        String restaurantID;
        String restaurantTag;
        CardView cardViewRestaurant;
        public RestaurantListRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            //========================== ATTACHING VIEW COMPONENTS ====================================
            restaurantLogo=(ImageView) itemView.findViewById(R.id.iv_restaurant_logo);
            restaurantName=(TextView) itemView.findViewById(R.id.tv_rest_name);
            cardViewRestaurant=(CardView) itemView.findViewById(R.id.cv_restaurant_list);

            //========================= SET ONCLICK LISTENER IN CARD VIEW =========================
            cardViewRestaurant.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent itemListIntent=new Intent(context,ItemListActivity.class);
                    itemListIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    itemListIntent.putExtra("option","3");
                    itemListIntent.putExtra("restaurantID",restaurantID);
                    itemListIntent.putExtra("restaurantName",restaurantName.getText().toString());
                    itemListIntent.putExtra("itemCatID",itemCatID);
                    itemListIntent.putExtra("restaurantTag",restaurantTag);
                    context.startActivity(itemListIntent);
                }
            });
        }
    }
}
