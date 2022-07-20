package com.example.rannaghor;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

public class LoginFragment extends Fragment {
    EditText varEmail,varPassword;
    Button btnLogin;
    FirebaseAuth firebaseAuth;
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.login_fragment,container,false);
        // ATTACHED XML COMPONENTS
        varEmail=(EditText) view.findViewById(R.id.edt_username);
        varPassword=(EditText) view.findViewById(R.id.edt_password);
        btnLogin=(Button)view.findViewById(R.id.btn_login); 
        //GET FIREBASE AUTH
        firebaseAuth=FirebaseAuth.getInstance();
        //SET ONCLICK LISTENER FOR LOGIN BUTTON
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoLogin();
            }
        });
        return view ;
    }

    private void DoLogin() {
        firebaseAuth.signInWithEmailAndPassword(varEmail.getText().toString(), varPassword.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            if(user!=null){
                                startActivity(new Intent(getActivity(),MainActivity.class));
                                getActivity().finish();
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getActivity(), "Login failed: "+task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
