package com.example.rannaghor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ItemListActivity extends AppCompatActivity {
    //================================ DECLARING OBJECTS AND VARIABLES ========================
    Intent thisIntent;
    Toolbar toolbar;
    RecyclerView recyclerViewItemList;
    TextView textViewBrand;
    TextView textViewTag;
    TextView textViewCartItemCounter;
    FloatingActionButton fbCART;
    String brandID, itemCatID;
    DatabaseReference DBreference;
    ArrayList<ItemModel> itemModelArrayList = new ArrayList<>();
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        // ==================== SET COLOR FOR STATUS BAR ==============================
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        //===================== ATTACHING VIEWS WITH THE OBJECTS ======================
        toolbar = (Toolbar) findViewById(R.id.tb_item_list);
        textViewBrand = (TextView) findViewById(R.id.tv_brand);
        textViewTag = (TextView) findViewById(R.id.tv_brand_tag);
        recyclerViewItemList = (RecyclerView) findViewById(R.id.rv_item_list);
        fbCART=(FloatingActionButton)findViewById(R.id.fb_cart);
        textViewCartItemCounter=(TextView)findViewById(R.id.tv_cart_item_counter);
        // ==================== GET THE INTENT =================================
        thisIntent = getIntent();
        //===================== SET TOOLBAR AS DEFAULT TOOLBAR =================
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //====================== HIDE FLOATING ACTION BUTTON BY DEFAULT =============
        fbCART.setVisibility(View.GONE);
        textViewCartItemCounter.setVisibility(View.GONE);
        //===================== IF OPTION IS 1 ================================
        if (thisIntent.getStringExtra("option").equals("1")) {
            getItemByBrand();
        } else if (thisIntent.getStringExtra("option").equals("2")) {
            getItemByRestaurant();
        } else if (thisIntent.getStringExtra("option").equals("3")) {
            getItemByBrandAndItemCategory();
        }
        //======================== SET ONCLICK LISTENER ON FLOATING ACTION BUTTON ================
        fbCART.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cartIntent=new Intent(getApplicationContext(),CartActivity.class);
                startActivity(cartIntent);
            }
        });

    }


    //======================== METHOD FOR GET ITEMS BY BRAND AND ITEM CATEGORY ============================
    private void getItemByBrandAndItemCategory() {
        itemModelArrayList.clear();
        //===================== SET TITLE FOR ITEM RESTAURANT ======================
        textViewBrand.setText(thisIntent.getStringExtra("restaurantName"));
        //===================== SET RESTAURANT ID ==================================
        brandID = thisIntent.getStringExtra("restaurantID").toString();
        //===================== SET ITEM CATEGORY ID ==============================
        itemCatID = thisIntent.getStringExtra("itemCatID");
        //===================== SET RESTAURANT TAG ===========================
        textViewTag.setText(thisIntent.getStringExtra("restaurantTag"));
        //====================== GET ITEM BY RESTAURANT ===========================
        DBreference = FirebaseDatabase.getInstance().getReference();
        if (brandID.startsWith("B")) {
            //======================== IF THE ITEMS ARE FROM BRAND =========================================
            DBreference.child("items").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        DBreference.child(data.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                //====================== CHECK BRAND =========================
                                if (data.child("item_BRAND").getValue().toString() != null && data.child("item_BRAND").getValue().toString().equals(brandID) && data.child("item_CATEGORY").getValue().toString().equals(itemCatID)) {
                                    itemModelArrayList.add(new ItemModel(
                                            data.getKey(),
                                            data.child("item_NAME").getValue().toString(),
                                            data.child("item_IMG").getValue().toString(),
                                            data.child("item_PRICE").getValue().toString(),
                                            data.child("item_CATEGORY").getValue().toString(),
                                            data.child("item_BRAND").getValue().toString(),
                                            data.child("item_RESTAURANT").getValue().toString(),
                                            (String) data.child("item_TAGS").getValue()
                                    ));
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
            DBreference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    int i;
                    if (task != null) {
                        for (i = 0; i < itemModelArrayList.size(); i++) {
                            int finalI = i;
                            DBreference.child("item_categories").child(itemModelArrayList.get(i).getITEM_CATEGORY()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    itemModelArrayList.get(finalI).setITEM_CATEGORY(snapshot.child("item_NAME").getValue().toString());
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                        DBreference.child("item_categories").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (task != null) {
                                    recyclerViewItemList.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
                                    recyclerViewItemList.setAdapter(new ItemListRecyclerAdapter(ItemListActivity.this, itemModelArrayList,getSupportFragmentManager()));
                                }
                            }
                        });

                    }
                }
            });
        } else {
            //============================== IF THE ITEM ARE FROM RESTAURANT ===============================================
            DBreference.child("items").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        DBreference.child(data.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                //====================== CHECK BRAND =========================
                                if (data.child("item_RESTAURANT").getValue().toString() != null && data.child("item_RESTAURANT").getValue().toString().equals(brandID) && data.child("item_CATEGORY").getValue().toString().equals(itemCatID)) {
                                    itemModelArrayList.add(new ItemModel(
                                            data.getKey(),
                                            data.child("item_NAME").getValue().toString(),
                                            data.child("item_IMG").getValue().toString(),
                                            data.child("item_PRICE").getValue().toString(),
                                            data.child("item_CATEGORY").getValue().toString(),
                                            data.child("item_BRAND").getValue().toString(),
                                            data.child("item_RESTAURANT").getValue().toString(),
                                            (String) data.child("item_TAGS").getValue()
                                    ));
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
            DBreference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    int i;
                    if (task != null) {
                        for (i = 0; i < itemModelArrayList.size(); i++) {
                            int finalI = i;
                            DBreference.child("item_categories").child(itemModelArrayList.get(i).getITEM_CATEGORY()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    itemModelArrayList.get(finalI).setITEM_CATEGORY(snapshot.child("item_NAME").getValue().toString());
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                        DBreference.child("item_categories").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (task != null) {
                                    recyclerViewItemList.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
                                    recyclerViewItemList.setAdapter(new ItemListRecyclerAdapter(ItemListActivity.this, itemModelArrayList,getSupportFragmentManager()));
                                }
                            }
                        });

                    }
                }
            });
        }


    }

    //======================== GET ITEMS BY RESTAURANT =========================
    private void getItemByRestaurant() {
        itemModelArrayList.clear();
        //===================== SET TITLE FOR ITEM BRAND ======================
        textViewBrand.setText(thisIntent.getStringExtra("restaurantName"));
        //===================== SET RESTAURANT ID ==================================
        brandID = thisIntent.getStringExtra("restaurantID").toString();
        //===================== SET RESTAURANT TAG ===========================
        textViewTag.setText(thisIntent.getStringExtra("restaurantTag"));
        //====================== GET ITEM BY RESTAURANT ===========================
        DBreference = FirebaseDatabase.getInstance().getReference();
        DBreference.child("items").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    DBreference.child(data.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //====================== CHECK BRAND =========================
                            if (data.child("item_RESTAURANT").getValue().toString() != null && data.child("item_RESTAURANT").getValue().toString().equals(brandID)) {
                                itemModelArrayList.add(new ItemModel(
                                        data.getKey(),
                                        data.child("item_NAME").getValue().toString(),
                                        data.child("item_IMG").getValue().toString(),
                                        data.child("item_PRICE").getValue().toString(),
                                        data.child("item_CATEGORY").getValue().toString(),
                                        data.child("item_BRAND").getValue().toString(),
                                        data.child("item_RESTAURANT").getValue().toString(),
                                        (String) data.child("item_TAGS").getValue()
                                ));
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
        DBreference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                int i;
                if (task != null) {
                    for (i = 0; i < itemModelArrayList.size(); i++) {
                        int finalI = i;
                        DBreference.child("item_categories").child(itemModelArrayList.get(i).getITEM_CATEGORY()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                itemModelArrayList.get(finalI).setITEM_CATEGORY(snapshot.child("item_NAME").getValue().toString());
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    DBreference.child("item_categories").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task != null) {
                                recyclerViewItemList.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
                                recyclerViewItemList.setAdapter(new ItemListRecyclerAdapter(ItemListActivity.this, itemModelArrayList,getSupportFragmentManager()));
                            }
                        }
                    });

                }
            }
        });


    }

    //====================== GET ITEMS BY BRAND ===============================
    private void getItemByBrand() {
        itemModelArrayList.clear();
        //===================== SET TITLE FOR ITEM BRAND ======================
        textViewBrand.setText(thisIntent.getStringExtra("brandName"));
        //===================== SET BRAND ID ==================================
        brandID = thisIntent.getStringExtra("brandID").toString();
        //===================== SET BRAND TAG ================================
        textViewTag.setText(thisIntent.getStringExtra("brandTag"));
        //====================== GET ITEM BY BRAND ===========================
        DBreference = FirebaseDatabase.getInstance().getReference();
        DBreference.child("items").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    DBreference.child(data.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //====================== CHECK BRAND =========================
                            if (data.child("item_BRAND").getValue().toString() != null && data.child("item_BRAND").getValue().toString().equals(brandID)) {
                                itemModelArrayList.add(new ItemModel(
                                        data.getKey(),
                                        data.child("item_NAME").getValue().toString(),
                                        data.child("item_IMG").getValue().toString(),
                                        data.child("item_PRICE").getValue().toString(),
                                        data.child("item_CATEGORY").getValue().toString(),
                                        data.child("item_BRAND").getValue().toString(),
                                        data.child("item_RESTAURANT").getValue().toString(),
                                        (String) data.child("item_TAGS").getValue()
                                ));
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
        DBreference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                int i;
                if (task != null) {
                    for (i = 0; i < itemModelArrayList.size(); i++) {
                        int finalI = i;
                        DBreference.child("item_categories").child(itemModelArrayList.get(i).getITEM_CATEGORY()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                itemModelArrayList.get(finalI).setITEM_CATEGORY(snapshot.child("item_NAME").getValue().toString());
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    DBreference.child("item_categories").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task != null) {
                                recyclerViewItemList.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
                                recyclerViewItemList.setAdapter(new ItemListRecyclerAdapter(ItemListActivity.this, itemModelArrayList,getSupportFragmentManager()));

                            }
                        }
                    });

                }
            }
        });
    }
    //============================ DOUBLE BACK PRESS TO BACK ===============================
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            DBreference=FirebaseDatabase.getInstance().getReference("item_cart");
            DBreference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot data: snapshot.getChildren()){
                        DBreference.child(data.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.child("user_ID").getValue().toString().equals(FirebaseAuth.getInstance().getUid())){
                                    DBreference.child(data.getKey()).removeValue();
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
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit\nYour cart will be deleted.", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}