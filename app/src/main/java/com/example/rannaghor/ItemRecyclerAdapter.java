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

public class ItemRecyclerAdapter extends RecyclerView.Adapter<ItemRecyclerAdapter.ItemRecyclerViewHolder> {
    //==================== DECLARING VARIABLES & OBJECTS =========================
    Context context;
    ArrayList<ItemCategoryModel> itemCategoryList=new ArrayList<>();
    StorageReference STreference;
    //==================== CONSTRUCTOR FOR ADAPTER CLASS =========================


    public ItemRecyclerAdapter(Context context, ArrayList<ItemCategoryModel> itemCategoryList) {
        this.context = context;
        this.itemCategoryList = itemCategoryList;
    }

    //==================== OVERRIDE METHOD FOR CREATE VIEW ========================
    @NonNull
    @Override
    public ItemRecyclerAdapter.ItemRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_view_holder,parent,false);
        return new ItemRecyclerViewHolder(view);
    }

    //==================== OVERRIDE METHOD FOR BINDING VIEW WITH DATA ==============
    @Override
    public void onBindViewHolder(@NonNull ItemRecyclerAdapter.ItemRecyclerViewHolder holder, int position) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference photoReference= storageReference.child("img_item/"+itemCategoryList.get(position).getITEM_CATEGORY_IMAGE());

        final long ONE_MEGABYTE = 1024*1024;
        photoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                holder.itemCategoryImage.setImageBitmap(bmp);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
//                Toast.makeText(context.getApplicationContext(), "No Such file or Path found!!", Toast.LENGTH_LONG).show();
            }
        });
        holder.itemCategoryName.setText(itemCategoryList.get(position).getITEM_CATEGORY_NAME());
        holder.itemCategoryID=itemCategoryList.get(position).getITEM_CATEGORY_ID();
    }

    //==================== OVERRIDE METHOD FOR COUNTING ITEMS ======================
    @Override
    // =================== FOR RETURN ONLY 12 ITEMS IN HOME SCREEN
    public int getItemCount() {
        if(itemCategoryList.size()>12)
            return 12;
        else
            return itemCategoryList.size();
    }

    //======================== CLASS FOR VIEWHOLDER ===============================
    public class ItemRecyclerViewHolder extends RecyclerView.ViewHolder {
        //================= DECLARING OBJECTS & VARIABLES FOR VIEWHOLDER ===========
        String itemCategoryID;
        ImageView itemCategoryImage;
        TextView itemCategoryName;
        public ItemRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            //=============== ATTATCHEING COMPONENTS ==============================
            itemCategoryImage=(ImageView) itemView.findViewById(R.id.iv_item_image);
            itemCategoryName=(TextView)itemView.findViewById(R.id.tv_item_name);

            itemCategoryImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent restaurantListIntent=new Intent(context,RestaurantListAcivity.class);
                    restaurantListIntent.putExtra("itemCatID",itemCategoryID);
                    restaurantListIntent.putExtra("itemCatName",itemCategoryName.getText());
                    context.startActivity(restaurantListIntent);
                }
            });
        }
    }
}
