package com.example.rannaghor;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ItemListRecyclerAdapter extends RecyclerView.Adapter<ItemListRecyclerAdapter.ItemRecyclerViewHolder> {
    //================================ DECLARING OBJECTS AND VARIABLES =================================
    Activity context;
    FragmentManager fragmentManager;
    ArrayList<ItemModel> itemList = new ArrayList<>();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference nDatabase;
    //=============================== CONSTRUCTOR ======================================================

    public ItemListRecyclerAdapter(Activity context, ArrayList<ItemModel> itemList, FragmentManager fragmentManager) {
        this.context = context;
        this.itemList = itemList;
        this.fragmentManager = fragmentManager;

    }

    //================================ OVERRIDE METHOD FOR CREATE VIEW HOLDER ==========================
    @NonNull
    @Override
    public ItemListRecyclerAdapter.ItemRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler_view_holder, parent, false);
        return new ItemRecyclerViewHolder(view);
    }

    //============================== OVERRIDE METHOD FOR ON BIND VIEW HOLDER ==========================
    @Override
    public void onBindViewHolder(@NonNull ItemListRecyclerAdapter.ItemRecyclerViewHolder holder, int position) {
        //========================= SET ITEM OBJECT FOR CART ITEM =================================
        holder.cartITEM = itemList.get(position);
        //========================== GET ITEM IMAGE FROM FIREBASE =================================
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference photoReference = storageReference.child("img_products/" + itemList.get(position).getITEM_IMG());

        final long ONE_MEGABYTE = 1024 * 1024;
        photoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                holder.iv_ITEM_IMAGE.setImageBitmap(bmp);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
//                Toast.makeText(context.getApplicationContext(), "No Such file or Path found!!", Toast.LENGTH_LONG).show();
            }
        });
        //======================== SET ITEM ID ====================================================
        holder.ITEM_ID = itemList.get(position).getITEM_ID();
        //========================= SET ITEM NAME =================================================
        holder.tv_ITEM_NAME.setText(itemList.get(position).getITEM_NAME());
        //========================= SET ITEM CATEGORY =============================================
        holder.tv_ITEM_CATEGORY.setText(itemList.get(position).getITEM_CATEGORY());
        //========================= SET ITEM PRICE ================================================
        holder.tv_ITEM_PRICE.setText("₹ " + itemList.get(position).getITEM_PRICE());
        //========================= SET ITEM TAGS =================================================
        holder.tv_ITEM_TAGS.setText(itemList.get(position).getITEM_TAGS());
    }


    //============================== OVERRIDE METHOD FOR ITEM COUNT ==================================
    @Override
    public int getItemCount() {
        return itemList.size();
    }

    //============================= CLASS ITEM VIEW HOLDER ==========================================
    public class ItemRecyclerViewHolder extends RecyclerView.ViewHolder {
        //================= DECLARING VIEWS AND VARIABLES ==========================================
        TextView tv_ITEM_NAME, tv_ITEM_CATEGORY, tv_ITEM_PRICE, tv_ITEM_TAGS, tv_QTY_DISP;
        ImageView iv_ITEM_IMAGE;
        Button btn_ADD_ITEM, btn_QTY_RM, btn_QTY_ADD;
        String ITEM_ID;
        CardView quantityLayout;
        TextView tvCartItemCounter;
        ItemModel cartITEM;
        String CART_ID;

        public ItemRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            //========================= ATTACHING VIEWS WITH THE COMPONENTS ========================
            tv_ITEM_NAME = (TextView) itemView.findViewById(R.id.tv_item_name);
            tv_ITEM_CATEGORY = (TextView) itemView.findViewById(R.id.tv_item_category);
            tv_ITEM_PRICE = (TextView) itemView.findViewById(R.id.tv_item_price);
            tv_ITEM_TAGS = (TextView) itemView.findViewById(R.id.tv_item_tags);
            iv_ITEM_IMAGE = (ImageView) itemView.findViewById(R.id.iv_item_image);
            btn_ADD_ITEM = (Button) itemView.findViewById(R.id.btn_add_item);
            quantityLayout = (CardView) itemView.findViewById(R.id.cv_quantity_layout);
            btn_QTY_ADD = (Button) itemView.findViewById(R.id.btn_qty_add);
            btn_QTY_RM = (Button) itemView.findViewById(R.id.btn_qty_rm);
            tv_QTY_DISP = (TextView) itemView.findViewById(R.id.tv_qty_disp);

            //==================== REFERENCE TEXT VIEW FROM ITEM LIST ACTIVITY ======================
            tvCartItemCounter = context.findViewById(R.id.tv_cart_item_counter);
            //==================== VISIBILITY OFF FOR QUANTITY LAYOUT ================================
            quantityLayout.setVisibility(View.GONE);

            //======================= SET ONCLICK LISTENER ON ADD TO CART BUTTON =====================
            btn_ADD_ITEM.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btn_ADD_ITEM.setVisibility(View.GONE);
                    quantityLayout.setVisibility(View.VISIBLE);
                    context.findViewById(R.id.fb_cart).setVisibility(View.VISIBLE);
                    context.findViewById(R.id.tv_cart_item_counter).setVisibility(View.VISIBLE);
                    tvCartItemCounter.setText(new String(Integer.parseInt(tvCartItemCounter.getText().toString()) + 1 + ""));
                    //=========================== SET CART ID USING DATE & TIME =====================
                    String currentDate = new SimpleDateFormat("ddMMyyyy", Locale.getDefault()).format(new Date());
                    String currentTime = new SimpleDateFormat("HHmmssms", Locale.getDefault()).format(new Date());
                    CART_ID = currentDate + currentTime;
                    //=========================== CHECK IF THE ITEM IS FROM RESTAURANT OR BRAND ============================
                    if (!cartITEM.getITEM_RESTAURANT().equals("")) {
                        //========================== GET RESTAURANT NAME =================================
                        final String[] nameRESTAURANT = new String[1];
                        nDatabase = FirebaseDatabase.getInstance().getReference("restaurants");
                        nDatabase.child(cartITEM.getITEM_RESTAURANT()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                nameRESTAURANT[0] = snapshot.child("res_NAME").getValue().toString();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        nDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                //=========================== ADD CART ITEM TO THE FIREBASE ======================
                                nDatabase = FirebaseDatabase.getInstance().getReference("item_cart");
                                nDatabase.child(CART_ID).setValue(
                                        new CartItemModel(CART_ID,mAuth.getCurrentUser().getUid(),
                                                cartITEM.getITEM_ID(),
                                                cartITEM.getITEM_RESTAURANT(),
                                                nameRESTAURANT[0].toString(),
                                                cartITEM.getITEM_IMG(),
                                                cartITEM.getITEM_NAME(),
                                                cartITEM.getITEM_PRICE(),
                                                tv_QTY_DISP.getText().toString())
                                );
                            }
                        });
                    } else {
                        //=========================== GET BRAND NAME ====================================
                        final String[] nameBRAND = new String[1];
                        nDatabase = FirebaseDatabase.getInstance().getReference("brands");
                        nDatabase.child(cartITEM.getITEM_BRAND()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                nameBRAND[0] = snapshot.child("brand_NAME").getValue().toString();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        nDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                //=========================== ADD CART ITEM TO THE FIREBASE ======================
                                nDatabase = FirebaseDatabase.getInstance().getReference("item_cart");
                                nDatabase.child(CART_ID).setValue(
                                        new CartItemModel(CART_ID,mAuth.getCurrentUser().getUid(),
                                                cartITEM.getITEM_ID(),
                                                cartITEM.getITEM_BRAND(),
                                                nameBRAND[0].toString(),
                                                cartITEM.getITEM_IMG(),
                                                cartITEM.getITEM_NAME(),
                                                cartITEM.getITEM_PRICE(),
                                                tv_QTY_DISP.getText().toString())
                                );
                            }
                        });
                    }


                }
            });

            //====================== SET ONCLICK LISTENER ON ITEM REMOVE BUTTON ============================
            btn_QTY_RM.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Integer.parseInt(tv_QTY_DISP.getText().toString()) == 1) {
                        btn_ADD_ITEM.setVisibility(View.VISIBLE);
                        quantityLayout.setVisibility(View.GONE);
                        if (Integer.parseInt(tvCartItemCounter.getText().toString()) == 1) {
                            context.findViewById(R.id.fb_cart).setVisibility(View.GONE);
                            context.findViewById(R.id.tv_cart_item_counter).setVisibility(View.GONE);
                        }
                        tvCartItemCounter.setText(new String(Integer.parseInt(tvCartItemCounter.getText().toString()) - 1 + ""));
                        nDatabase.child(CART_ID).removeValue();
                    } else {
                        tv_QTY_DISP.setText(new String(Integer.parseInt(tv_QTY_DISP.getText().toString()) - 1 + ""));
                        tvCartItemCounter.setText(new String(Integer.parseInt(tvCartItemCounter.getText().toString()) - 1 + ""));
                        nDatabase.child(CART_ID).child("item_QTY").setValue(tv_QTY_DISP.getText());
                    }
                }
            });

            //====================== SET ONCLICK LISTENER ON ITEM ADD BUTTON =========================
            btn_QTY_ADD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Integer.parseInt(tv_QTY_DISP.getText().toString()) < 3) {
                        tv_QTY_DISP.setText(new String(Integer.parseInt(tv_QTY_DISP.getText().toString()) + 1 + ""));
                        tvCartItemCounter.setText(new String(Integer.parseInt(tvCartItemCounter.getText().toString()) + 1 + ""));
                        nDatabase.child(CART_ID).child("item_QTY").setValue(tv_QTY_DISP.getText());
                    }
                }
            });


        }

    }
}
