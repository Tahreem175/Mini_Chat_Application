package com.example.mini_chat_application;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.facebook.GraphRequest;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.regex.Pattern;


public class socialLogin extends AppCompatActivity {






    //  private CallbackManager mCallbackManager;

    private static final String TAG = "MyActivity";
    private static final String EMAIL = "email";
    SessionManager manager;
    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build();


    TextView et_email,et_pass;

    public final Pattern Email_pattern = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +

            "\\@" +

            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +

            "(" +

            "\\." +

            "[a-zA-Z][a-zA-Z\\-]{0,25}" +

            ")+");
    // password k patterns rakhe jaa re hain jece k pass ko hum alphabets bhi le len
    // Aur 0 se le k 9 tak ki numbers le len

    public final Pattern Password_Pattern = Pattern.compile("[a-zA-Z0-9+]{6,20}");

    /*
        private CallbackManager callbackManager;







        private static final String EMAIL = "email";





     */
    DatabaseReference dbref;
    DatabaseReference databaseReference;
    SharedPreferences.Editor sharedPreferences;
    LoginButton loginButton;
    GoogleSignInClient mGoogleSignInClient;
    SignInButton sign_in;
    int RC_SIGN_IN = 0;
    TextView signuptxt,signupbyemail;
    Button loginbuttonsimple;
    private FirebaseAuth firebaseAuth;
    private CallbackManager callbackManager;
    ProgressDialog progressDialog;


   // Button loginbuttonsimple;
    @Override
    protected void onStart() {
        super.onStart();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        //FacebookSdk.sdkInitialize(sociallogin.this);

        AppEventsLogger.activateApp(getApplication());


        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        socialLogin context = this;
        // FacebookSdk.sdkInitialize(context);
        //callbackManager = CallbackManager.Factory.create();

        // FacebookSdk.fullyInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_sociallogin);

        firebaseAuth = FirebaseAuth.getInstance();



        //   callbackManager = CallbackManager.Factory.create();
        // manager = new SessionManager(this);

        // firebase ki authentication ka kaam ka instance aarha hai ye


        //


        loginButton = findViewById(R.id.facebook_login);

        //LoginManager.getInstance().logInWithReadPermissions(,   Arrays.asList("EMAIL"));

        //loginButton.setReadPermissions(Arrays.asList(EMAIL));


        callbackManager = CallbackManager.Factory.create();
       // loginButton.setPermissions("public_profile","email", "user_birthday");



        signuptxt = findViewById(R.id.suptxt);
          loginbuttonsimple = (Button)findViewById(R.id.loginbuttonsimple);
        signupbyemail = (TextView)findViewById(R.id.signupbyemail);
        et_email = (EditText)findViewById(R.id.et_emailID);
        et_pass = (EditText)findViewById(R.id.etPass);
        // Initialize Facebook Login button
        //callbackManager = CallbackManager.Factory.create();
        //LoginButton loginButton = mBinding.buttonFacebookLogin;
        loginButton.setReadPermissions("email", "public_profile");

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);

                handleFacebookAccessToken(loginResult.getAccessToken() ,Profile.getCurrentProfile());

                Intent intent = new Intent(socialLogin.this, multiplechatview.class);
                startActivity(intent);

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        // Application code
                        Log.i(TAG, "onCompleted: response: " + response.toString());
                        try {
                            String email = object.getString("email");
                            String birthday = object.getString("birthday");

                            Log.i(TAG, "onCompleted: Email: " + email);
                            Log.i(TAG, "onCompleted: Birthday: " + birthday);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i(TAG, "onCompleted: JSON exception");
                        }
                    }
                });

                //handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });



       /*loginbuttonsimple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sign_in();

            }
        });


        */


        signupbyemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(socialLogin.this, SimpleSignup.class);
                startActivity(intent);
            }
        });


        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                SessionManager sessionManager = new SessionManager(getApplicationContext());
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

                Log.d("clima1 ", loginResult.toString());
                /*accessToken.getUserId();ahi
                Profile.getCurrentProfile().getFirstName();
                String email = Profile.getCurrentProfile().getId();

                facebooksignup(loginResult.getAccessToken(), Profile.getCurrentProfile());
                Profile.getCurrentProfile().getLastName();*/


                //  sessionManager.createSession("", "");
                // App code
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.d("clima", exception.getMessage());
            }
        });


        signuptxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(socialLogin.this, loginByPhone.class);
                startActivity(intent);

            }
        });

    }





   /* public void sign_in()
    {

        // email ID ,password vageraa le re hain yahan par aur if else k checks par se kaam kar re hain jin me kuch conditions hain
        String emaiAdd = et_email.getText().toString();

        // string me  save kareaa re hain is me email aur pass dnn ko
        String pword = et_pass.getText().toString();

        // email aur pass check karen
        if (checkEmail(emaiAdd) && checkPassword(pword)) {
            firebaseAuth.signInWithEmailAndPassword(emaiAdd, pword)
                    // ye dehan rahe k dnn chzen filled hon like email address hogaa, aur password dekhen ge
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // agar task successful ho jaata hai to User nam ka ek table firebase me hai mojud
                                // vo reference ajae gaa aur us ka instance aarhaa0 hai yahan par
                                // Uzer me basically sb ki information bparri v hai
                                dbref = FirebaseDatabase.getInstance().getReference("Users");
                                // value event listener se kaam ho traha hai aur is me listener ho raha hai
                                // jo k events recieve karta hai data changes par
                                dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        // firebase k instances hain basically snapshots matlab k databases ki duplicates rakhi jaati hain
                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                            if (dataSnapshot1.child("email_id").getValue(String.class).equals(emaiAdd)) {
                                                final String u_name = dataSnapshot1.child("firstn").getValue().toString();

                                                // is k email id and fierstname lastname vagrea ka string me connections ho rahe hain

                                                // shared preferences me ye hai k mmory kse saman ikhatta kar k laa re hain
                                                sharedPreferences = getSharedPreferences("usertype", MODE_PRIVATE).edit();
                                                sharedPreferences.putBoolean("utype", false);
                                                sharedPreferences.apply();
                                                // yahan par session create ho gaya jis me humare ps email aur pass hota hai
                                                manager.createSession(u_name, emaiAdd);
//                                            progressDialog.hide();

                                                // yahan par msg boxes ho re hain show k login successful karaa do
                                                Toast.makeText(getApplicationContext(), "Login Successfully", Toast.LENGTH_SHORT).show();
                                                Intent MainIntent = new Intent(socialLogin.this,multiplechatview.class);
                                                startActivity(MainIntent);
                                                finish();





                                                break;
                                            }
                                        }
                                    }








                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            } else {


                                // varna aap  ye msg show karaa do k available nh hai aap ka email
                                Toast.makeText(socialLogin.this, "Enter a valid Email And Password !", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else
        {
            Toast.makeText(socialLogin.this, "Check Your Internet Connection!", Toast.LENGTH_SHORT).show();

        }


    }

    */

    public void sign_in(View view) {
        String e_mail = et_email.getText().toString();
        String p_word = et_pass.getText().toString();
        if (TextUtils.isEmpty(e_mail)) {
            et_email.setError("Field empty");
        }
        else if (TextUtils.isEmpty(p_word)){
            et_pass.setError("Field empty");
            //Toast.makeText(this, "Fill All Fields", Toast.LENGTH_SHORT).show();
        } else {
            if (checkEmail(e_mail) && checkPassword(p_word)) {


                firebaseAuth.signInWithEmailAndPassword(e_mail, p_word).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {



                            //progressDialog.hide();

                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                        if(dataSnapshot1.child("email_id").getValue().toString().equals(e_mail))
                                        {
                                            String u_name = dataSnapshot1.child("firstn").getValue().toString();
//                                            Intent intent1 = new Intent(getApplicationContext(),popPostScholar.class);
//                                            intent1.putExtra("userName",u_name);
//                                            intent1.putExtra("userEmail",e_mail);
//                                            startActivity(intent1);
                                            Log.d("mssg"+ u_name,"mssg");
//                                            manager.createSession(u_name, e_mail);
                                            Toast.makeText(getApplicationContext(), "Login Successfully", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getApplicationContext(), multiplechatview.class);
                                            finish();
                                            startActivity(intent);

                                            break;
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }

                        else
                        {
                            progressDialog.hide();
                            Toast.makeText(socialLogin.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            } else if (!checkEmail(e_mail)) {
                Toast.makeText(this, "Email not valid", Toast.LENGTH_SHORT).show();
            } else if (!checkPassword(p_word)) {
                Toast.makeText(this, "Password not valid", Toast.LENGTH_SHORT).show();
            }

        }

    }


    private boolean checkPassword(String password) {

        // password pattern ko check krana hai jo hum ne rejex sekaam kiyaa hai
        return Password_Pattern.matcher(password).matches();
    }

    // email check kare gaa  aur us k patern se match bhi kare gaa
    private boolean checkEmail(String email) {
        return Email_pattern.matcher(email).matches();
    }

    /*

    private void handleFacebookAccessToken(AccessToken token , final Profile profile) {
//        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        final String emaiAdd = task.getResult().getUser().getEmail();
                        if (task.isSuccessful()) {
                            String uid = firebaseAuth.getCurrentUser().getUid();
                            User us = new User(task.getResult().getUser().getEmail(),profile.getFirstName(), profile.getLastName());
                            databaseReference.child("Users").child(uid).setValue(us)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull final Task<Void> task) {


                                            dbref = FirebaseDatabase.getInstance().getReference("Users");
                                            dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    // firebase k instances hain basically snapshots matlab k databases ki duplicates rakhi jaati hain
                                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                        if (dataSnapshot1.child("email_id").getValue(String.class).equals(emaiAdd)) {
                                                            final String u_name = dataSnapshot1.child("firstn").getValue().toString();


                                                            sharedPreferences = getSharedPreferences("usertype", MODE_PRIVATE).edit();
                                                            sharedPreferences.putBoolean("utype", false);
                                                            sharedPreferences.apply();

                                                            manager.createSession(u_name, emaiAdd);
//                                            progressDialog.hide();

                                                            // yahan par msg boxes ho re hain show k login successful karaa do
                                                            Toast.makeText(getApplicationContext(), "Login Successfully", Toast.LENGTH_SHORT).show();
                                                            Intent MainIntent = new Intent(sociallogin.this, convo_list.class);
                                                            startActivity(MainIntent);
                                                            finish();





                                                            break;
                                                        }
                                                    }
                                                }








                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                        }
                                    });
                            Intent intent = new Intent(sociallogin.this, convo_list.class);
                            finish();
                            startActivity(intent);
                        } else {
                            Toast.makeText(sociallogin.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }


     */


    private void facebooksignup(AccessToken accessToken, Profile currentProfile) {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("EMAIL"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

/*

    private void handleFacebookAccessToken(AccessToken token, Profile currentProfile){
        Log.d(TAG,"handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Login Successfully", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }




 */

    private void handleFacebookAccessToken(AccessToken token, final Profile profile) {
//        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        final String emaiAdd = task.getResult().getUser().getEmail();
                        if (task.isSuccessful()) {
                            String uid = firebaseAuth.getCurrentUser().getUid();
                            User us = new User(task.getResult().getUser().getEmail(), profile.getFirstName(), profile.getLastName());
                            databaseReference.child("Users").child(uid).setValue(us)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull final Task<Void> task) {


                                            databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                                            // value event listener se kaam ho traha hai aur is me listener ho raha hai
                                            // jo k events recieve karta hai data changes par
                                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    // firebase k instances hain basically snapshots matlab k databases ki duplicates rakhi jaati hain
                                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                        if (dataSnapshot1.child("email_id").getValue(String.class).equals(emaiAdd)) {
                                                            final String u_name = dataSnapshot1.child("firstn").getValue().toString();

                                                            // is k email id and fierstname lastname vagrea ka string me connections ho rahe hain

                                                            // shared preferences me ye hai k mmory kse saman ikhatta kar k laa re hain
                                                            sharedPreferences = getSharedPreferences("usertype", MODE_PRIVATE).edit();
                                                            sharedPreferences.putBoolean("utype", false);
                                                            sharedPreferences.apply();
                                                            // yahan par session create ho gaya jis me humare ps email aur pass hota hai
                                                            manager.createSession(u_name, emaiAdd);
//                                            progressDialog.hide();


                                                            // yahan par msg boxes ho re hain show k login successful karaa do
                                                            Toast.makeText(getApplicationContext(), "Login Successfully", Toast.LENGTH_SHORT).show();
                                                            Intent MainIntent = new Intent(socialLogin.this, multiplechatview.class);
                                                            startActivity(MainIntent);
                                                            finish();


                                                            break;
                                                        }
                                                    }
                                                }


                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                        }
                                    });
                            Intent intent = new Intent(socialLogin.this, convo_list.class);
                            finish();
                            startActivity(intent);
                        } else {
                            Toast.makeText(socialLogin.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

    }


}
