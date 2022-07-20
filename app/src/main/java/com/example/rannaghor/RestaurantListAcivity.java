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
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class RestaurantListAcivity extends AppCompatActivity {
    //====================== DECLARING OBJECTS AND VARIABLES =========================
    Toolbar customToolbar;
    TextView itemCategoryTextView,itemCategoryLabel;
    RecyclerView restaurantListRecyclerView;
    String itemCategoryID;
    DatabaseReference DBreference;
    ArrayList<String> brandIDList=new ArrayList<>();
    ArrayList<RestaurentModel> restaurantObjectList=new ArrayList<>();
    LinkedHashSet<String>brandIDSet=new LinkedHashSet<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list_acivity);
        // ==================== SET COLOR FOR STATUS BAR ==============================
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.white));

        //===================== ATTACHING VIEW COMPONENTS =============================
        customToolbar=(Toolbar) findViewById(R.id.tb_restaurant_list);
        itemCategoryTextView=(TextView) findViewById(R.id.tv_item_category);
        itemCategoryLabel=(TextView)findViewById(R.id.tv_rest_label);
        restaurantListRecyclerView=(RecyclerView)findViewById(R.id.rv_restaurant_list);
        //===================== SET TOOLBAR AS A DEFAULT TOOLBAR ======================
        setSupportActionBar(customToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //===================== SET ITEM NAME INTO TOOLBAR ===========================
        itemCategoryTextView.setText(getIntent().getStringExtra("itemCatName"));
        itemCategoryID=getIntent().getStringExtra("itemCatID");
        itemCategoryLabel.setText(itemCategoryLabel.getText()+" "+getIntent().getStringExtra("itemCatName"));

        //===================== GET DATABASE REFERENCE FOR ITEMS =====================
        DBreference= FirebaseDatabase.getInstance().getReference("items");
        DBreference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data:snapshot.getChildren()){
                    DBreference.child(data.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(data.child("item_CATEGORY").getValue().equals(itemCategoryID)){
                                if(!data.child("item_BRAND").getValue().toString().equals(""))
                                    brandIDList.add(data.child("item_BRAND").getValue().toString());
                                if(!data.child("item_RESTAURANT").getValue().toString().equals(""))
                                    brandIDList.add(data.child("item_RESTAURANT").getValue().toString());
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

        //====================== ON COMPLETE LISTENER =======================
        DBreference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task!=null){
                    brandIDSet.addAll(brandIDList);
                    getBrandData();
                }
            }
        });
    }
    //=================== FUNCTION FOR GETTING BRAND DATA ============================
    public void getBrandData(){
        //============================== GET ALL THE RESTAURANTS AND BRANDS FROM THE LIST ==============================
        for(String ID: brandIDSet){
            if(ID.startsWith("B")){
                DBreference=FirebaseDatabase.getInstance().getReference("brands");
                DBreference.child(ID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        restaurantObjectList.add(new RestaurentModel(
                                ID,
                                snapshot.child("brand_NAME").getValue().toString(),
                                snapshot.child("brand_IMG").getValue().toString(),
                                snapshot.child("tags").getValue().toString(),
                                Integer.parseInt(snapshot.child("is_TOP").getValue().toString()),
                                Integer.parseInt(snapshot.child("is_NEAR").getValue().toString())
                        ));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            if(ID.startsWith("R")){
                DBreference=FirebaseDatabase.getInstance().getReference("restaurants");
                DBreference.child(ID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        restaurantObjectList.add(new RestaurentModel(
                                ID,
                                snapshot.child("res_NAME").getValue().toString(),
                                snapshot.child("res_IMG").getValue().toString(),
                                snapshot.child("res_TAGS").getValue().toString(),
                                Integer.parseInt(snapshot.child("is_TOP").getValue().toString()),
                                Integer.parseInt(snapshot.child("is_NEAR").getValue().toString())
                        ));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }
        DBreference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task!=null){
                    restaurantListRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
                    restaurantListRecyclerView.setAdapter(new RestarantListRecyclerAdapter(getApplicationContext(),restaurantObjectList,itemCategoryID));
                }
            }
        });

    }
}