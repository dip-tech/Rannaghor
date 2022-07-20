package com.example.rannaghor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView mainNavigation;
    Fragment selectedFragment=null,nHOME_FRAGMENT,nSEARCH_FRAGMENT,nCART_FRAGMENT,nORDER_DETAILS_FRAGMENT,nPROFILE_FRAGMENT;
    DatabaseReference DBreference;
    Boolean is_ORDER_PLACED=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainNavigation=(BottomNavigationView) findViewById(R.id.main_bottom_navigation);
        mainNavigation.setItemIconTintList(null);
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.white));
//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
//        }
        // ======================== LOAD HOME FRAGMENT AS DEFAULT FRAGMENT ====================
        nHOME_FRAGMENT=new HomeFragment();
        // ======================== LOAD SEARCH FRAGMENT AS DEFAULT FRAGMENT ====================
        nSEARCH_FRAGMENT=new SearchFragment();
        // ======================== LOAD CART FRAGMENT AS DEFAULT FRAGMENT ====================
        nCART_FRAGMENT=new CartFragment();
        // ======================== LOAD ORDER DETAILS FRAGMENT AS DEFAULT FRAGMENT ====================
        nORDER_DETAILS_FRAGMENT=new OrderDetailsFragment();
        // ======================== LOAD PROFILE FRAGMENT AS DEFAULT FRAGMENT ====================
        nPROFILE_FRAGMENT=new ProfileFragment();
        //======================== CHECK YOU ARE CONNECTED TO INTERNET OR NOT ===================
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network

            // ===================== SET HOME FRAGMENT TO MAIN ACTIVITY ==================
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_container,nHOME_FRAGMENT).commit();

            DBreference=FirebaseDatabase.getInstance().getReference("item_orders");
            DBreference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot data: snapshot.getChildren()){
                        DBreference.child(data.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(data.child("user_ID").getValue().toString().equals(FirebaseAuth.getInstance().getUid())){
                                    is_ORDER_PLACED=true;
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
//                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_container,selectedFragment).commit();
                }
            });

        }
        else{
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_container,new OfflineFragment()).commit();
        }

        mainNavigation.setOnNavigationItemSelectedListener(navListener);
    }
    BottomNavigationView.OnNavigationItemSelectedListener navListener=new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
            switch(item.getItemId()){
                case R.id.nav_home:
                    //======================== CHECK YOU ARE CONNECTED TO INTERNET OR NOT ===================
                    ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                    if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                        //we are connected to a network
                        selectedFragment=nHOME_FRAGMENT;

                    }
                    else{
                        selectedFragment=new OfflineFragment();
                    }

                    break;
                case R.id.nav_search:
                    //======================== CHECK YOU ARE CONNECTED TO INTERNET OR NOT ===================
                    connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                    if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                        //we are connected to a network
                        selectedFragment=nSEARCH_FRAGMENT;
                    }
                    else{
                        selectedFragment=new OfflineFragment();
                    }
                    break;
                case R.id.nav_cart:
                    //======================== CHECK YOU ARE CONNECTED TO INTERNET OR NOT ===================
                    connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                    //we are connected to a network
                    if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

                           if(is_ORDER_PLACED){
                               selectedFragment=nORDER_DETAILS_FRAGMENT;
                           }else{
                               selectedFragment=nCART_FRAGMENT;
                           }

                       }
                    else{
                        selectedFragment=new OfflineFragment();
                    }
                    break;
                case R.id.nav_profile:
                    //======================== CHECK YOU ARE CONNECTED TO INTERNET OR NOT ===================
                    connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                    if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                        //we are connected to a network
                        selectedFragment=nPROFILE_FRAGMENT;
                    }
                    else{
                        selectedFragment=new OfflineFragment();
                    }
                    break;
                default:
                    selectedFragment=nHOME_FRAGMENT;
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_container,selectedFragment).commit();
            return true;
        }
    };
}