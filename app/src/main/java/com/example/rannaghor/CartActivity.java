package com.example.rannaghor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class CartActivity extends AppCompatActivity {

    //================== DECLARING OBJECTS && VARIABLS ================
    Toolbar nToolbar;
    RecyclerView nCartItemRecyclerView;
    TextView nTOTAL_PRICE,nTAX,nGRAND_TOTAL,nDELIVERY_ADDRESS,nPAYMENT_METHOD;
    Button nbtnPLACE_ORDER;
    ArrayList<CartItemModel>nCART_ITEMS=new ArrayList<>();
    ArrayList<OrderItemModel>nORDER_ITEM_LIST=new ArrayList<>();
    String nORDER_FROM_RESTAURENT_NAME,nORDER_FROM_RESTAURENT_ID;
    DatabaseReference nDatabaseRaference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //======================= SET STATUS BAR COLOR && STATUS BAR THEME ====================
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.white));

        //======================= ATTACH VIEWS WITH THE OBJECTS =========================
        nToolbar=(Toolbar) findViewById(R.id.tb_cart);
        nCartItemRecyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        nTOTAL_PRICE=(TextView)findViewById(R.id.tv_total_price);
        nTAX=(TextView)findViewById(R.id.tv_tax);
        nGRAND_TOTAL=(TextView)findViewById(R.id.tv_grand_total);
        nDELIVERY_ADDRESS=(TextView)findViewById(R.id.tv_delivery_address);
        nPAYMENT_METHOD=(TextView)findViewById(R.id.tv_payment_method);
        nbtnPLACE_ORDER=(Button)findViewById(R.id.btn_placed_order);
        //======================= SET TOOLBAR AS DEFAULT TOOLBAR =========================
        setSupportActionBar(nToolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //======================= GET FIREBASE DATABASE REFERENCE ===================
        nDatabaseRaference= FirebaseDatabase.getInstance().getReference("item_cart");
        nDatabaseRaference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for( DataSnapshot data: snapshot.getChildren()){
                    nDatabaseRaference.child(data.getKey().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (Objects.equals(data.child("user_ID").getValue(), FirebaseAuth.getInstance().getUid())) {
                                nCART_ITEMS.add(
                                        new CartItemModel(
                                                data.child("cart_ID").getValue().toString(),
                                                data.child("user_ID").getValue().toString(),
                                                data.child("item_ID").getValue().toString(),
                                                data.child("restaurant_ID").getValue().toString(),
                                                data.child("restaurant_NAME").getValue().toString(),
                                                data.child("item_IMAGE").getValue().toString(),
                                                data.child("item_NAME").getValue().toString(),
                                                data.child("item_PRICE").getValue().toString(),
                                                data.child("item_QTY").getValue().toString()
                                        )
                                );
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
        //=================== ON COMPLETE LISTENER FOR DATABASE REFERENCE ================
        nDatabaseRaference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task!=null){
                    nCartItemRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false));
                    nCartItemRecyclerView.setAdapter(new CartItemRecyclerAdapter(CartActivity.this,nCART_ITEMS));

                }
            }
        });

        //======================= SET ONCLICK LISTENER ON PLACE ORDER BUTTON ==================
        nbtnPLACE_ORDER.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //================ CREATING ORDER ID =====================
                String nDATETODAY=new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
                String nTIMENOW=new SimpleDateFormat("HHmmssms",Locale.getDefault()).format(new Date());
                String nORDER_ID="OID"+nDATETODAY+nTIMENOW;
                //================ GET CART ITEMS FROM FIREBASE DATABASE =====================
                nDatabaseRaference=FirebaseDatabase.getInstance().getReference("item_cart");
                nDatabaseRaference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot data:snapshot.getChildren()){
                            nDatabaseRaference.child(data.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(data.child("user_ID").getValue().equals(FirebaseAuth.getInstance().getUid())){
                                        nORDER_ITEM_LIST.add(new OrderItemModel(
                                                data.child("item_ID").getValue().toString(),
                                                data.child("item_IMAGE").getValue().toString(),
                                                data.child("item_NAME").getValue().toString(),
                                                data.child("item_PRICE").getValue().toString(),
                                                data.child("item_QTY").getValue().toString()
                                        ));
                                        nORDER_FROM_RESTAURENT_ID=data.child("restaurant_ID").getValue().toString();
                                        nORDER_FROM_RESTAURENT_NAME=data.child("restaurant_NAME").getValue().toString();
                                        //========= DELETE CART ITEMS AFTER ORDER ===================
                                        nDatabaseRaference.child(data.getKey()).removeValue();
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
                //================ SET ONCLICK LISTENER ON DATABASE REFERENCE (CART ITEMS) =======
                nDatabaseRaference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task!=null){
                            //================ GET FIREBASE REFERENCE FOR ITEM_ORDER TABLE ================
                            nDatabaseRaference=FirebaseDatabase.getInstance().getReference("item_orders");
                            nDatabaseRaference.child(nORDER_ID).setValue(new OrderModel(
                                    nORDER_ID,
                                    nORDER_FROM_RESTAURENT_ID,
                                    nORDER_FROM_RESTAURENT_NAME,
                                    nTOTAL_PRICE.getText().toString().replace("₹",""),
                                    nDELIVERY_ADDRESS.getText().toString(),
                                    nGRAND_TOTAL.getText().toString().replace("₹",""),
                                    nTAX.getText().toString().replace("₹",""),
                                    FirebaseAuth.getInstance().getUid().toString(),
                                    new SimpleDateFormat("dd-MM-yy", Locale.getDefault()).format(new Date()),
                                    new SimpleDateFormat("HH:mm",Locale.getDefault()).format(new Date()),
                                    nORDER_ITEM_LIST

                            ));



                            // ====================== START ORDER ACTIVITY ==============
                            Intent nORDER_INTENT=new Intent(getApplicationContext(),OrderActivity.class);
                            startActivity(nORDER_INTENT);
                        }
                    }
                });
            }
        });
    }
}