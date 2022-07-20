package com.example.rannaghor;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class ProfileFragment extends Fragment {
    // ====================== OBJECT FOR BUTTON,EDITTEXT AND OTHER ========================
    EditText userFullName,userEmailAddress,userPhoneNumber;
    TextView addAddress;
    Button logout;
    FirebaseAuth firebaseAuth;
    DatabaseReference firebaseDatabase;
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        // =================== CREATING VIEW FOR PROFILE FRAGMENT ========================
        View view=inflater.inflate(R.layout.fragment_profile,container,false);
        //===================== SET STATUS BAR TEXT AND BACKGROUND COLOR =================
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(this.getActivity(),R.color.black));
        //===================== ATTATCHED VIEW COMPONENTS WITH ACTIVITY ====================
        logout=(Button)view.findViewById(R.id.btn_logout);
        userFullName=(EditText)view.findViewById(R.id.et_user_name);
        userEmailAddress=(EditText)view.findViewById(R.id.et_user_email);
        userPhoneNumber=(EditText)view.findViewById(R.id.et_userphone);
        addAddress=(TextView)view.findViewById(R.id.tv_add_address);
        // ===================== RETRIVE USER DATA FROM FIREBASE ============================
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance().getReference("user_details");
        firebaseDatabase.child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userFullName.setText(snapshot.child("first_NAME").getValue()+" "+snapshot.child("last_NAME").getValue());
                userEmailAddress.setText(snapshot.child("email").getValue().toString());
                userPhoneNumber.setText(snapshot.child("phone").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });
        // ===================== SET ONCLICK LISTENER FOR LOGOUT BUTTON =====================
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                getActivity().startActivity(new Intent(getActivity(),LoginAndRegistrationActivity.class));
                getActivity().finish();
            }
        });

        return view;
    }
}
