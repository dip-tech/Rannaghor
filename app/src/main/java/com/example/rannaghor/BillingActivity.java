package com.example.rannaghor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class BillingActivity extends AppCompatActivity {
    TextView nORDER_FROM_TEXT_VIEW,nORDER_TOTAL_AMOUNT_TEXT_VIEW,nORDER_TAX_AMOUNT_TEXT_VIEW,nGRAND_TOTAL_TEXT_VIEW;
    RecyclerView nBILL_ITEM_RECYCLERVIEW;
    String nORDER_ID,nGRAND_TOTAL;
    DatabaseReference DBreferance;
    ArrayList<OrderItemModel> nORDER_ITEM_LIST=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);

        // ==================== SET COLOR FOR STATUS BAR ==============================
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.white));

        //===================== ATTACH VIEW COMPONENTS ===============================
        nORDER_FROM_TEXT_VIEW=(TextView)findViewById(R.id.tv_order_from_restaurent);
        nBILL_ITEM_RECYCLERVIEW=(RecyclerView)findViewById(R.id.bill_item_recyclerview);
        nORDER_TOTAL_AMOUNT_TEXT_VIEW=(TextView)findViewById(R.id.tv_total_amount);
        nORDER_TAX_AMOUNT_TEXT_VIEW=(TextView)findViewById(R.id.tv_tax_amount);
        nGRAND_TOTAL_TEXT_VIEW=(TextView)findViewById(R.id.tv_grand_amount);
        //==================== GET ORDER_ID & ORDER_FROM =============================
        nORDER_ID=getIntent().getStringExtra("ORDER_ID");
        nORDER_FROM_TEXT_VIEW.setText(getIntent().getStringExtra("ORDER_FROM"));

        //==================== GET BILL DETAILS FORM DATABASE =======================
        DBreferance= FirebaseDatabase.getInstance().getReference("item_orders").child(nORDER_ID);
        DBreferance.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nGRAND_TOTAL_TEXT_VIEW.setText("₹ "+snapshot.child("grand_TOTAL").getValue().toString());
                nORDER_TOTAL_AMOUNT_TEXT_VIEW.setText("₹ "+snapshot.child("total_AMOUNT").getValue().toString());
                nORDER_TAX_AMOUNT_TEXT_VIEW.setText("₹ "+snapshot.child("tax_AND_CHARGES").getValue().toString());
                DBreferance.child("order_ITEMS").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot data: snapshot.getChildren()){
                            DBreferance=DBreferance.child("order_ITEMS").child(data.getKey());
                            DBreferance.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    nORDER_ITEM_LIST.add(new OrderItemModel(data.child("ordered_ITEM_ID").getValue().toString(),
                                            data.child("ordered_ITEM_IMAGE").getValue().toString(),
                                            data.child("ordered_ITEM_NAME").getValue().toString(),
                                            data.child("ordered_ITEM_PRICE").getValue().toString(),
                                            data.child("ordered_ITEM_QTY").getValue().toString()));
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                        DBreferance.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if(task!=null){
                                    nBILL_ITEM_RECYCLERVIEW.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
                                    nBILL_ITEM_RECYCLERVIEW.setAdapter(new BillItemRecyclerAdapter(getApplicationContext(),nORDER_ITEM_LIST));

                                }
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}