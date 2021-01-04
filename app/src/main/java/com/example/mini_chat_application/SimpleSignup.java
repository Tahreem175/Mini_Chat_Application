package com.example.mini_chat_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class SimpleSignup extends AppCompatActivity {


    private ProgressDialog LoadingBar;
    // yahan par hum ne instances banana shru kare
    // means k sb k objects ya variables lena tart kar dieye
    private EditText first_name, last_name, email, password, confirm_password;
    private RadioButton radioButtonMale, radioButtonFemale,radioButtonVendor,radioButtonUser;
    private Button signupbutton;
    private ProgressBar progressBar;
    ImageView visiblityone;
    ImageView visibilitytwo;
    // firebase ki authentication
    private FirebaseAuth auth;

    boolean passwordvisiblity = false;
    boolean checked= false;
    //EditText first_name, last_name, email, password, confirm_password;
    //  ImageView visiblity, match;
    // TextView signin, condition;
    //Button signupbutton;
    // boolean password_visiblity = false;
    // Drawable border;

    // firebase ki authentication ho ree hai
    private FirebaseAuth firebaseAuth;
    // DB ka refernce diya hai
    DatabaseReference databaseReference;

    // ye progress bar jece kaam karta hai
    // but yahan ye as such use nh ho rahaa hai
    ProgressDialog progressDialog;

    String radioButtonText="";


    // rejex k vahi patterns lag re hain jis me a se z tak k alphatbets hain
    // 0 se le k 09 tak k numbers hain aur ASCII ki tenms hin
    // ye bataa raha hai k ye ye pattern ho to accept hoga email varna nh karna hai
    public final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +

                    "\\@" +

                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +

                    "(" +

                    "\\." +

                    "[a-zA-Z][a-zA-Z\\-]{0,25}" +

                    ")+");


    public final Pattern Password_Pattern = Pattern.compile("[a-zA-Z0-9+]{6,20}");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_simple_signup);

        firebaseAuth = FirebaseAuth.getInstance();
        //Get Firebase auth instance
        // auth = FirebaseAuth.getInstance();



        signupbutton = (Button) findViewById(R.id.signup_btn);
        email = (EditText) findViewById(R.id.emailtxt);
        password = (EditText) findViewById(R.id.passtxt);
        confirm_password = (EditText)findViewById(R.id.passcond) ;
        first_name = (EditText) findViewById(R.id.fnametxt);
        last_name = (EditText) findViewById(R.id.lnametxt);



        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                signup();
                LoadingBar = new ProgressDialog(v.getContext());
                LoadingBar.setTitle("SIGNING UP ");
                LoadingBar.setMessage("PLEASE WAIT ");
                LoadingBar.setCanceledOnTouchOutside(true);
                LoadingBar.show();



            }
        });








    }



    void signup() {
        final String Email = email.getText().toString().trim();
        final String Pass = password.getText().toString().trim();
        final String fName = first_name.getText().toString().trim();
        final String lName = last_name.getText().toString().trim();


        if (fName.isEmpty()) {
            first_name.setError("Enter first name");
            first_name.requestFocus();
            Toast.makeText(getApplicationContext(), "Enter first name!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (lName.isEmpty()) {
            last_name.setError("Enter last name");
            last_name.requestFocus();
            Toast.makeText(getApplicationContext(), "Enter last name!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (Email.isEmpty()) {
            email.setError("Enter Email Address");
            email.requestFocus();
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (Pass.isEmpty()) {
            password.setError("Enter Password");
            email.requestFocus();
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (fName.isEmpty() && lName.isEmpty() && Email.isEmpty() && Pass.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter Fields", Toast.LENGTH_SHORT).show();
        } else if (!(fName.isEmpty() && lName.isEmpty() && Email.isEmpty() && Pass.isEmpty())) {


            databaseReference = FirebaseDatabase.getInstance().getReference();
            firebaseAuth.createUserWithEmailAndPassword(Email, Pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String uid = firebaseAuth.getCurrentUser().getUid();
                                User us = new User(Email, fName, lName);
                                databaseReference.child("Users").child(uid).setValue(us)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(SimpleSignup.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(SimpleSignup.this, socialLogin.class);
                                                startActivity(intent);
                                            }
                                        });
                                Intent intent = new Intent(SimpleSignup.this, socialLogin.class);
                                finish();
                                startActivity(intent);
                            } else {
                                Toast.makeText(SimpleSignup.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                            }
                        }
                    });
        } else {
            Toast.makeText(SimpleSignup.this, "Fill The Fields Properly", Toast.LENGTH_LONG).show();

        }


    }
}