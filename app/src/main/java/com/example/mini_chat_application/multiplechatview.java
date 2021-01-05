package com.example.mini_chat_application;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class multiplechatview extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private AppBarConfiguration mAppBarConfiguration;

    private ProgressDialog LoadingBar;
    DatabaseReference databaseReference;
    TextView textViewnamenav, textviewemailnav;
    ImageView imageViewProf;
    String user_id;
    String U_Fname = "", U_LName = "", U_Email = "", U_pic = "";
    FirebaseAuth firebaseAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplechatview);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(multiplechatview.this, SendSMS.class);
                startActivity(intent);
            }

        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);



        navigationView.setNavigationItemSelectedListener(this);


        View headerView = navigationView.getHeaderView(0);



        textViewnamenav = headerView.findViewById(R.id.profilename);
        textviewemailnav = headerView.findViewById(R.id.profileemail);
        imageViewProf = headerView.findViewById(R.id.imageViewprof);
        getPersonalInfo();
    }

    void getPersonalInfo() {

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

            user_id  = firebaseUser.getUid();

            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(user_id);

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    User mUser = dataSnapshot.getValue(User.class);
                    if(mUser != null){
                        U_pic = mUser.getUrl();
                        textViewnamenav.setText(mUser.getFirstn() + " " + mUser.getLastn() );
                        // edt_lastname.setText(U_LName);
                        textviewemailnav.setText(mUser.getEmail_id());
                        //  userNameDisplay.setText(U_Fname +" "+ U_LName);
                        //  userEmailDisplay.setText(U_Email);

                        if(U_pic != null && U_pic.length() > 0){
                            Glide.with(multiplechatview.this).load(U_pic).into(imageViewProf);
                        }
                    }

/*

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//                    if(dataSnapshot1.child(user_id).child("firstn").getValue(String.class) != null) {
//                        U_Fname = dataSnapshot1.child(user_id).child("firstn").getValue(String.class);
//                    }
//                    if(dataSnapshot1.child(user_id).child("lastn").getValue(String.class) != null) {
//                        U_LName = dataSnapshot1.child(user_id).child("lastn").getValue(String.class);
//                    }
//                    U_Email = dataSnapshot1.child(user_id).child("email_id").getValue(String.class);

                    if (dataSnapshot1.child(user_id).child("url").exists())
                    {
                        // child hai jis me URL ki value get karaa re hain
                        //U_pic = dataSnapshot1.child(user_id).child("url").getValue(String.class);

                    }

                }

*/

                    // data set karayaa
                    // jo heading par likhna hai .. us k liey bho ae ga ye

                    //U_Email = firebaseUser.getEmail();
                    //U_Fname = firebaseUser.getDisplayName();


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Toast.makeText(multiplechatview.this, "Error!!", Toast.LENGTH_SHORT).show();

                }
            });
        }

        // database ka refernce de diya hai yahan par
//        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
//        // listener add karaa diyaa yahan par
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//
//            // data ko change kr k vps db me bhi to store krana hoga naa
//            // to vo data snapshot me ae gaa jis me User ka table hai
//            // aur us k childs hain 3  jis me name last name aur email address hain
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.multiplechatview, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;

        } else if (id == R.id.nav_edit_profile) {
            Intent intent = new Intent(multiplechatview.this, Edit_Profile.class);
            startActivity(intent);


        } else if (id == R.id.change_pass) {


        } else if (id == R.id.logout) {


            LoadingBar = new ProgressDialog(this);
            LoadingBar.setTitle("LOGGING OUT");
            LoadingBar.setMessage("PLEASE WAIT");
            LoadingBar.setCanceledOnTouchOutside(false);
            LoadingBar.show();
            LoginManager.getInstance().logOut();


            Toast.makeText(multiplechatview.this, "Logged Out!", Toast.LENGTH_SHORT).show();
            SessionManager manager = new SessionManager(multiplechatview.this);
            manager.logout();
        }
        else if (id == R.id.clear_cache) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;


    }
}