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
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class RestaurentRecyclerAdapter extends RecyclerView.Adapter<RestaurentRecyclerAdapter.RestaurentRecyclerViewHolder> {
    //==================== DECLARING OBJECTS & VARIABLES ========================
    Context context;
    ArrayList<RestaurentModel> restaurentList=new ArrayList<>();

    //============================== CONSTRUCTOR ================================


    public RestaurentRecyclerAdapter(Context context, ArrayList<RestaurentModel> restaurentList) {
        this.context = context;
        this.restaurentList = restaurentList;
    }

    // =================OVERRIDE METHOD FOR ON CREATE VIEW HOLDER ===============
    @NonNull
    @Override
    public RestaurentRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.restaruent_view_holder,parent,false);
        return new RestaurentRecyclerViewHolder(view);
    }

    //=============== OVERRIDE METHOD FOR ON BIND VIEW HOLDER ===================
    @Override
    public void onBindViewHolder(@NonNull RestaurentRecyclerViewHolder holder, int position) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference photoReference= storageReference.child("img_restaurants/"+restaurentList.get(position).getRESTAURENT_IMG());

        final long ONE_MEGABYTE = 1024*1024;
        photoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                holder.restaurentImage.setImageBitmap(bmp);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
//                Toast.makeText(context.getApplicationContext(), "No Such file or Path found!!", Toast.LENGTH_LONG).show();
            }
        });
        holder.restaurentName.setText(restaurentList.get(position).getRESTAURENT_NAME());
        holder.restarurntID=restaurentList.get(position).getRESTAURENT_ID();
        holder.restaurantTag=restaurentList.get(position).getTAGS();
    }


    //=============== OVERRIDE METHOD FOR COUNT RESTAURENTS ====================
    @Override
    public int getItemCount() {
        if(restaurentList.size()>=6)
            return 6;
        else
            return restaurentList.size();
    }

    //============== VIEWHOLDER CLASS FOR RESTAURENTS ==========================
    public class RestaurentRecyclerViewHolder extends RecyclerView.ViewHolder {
        ImageView restaurentImage;
        TextView restaurentName;
        String restarurntID;
        String restaurantTag;
        public RestaurentRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            restaurentImage=itemView.findViewById(R.id.iv_restaruent_img);
            restaurentName=itemView.findViewById(R.id.tv_restaruent_name);

            restaurentImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent itemListIntent=new Intent(context,ItemListActivity.class);
                    itemListIntent.putExtra("option","2");
                    itemListIntent.putExtra("restaurantID",restarurntID);
                    itemListIntent.putExtra("restaurantName",restaurentName.getText());
                    itemListIntent.putExtra("restaurantTag",restaurantTag);
                    context.startActivity(itemListIntent);
                }
            });
        }
    }
}
