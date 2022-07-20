package com.example.rannaghor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginAndRegistrationActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;
    Button btnLogin,btnReg;
    ImageButton ibGoogleSigning;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_and_registration);
        //SET NAVBAR COLOR BLACK
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
        }

        // ATTACH COMPONENTS WITH ACTIVITY
        btnLogin=(Button)findViewById(R.id.btn_login_toggle);
        btnReg=(Button)findViewById(R.id.btn_reg_toggle);
        ibGoogleSigning=(ImageButton)findViewById(R.id.btn_google_login);
        //GET FIREBASE AUTHENTICATION
        mAuth = FirebaseAuth.getInstance();
        //REDIRECT TO MAIN ACTIVITY IF LOGIN COMPLETE
        try{
            FirebaseUser CurrentUser=mAuth.getCurrentUser();
            if(CurrentUser!=null){
                startActivity(new Intent(LoginAndRegistrationActivity.this,MainActivity.class));
                finish();
            }
        }catch (Exception e){
            Toast.makeText(this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        // CHANGE FRAGMENT FOR LOGIN
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_auth_container,new LoginFragment()).commit();

        // ONCLICK LISTENER FOR LOGIN BUTTON(TOGGLE)
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLogin.setBackgroundResource(R.drawable.button_bg_pink);
                btnReg.setBackgroundResource(R.drawable.button_bg);
                btnLogin.setTextColor(Color.WHITE);
                btnReg.setTextColor(Color.BLACK);
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_auth_container,new LoginFragment()).commit();
            }
        });

        // ON CLICK LISTENER FOR REGISTRATION BUTTON(TOGGLE)
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnReg.setBackgroundResource(R.drawable.button_bg_pink);
                btnLogin.setBackgroundResource(R.drawable.button_bg);
                btnReg.setTextColor(Color.WHITE);
                btnLogin.setTextColor(Color.BLACK);
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_auth_container,new RegistrationFragment()).commit();
            }
        });
        
        //GOOGLE SIGNING FUNCTION
        createRequest();

        //SET ONCLICK LISTENER FOR GOOGLE SIGNING BUTTON
        ibGoogleSigning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }

    //GOOGLE SIGNING FUNCTION DEFINITION
    private void createRequest() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    //GOOGLE SINGING METHOD
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent mainActivityIntent=new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(mainActivityIntent);
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginAndRegistrationActivity.this, "Sorry Auth Failed.", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
}