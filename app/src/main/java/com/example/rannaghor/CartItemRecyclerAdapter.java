package com.example.rannaghor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CartItemRecyclerAdapter extends RecyclerView.Adapter<CartItemRecyclerAdapter.CartItemRecyclerViewHolder> {
    //======================== DECLARING VARIABLES AND OBJECTS ===================
    Activity nActivity;
    ArrayList<CartItemModel> nCartItemList;

    //======================== CONSTRUCTOR ===========================

    public CartItemRecyclerAdapter(Activity nActivity, ArrayList<CartItemModel> nCartItemList) {
        this.nActivity = nActivity;
        this.nCartItemList = nCartItemList;
    }

    //====================== METHOD FOR ON CREATE VIEW HOLDER ===================
    @NonNull
    @Override
    public CartItemRecyclerAdapter.CartItemRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(nActivity).inflate(R.layout.cartitem_recycler_view_holder, parent, false);
        return new CartItemRecyclerViewHolder(view);
    }

    //====================== METHOD FOR ON BIND VIEW HOLDER ========================
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CartItemRecyclerAdapter.CartItemRecyclerViewHolder holder, int position) {
        double nTOTAL;
        double nGRAND_TOTAL;
        double nPRICE;
        nPRICE=Double.parseDouble(nCartItemList.get(position).getITEM_PRICE())*Double.parseDouble(nCartItemList.get(position).getITEM_QTY());
        holder.nCART_ITEM_NAME.setText(nCartItemList.get(position).getITEM_NAME());
        holder.nCART_ITEM_PRICE.setText(nPRICE+"");
        holder.nCART_ITEM_QTY.setText(nCartItemList.get(position).getITEM_QTY());
        nTOTAL=Double.parseDouble(holder.nTEXTVIEW_TOTAL.getText().toString())+(Double.parseDouble(nCartItemList.get(position).getITEM_PRICE())*Double.parseDouble(nCartItemList.get(position).getITEM_QTY()));
        holder.nCART_ID=nCartItemList.get(position).getCART_ID().toString();
        if(position==nCartItemList.size()-1){
            holder.nTEXTVIEW_TOTAL.setText("₹ "+nTOTAL);
            nGRAND_TOTAL=nTOTAL+Double.parseDouble(holder.nTEXT_TAX.getText().toString());
            holder.nTEXT_GRAND_TOTAL.setText("₹ "+nGRAND_TOTAL);
            holder.nTEXT_TAX.setText("₹ "+holder.nTEXT_TAX.getText());
        }
        else {
            holder.nTEXTVIEW_TOTAL.setText(nTOTAL+"");
        }
    }

    //======================= COUNT NUMBER OF DATA ==============================
    @Override
    public int getItemCount() {
        return nCartItemList.size();
    }

    //=============================== RECYCLER VIEW HOLDER CLASS ======================
    public class CartItemRecyclerViewHolder extends RecyclerView.ViewHolder {
        //=================== DECLARING VARIABLES AND OBJECTS =======================
        String nCART_ID;
        TextView nCART_ITEM_NAME, nCART_ITEM_PRICE, nCART_ITEM_QTY, nTEXTVIEW_TOTAL,nTEXT_TAX,nTEXT_GRAND_TOTAL;
        ImageButton nCART_ITEM_QTY_ADD, nCART_ITEM_QTY_RM;
        CardView nITEM_LIST_CARD;
        DatabaseReference nDatabaseReference;

        public CartItemRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            //================== ATTACHING VIEWS WITH THE OBJECTS ==============
            nITEM_LIST_CARD=(CardView)itemView.findViewById(R.id.cv_item);
            nCART_ITEM_NAME = (TextView) itemView.findViewById(R.id.tv_cart_item_name);
            nCART_ITEM_PRICE = (TextView) itemView.findViewById(R.id.tv_cart_item_price);
            nCART_ITEM_QTY = (TextView) itemView.findViewById(R.id.tv_cart_item_qty);
            nCART_ITEM_QTY_ADD = (ImageButton) itemView.findViewById(R.id.ib_cart_qty_add);
            nCART_ITEM_QTY_RM = (ImageButton) itemView.findViewById(R.id.ib_cart_qty_rm);
            //==================== GET REFERENCE FOR PARENT TEXT VIEW ===============
            nTEXTVIEW_TOTAL = (TextView) nActivity.findViewById(R.id.tv_total_price);
            nTEXT_TAX=(TextView)nActivity.findViewById(R.id.tv_tax);
            nTEXT_GRAND_TOTAL=(TextView)nActivity.findViewById(R.id.tv_grand_total);
            nITEM_LIST_CARD.setBackgroundColor(Color.WHITE);

            //===================== ADD ONCLICK LISTENER IN QTY ADD BUTTON ==================
            nCART_ITEM_QTY_ADD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int nCURENT_QTY=Integer.parseInt(nCART_ITEM_QTY.getText().toString());
                    Double nCURENT_PRICE;
                    Double nCURENT_TOTAL_PRICE;
                    Double nGRAND_TOTAL;

                    if(nCURENT_QTY<3){

                        //==================== GET FIREBASE DATABASE ==================
                        nDatabaseReference= FirebaseDatabase.getInstance().getReference("item_cart");
                        Double nSINGLEITEMPRICE=Double.parseDouble(nCART_ITEM_PRICE.getText().toString())/nCURENT_QTY;
                        nCURENT_QTY+=1;
                        //==================== ADD QUANTITY TO DATABASE ===============
                        nDatabaseReference.child(nCART_ID).child("item_QTY").setValue(nCURENT_QTY);
                        nCART_ITEM_QTY.setText(nCURENT_QTY+"");
                        nCURENT_PRICE=Double.parseDouble(nCART_ITEM_PRICE.getText().toString())+nSINGLEITEMPRICE;
                        nCART_ITEM_PRICE.setText(nCURENT_PRICE+"");
                        nCURENT_TOTAL_PRICE=Double.parseDouble(nTEXTVIEW_TOTAL.getText().toString().replace("₹ ",""));
                        nCURENT_TOTAL_PRICE+=nSINGLEITEMPRICE;
                        nTEXTVIEW_TOTAL.setText("₹ "+nCURENT_TOTAL_PRICE);
                        nGRAND_TOTAL=nCURENT_TOTAL_PRICE+Double.parseDouble(nTEXT_TAX.getText().toString().replace("₹ ",""));
                        nTEXT_GRAND_TOTAL.setText("₹ "+nGRAND_TOTAL);
                    }

                }
            });

            //========================= ADD ONCLICK LISTENER IN QTY REMOVE BUTTON ============================
            nCART_ITEM_QTY_RM.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int nCURENT_QTY=Integer.parseInt(nCART_ITEM_QTY.getText().toString());
                    Double nCURENT_PRICE;
                    Double nCURENT_TOTAL_PRICE;
                    Double nGRAND_TOTAL;
                    if(nCURENT_QTY>1){
                        //==================== GET FIREBASE DATABASE ==================
                        nDatabaseReference= FirebaseDatabase.getInstance().getReference("item_cart");
                        Double nSINGLEITEMPRICE=Double.parseDouble(nCART_ITEM_PRICE.getText().toString())/nCURENT_QTY;
                        nCURENT_QTY-=1;
                        //==================== REMOVE QUANTITY FROM DATABASE ===============
                        nDatabaseReference.child(nCART_ID).child("item_QTY").setValue(nCURENT_QTY);

                        nCART_ITEM_QTY.setText(nCURENT_QTY+"");
                        nCURENT_PRICE=Double.parseDouble(nCART_ITEM_PRICE.getText().toString())-nSINGLEITEMPRICE;
                        nCART_ITEM_PRICE.setText(nCURENT_PRICE+"");
                        nCURENT_TOTAL_PRICE=Double.parseDouble(nTEXTVIEW_TOTAL.getText().toString().replace("₹ ",""));
                        nCURENT_TOTAL_PRICE-=nSINGLEITEMPRICE;
                        nTEXTVIEW_TOTAL.setText("₹ "+nCURENT_TOTAL_PRICE);
                        nGRAND_TOTAL=nCURENT_TOTAL_PRICE+Double.parseDouble(nTEXT_TAX.getText().toString().replace("₹ ",""));
                        nTEXT_GRAND_TOTAL.setText("₹ "+nGRAND_TOTAL);
                    }
                }
            });
        }
    }
}
