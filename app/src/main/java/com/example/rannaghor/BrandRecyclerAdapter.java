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

public class BrandRecyclerAdapter extends RecyclerView.Adapter<BrandRecyclerAdapter.BrandRecyclerViewHolder> {
    //================== DECLARING OBJECTS AND VARIABLES =======================
    Context context;
    ArrayList<BrandModel> brandList=new ArrayList<>();
    
    //================= CONSTRUCTOR ==================================
    public BrandRecyclerAdapter(Context context, ArrayList<BrandModel> brandList) {
        this.context = context;
        this.brandList = brandList;
    }

    //================= OVERRIDE METHOD FOR ON CREATE VIEW HOLDER ===============
    @NonNull
    @Override
    public BrandRecyclerAdapter.BrandRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.brand_view_holder,parent,false);
        return new BrandRecyclerViewHolder(view);
    }
    //================= OVERRIDE METHOD FOR DATA BINDING ==========================
    @Override
    public void onBindViewHolder(@NonNull BrandRecyclerAdapter.BrandRecyclerViewHolder holder, int position) {
        holder.brandID=brandList.get(position).getBRAND_ID();
        holder.brandTag=brandList.get(position).getTAGS();
        holder.brandName.setText(brandList.get(position).getBRAND_NAME());
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference photoReference= storageReference.child("img_brands/"+brandList.get(position).getBRAND_IMAGE());

        final long ONE_MEGABYTE = 1024*1024;
        photoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                holder.brandImage.setImageBitmap(bmp);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
//                Toast.makeText(context.getApplicationContext(), "No Such file or Path found!!", Toast.LENGTH_LONG).show();
            }
        });
    }

    //================== OVERRIDE METHOD FOR TOTAL ITEM COUNTS =====================
    @Override
    public int getItemCount() {
        return brandList.size();
    }

    public class BrandRecyclerViewHolder extends RecyclerView.ViewHolder {
        //======================= CREATING OBJECT FOR BRAND VIEW HOLDER ======================
        ImageView brandImage;
        TextView brandName;
        String brandID;
        String brandTag;
        public BrandRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            //===================================== ATTATCHING VIEW COMPONENTS WITH OBJECTS ====================
            brandImage=(ImageView) itemView.findViewById(R.id.iv_brand_image);
            brandName=(TextView) itemView.findViewById(R.id.tv_brand_name);
            brandImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent itemListIntent=new Intent(context,ItemListActivity.class);
                    itemListIntent.putExtra("option","1");
                    itemListIntent.putExtra("brandName",brandName.getText());
                    itemListIntent.putExtra("brandID",brandID);
                    itemListIntent.putExtra("brandTag",brandTag);
                    context.startActivity(itemListIntent);
                }
            });
        }
    }
}
