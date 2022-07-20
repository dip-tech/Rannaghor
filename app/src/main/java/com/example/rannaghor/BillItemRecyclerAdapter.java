package com.example.rannaghor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BillItemRecyclerAdapter extends RecyclerView.Adapter<BillItemRecyclerAdapter.BillItemRecyclerViewHolder> {
    Context nContext;
    ArrayList<OrderItemModel> nItemList;

    public BillItemRecyclerAdapter(Context nContext, ArrayList<OrderItemModel> nItemList) {
        this.nContext = nContext;
        this.nItemList = nItemList;
    }

    @NonNull
    @Override
    public BillItemRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(nContext).inflate(R.layout.bill_item_view_holder,parent,false);
        return new BillItemRecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BillItemRecyclerViewHolder holder, int position) {
        Double nPRICE =Double.parseDouble(nItemList.get(position).getORDERED_ITEM_QTY())*Double.parseDouble(nItemList.get(position).getORDERED_ITEM_PRICE());
        holder.nBILL_ITEM_NAME.setText(nItemList.get(position).getORDERED_ITEM_NAME());
        holder.nBILL_ITEM_QTY.setText(nItemList.get(position).getORDERED_ITEM_QTY());
        holder.nBILL_ITEM_PRICE.setText("₹ "+nPRICE);

    }

    @Override
    public int getItemCount() {
        return nItemList.size();
    }

    public class BillItemRecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView nBILL_ITEM_NAME,nBILL_ITEM_QTY,nBILL_ITEM_PRICE;
        public BillItemRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            //============================ ATTACH VIEW HOLDER COMPONENTS ====================
            nBILL_ITEM_NAME=(TextView) itemView.findViewById(R.id.tv_bill_item_name);
            nBILL_ITEM_QTY=(TextView) itemView.findViewById(R.id.tv_bill_item_qty);
            nBILL_ITEM_PRICE=(TextView) itemView.findViewById(R.id.tv_bill_item_price);
        }
    }
}
