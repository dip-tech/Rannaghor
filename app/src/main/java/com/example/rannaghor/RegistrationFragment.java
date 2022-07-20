package com.example.rannaghor;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.Executor;

public class RegistrationFragment extends Fragment {
    Button btnUserReg;
    EditText etUserFirstName, etUserMiddleName, etUserLastName, etUserEmail, etUserPhone, etUserPassword, etUserConfirmPassword;
    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;
    AlertDialog nDialog;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.registration_fragment, container, false);

        //ATTATCHED COMPONENTS WITH FRAGMENTS
        btnUserReg = (Button) view.findViewById(R.id.btn_registration);
        etUserFirstName = (EditText) view.findViewById(R.id.et_user_first_name);
        etUserLastName = (EditText) view.findViewById(R.id.et_user_last_name);
        etUserEmail = (EditText) view.findViewById(R.id.et_user_email);
        etUserPhone = (EditText) view.findViewById(R.id.et_user_phone);
        etUserPassword = (EditText) view.findViewById(R.id.et_user_password);
        etUserConfirmPassword = (EditText) view.findViewById(R.id.et_user_conf_password);
        //GET FIREBASE DATABASR
        database = FirebaseDatabase.getInstance();
        //GET FIREBASE AUTHENTICATION
        firebaseAuth = FirebaseAuth.getInstance();
        //ONCLICK LISTENER FOR USER REGISTRATION BUTTON
        btnUserReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etUserFirstName.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please Enter First Name", Toast.LENGTH_LONG).show();
                } else {
                    if (etUserLastName.getText().toString().equals("")) {
                        Toast.makeText(getActivity(), "Please Enter Last Name", Toast.LENGTH_LONG).show();
                    } else {
                        if (etUserEmail.getText().toString().equals("")) {
                            Toast.makeText(getActivity(), "Please Enter Email", Toast.LENGTH_LONG).show();
                        } else {
                            if (etUserPhone.getText().toString().equals("")) {
                                Toast.makeText(getActivity(), "Please Enter Phone", Toast.LENGTH_LONG).show();
                            } else {
                                if (etUserPassword.getText().toString().equals("")) {
                                    Toast.makeText(getActivity(), "Please Enter Password", Toast.LENGTH_LONG).show();
                                } else {
                                    if (etUserConfirmPassword.getText().toString().equals("")) {
                                        Toast.makeText(getActivity(), "Please Enter Confrim Password", Toast.LENGTH_LONG).show();
                                    } else {
                                        if (etUserPassword.getText().toString().equals(etUserConfirmPassword.getText().toString())) {
                                            //CREATING USER INSTANCE
                                            User USER = new User(etUserFirstName.getText().toString(), etUserLastName.getText().toString(),
                                                    etUserEmail.getText().toString(), etUserPhone.getText().toString());
                                            //FUNCTION FOR USER REGISTRATION
                                            UserSignup(USER);
                                            //CLEARING DATA AFTER REGISTRATION
                                            etUserFirstName.setText("");
                                            etUserLastName.setText("");
                                            etUserEmail.setText("");
                                            etUserPhone.setText("");
                                            etUserPassword.setText("");
                                            etUserConfirmPassword.setText("");
                                            nDialog = new AlertDialog.Builder(getActivity())
                                                    .setTitle("Message")
                                                    .setMessage("Registration Succesful\nLogin to Continue")
                                                    .setPositiveButton("OK",new DialogInterface.OnClickListener(){
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            nDialog.dismiss();
                                                        }
                                                    })
                                                    .create();
                                            nDialog.show();

                                        } else {
                                            Toast.makeText(getActivity(), "Password Not Matched", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }
        });
        return view;
    }

    private void UserSignup(User U) {
        firebaseAuth.createUserWithEmailAndPassword(etUserEmail.getText().toString(), etUserPassword.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            //ADDING DATA TO user_details
                            DatabaseReference myRef = database.getReference("user_details");
                            myRef.child(user.getUid()).setValue(U);

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getContext(), "Error: " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();


                        }
                    }
                });
    }

    private void updateUI(FirebaseUser o) {
    }
}
