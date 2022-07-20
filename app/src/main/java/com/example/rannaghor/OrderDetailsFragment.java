package com.example.rannaghor;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class OrderDetailsFragment extends Fragment {

    TextView nORDER_ID,nGRAND_TOTAL,nEST_TINE,nRESTAUENT_NAME,nDELIVERY_ADDRESS,nSEE_BILL;
    ImageView isOrderAccepted,isPreparingFood,isDeliveryAgentAssign,isOutForDelivery,isOrderDelivered;
    LinearLayout deliveryPersonLayout;
    TextView deliveryPersonName,deliveryPersonMob;
    DatabaseReference DBreference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_order_details, container, false);
        // ATTACH VIEW COMPONENTS WITH FRAGMENT
        nORDER_ID=view.findViewById(R.id.tv_order_id);
        nGRAND_TOTAL=view.findViewById(R.id.tv_grand_total);
        nEST_TINE=view.findViewById(R.id.tv_time);
        nRESTAUENT_NAME=view.findViewById(R.id.tv_order_from);
        nDELIVERY_ADDRESS=view.findViewById(R.id.tv_delivery_address);
        nSEE_BILL=(TextView)view.findViewById(R.id.tv_see_bill);

        isOrderAccepted=(ImageView) view.findViewById(R.id.iv_isorderaccepted);
        isPreparingFood=(ImageView)view.findViewById(R.id.iv_ispreparingfood);
        isDeliveryAgentAssign=(ImageView)view.findViewById(R.id.iv_is_delivery_agent_assigned);
        isOutForDelivery=(ImageView)view.findViewById(R.id.is_out_for_delivery);
        isOrderDelivered=(ImageView)view.findViewById(R.id.iv_is_order_delivered);

        deliveryPersonLayout=(LinearLayout)view.findViewById(R.id.ll_delivery_person_layout);
        deliveryPersonName=(TextView)view.findViewById(R.id.tv_delivery_agent_name);
        deliveryPersonMob=(TextView)view.findViewById(R.id.tv_delivery_agent_mobile);

        // SET ONCLICK LISTENER IN SEE BILL BUTTON
        nSEE_BILL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nBILL_DETAILS_INTENT=new Intent(getActivity(),BillingActivity.class);
                nBILL_DETAILS_INTENT.putExtra("ORDER_ID",nORDER_ID.getText());
                nBILL_DETAILS_INTENT.putExtra("ORDER_FROM",nRESTAUENT_NAME.getText());
                startActivity(nBILL_DETAILS_INTENT);
            }
        });


        // DATABASE REFERENCE FOR ITEM ORDER
        DBreference= FirebaseDatabase.getInstance().getReference("item_orders");
        DBreference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    DBreference.child(data.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if(data.child("user_ID").getValue().toString().equals(FirebaseAuth.getInstance().getUid())){
                                nORDER_ID.setText(data.child("order_ID").getValue().toString());
                                nGRAND_TOTAL.setText("₹ "+data.child("grand_TOTAL").getValue().toString());
                                nEST_TINE.setText("30 "+"min");
                                nRESTAUENT_NAME.setText(data.child("restaurent_NAME").getValue().toString());
                                nDELIVERY_ADDRESS.setText(data.child("delivery_ADDRESS").getValue().toString());
                                if(data.child("order_STATUS").getValue().toString().equals("Order Placed")){
                                    isOrderAccepted.setVisibility(View.INVISIBLE);
                                    isPreparingFood.setVisibility(View.INVISIBLE);
                                    isDeliveryAgentAssign.setVisibility(View.INVISIBLE);
                                    deliveryPersonLayout.setVisibility(View.INVISIBLE);
                                    isOutForDelivery.setVisibility(View.INVISIBLE);
                                    isOrderDelivered.setVisibility(View.INVISIBLE);
                                }
                                if(data.child("order_STATUS").getValue().toString().equals("Order Accepted")){
                                    isOrderAccepted.setVisibility(View.VISIBLE);
                                }
                                if(data.child("order_STATUS").getValue().toString().equals("Preparing Food")){
                                    isOrderAccepted.setVisibility(View.VISIBLE);
                                    isPreparingFood.setVisibility(View.VISIBLE);
                                }
                                if(data.child("order_STATUS").getValue().toString().equals("Delivery Agent Assigned")){
                                    isOrderAccepted.setVisibility(View.VISIBLE);
                                    isPreparingFood.setVisibility(View.VISIBLE);
                                    isDeliveryAgentAssign.setVisibility(View.VISIBLE);
                                    deliveryPersonLayout.setVisibility(View.VISIBLE);
                                }
                                if(data.child("order_STATUS").getValue().toString().equals("Out For Delivery")){
                                    isOrderAccepted.setVisibility(View.VISIBLE);
                                    isPreparingFood.setVisibility(View.VISIBLE);
                                    isDeliveryAgentAssign.setVisibility(View.VISIBLE);
                                    deliveryPersonLayout.setVisibility(View.VISIBLE);
                                    isOutForDelivery.setVisibility(View.VISIBLE);
                                }
                                if(data.child("order_STATUS").getValue().toString().equals("Order Delivered")){
                                    isOrderAccepted.setVisibility(View.VISIBLE);
                                    isPreparingFood.setVisibility(View.VISIBLE);
                                    isDeliveryAgentAssign.setVisibility(View.VISIBLE);
                                    deliveryPersonLayout.setVisibility(View.VISIBLE);
                                    isOutForDelivery.setVisibility(View.VISIBLE);
                                    isOrderDelivered.setVisibility(View.VISIBLE);
                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }
}