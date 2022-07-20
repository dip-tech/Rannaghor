package com.example.rannaghor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    // ======================== DECLEARING VARIABLE & OBJECTS ========================
    RecyclerView itemCategoryRecyclerView,brandRecyclerView,restaurantRecyclerView,nearbyRestaurantRecyclerView;
    ArrayList<ItemCategoryModel> itemCategoryArrayList=new ArrayList<ItemCategoryModel>();
    ArrayList<BrandModel>brandArrayList=new ArrayList<>();
    ArrayList<RestaurentModel>restaurentArrayList=new ArrayList<>();
    ArrayList<RestaurentModel>nearbyRestaurantArrayList=new ArrayList<>();
    DatabaseReference DBreference;
    ItemRecyclerAdapter itemAdapter;
    BrandRecyclerAdapter brandAdapter;
    RestaurentRecyclerAdapter restaurantAdapter;
    NearbyRestaurentRecyclerAdapter nearbyRestaurentAdapter;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        // ==================== CREATING VIEW FOR HOME FRAGMENT =======================
        View view=inflater.inflate(R.layout.fragment_home,container,false);

        // ==================== SET COLOR FOR STATUS BAR ==============================
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(this.getActivity(),R.color.white));

        //===================== ATTATCHEING COMPONENTS ===============================
        itemCategoryRecyclerView=(RecyclerView) view.findViewById(R.id.rv_item);
        brandRecyclerView=(RecyclerView)view.findViewById(R.id.rv_brands);
        restaurantRecyclerView=(RecyclerView)view.findViewById(R.id.rv_top_restaurant);
        nearbyRestaurantRecyclerView=(RecyclerView)view.findViewById(R.id.rv_nearby_restaurents);


        //===================== GET ITEMS CATEGORIES FROM FIREBASE DATABASE =====================
        DBreference= FirebaseDatabase.getInstance().getReference("item_categories");
        DBreference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot data : snapshot.getChildren()){
                    DBreference.child(data.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            itemCategoryArrayList.add(new ItemCategoryModel(data.getKey(),data.child("item_IMG").getValue().toString(),
                                    data.child("item_NAME").getValue().toString()));
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
        //======================= ON COMPLETE LISTENER FOR ITEM CATEGORY ===========================
        DBreference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task!=null){
                    itemAdapter=new ItemRecyclerAdapter(getContext(),itemCategoryArrayList);
                    itemCategoryRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
                    itemCategoryRecyclerView.setAdapter(itemAdapter);
                }
            }
        });


        //===================== GET DATABASE REFERENCE FOR BRANDs ===================
        DBreference=FirebaseDatabase.getInstance().getReference("brands");
        //===================== GET BRANDS FROM FIREBASE DATABASE ===================
        DBreference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for( DataSnapshot data: snapshot.getChildren()){
                    DBreference.child(data.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            brandArrayList.add(
                                    new BrandModel(data.getKey().toString(),
                                            data.child("brand_IMG").getValue().toString(),
                                            data.child("brand_NAME").getValue().toString(),
                                            data.child("tags").getValue().toString(),
                                            Integer.parseInt(data.child("is_NEAR").getValue().toString()),
                                            Integer.parseInt(data.child("is_TOP").getValue().toString())
                                    )
                            );
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
        //===================== ON COMPLETE LISTENER FOR BRANDs ======================
        DBreference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task!=null){
                    brandAdapter=new BrandRecyclerAdapter(getContext(),brandArrayList);
                    brandRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
                    brandRecyclerView.setAdapter(brandAdapter);
                }
            }
        });

        //===================== GET DATABASE REFERENCE FOR RESTAURANTS ================
        DBreference=FirebaseDatabase.getInstance().getReference("restaurants");
        //==================== GET RESTAURANTS FROM FIREBASE DATABASE =================
        DBreference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for( DataSnapshot data: snapshot.getChildren()){
                    DBreference.child(data.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(Integer.parseInt(data.child("is_TOP").getValue().toString())==1){
                                restaurentArrayList.add(
                                        new RestaurentModel(data.getKey().toString(),
                                                data.child("res_NAME").getValue().toString(),
                                                data.child("res_IMG").getValue().toString(),
                                                data.child("res_TAGS").getValue().toString(),
                                                Integer.parseInt(data.child("is_TOP").getValue().toString()),
                                                Integer.parseInt(data.child("is_NEAR").getValue().toString())

                                        )
                                );
                            }
                            if(Integer.parseInt(data.child("is_NEAR").getValue().toString())==1){
                                nearbyRestaurantArrayList.add(
                                        new RestaurentModel(data.getKey().toString(),
                                                data.child("res_NAME").getValue().toString(),
                                                data.child("res_IMG").getValue().toString(),
                                                data.child("res_TAGS").getValue().toString(),
                                                Integer.parseInt(data.child("is_TOP").getValue().toString()),
                                                Integer.parseInt(data.child("is_NEAR").getValue().toString())

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

        // ==================== ON COMPLETE LISTENER FOR RESTAURENTS ==================
        DBreference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task!=null){
                    restaurantAdapter=new RestaurentRecyclerAdapter(getContext(),restaurentArrayList);
                    restaurantRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
                    restaurantRecyclerView.setAdapter(restaurantAdapter);

                    nearbyRestaurentAdapter=new NearbyRestaurentRecyclerAdapter(getContext(),nearbyRestaurantArrayList);
                    nearbyRestaurantRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
                    nearbyRestaurantRecyclerView.setAdapter(nearbyRestaurentAdapter);

                }
            }
        });

        return view;
    }
}
