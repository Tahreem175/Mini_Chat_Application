package com.example.mini_chat_application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;

public class Splash_Mini_app_Chat extends AppCompatActivity {
    private static final int SPLASH_TIME_OUT = 4000;


    SessionManager manager;
    ProgressBar pb;

    private int mProgressStatus = 0;
    private final Handler mHandler = new Handler();
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);

   //   requestWindowFeature(Window.FEATURE_NO_TITLE);
     //   getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_splash_mini_app_chat);
        pb = findViewById(R.id.proog);
        mAuth=FirebaseAuth.getInstance();



     //   new Handler().postDelayed(new Runnable(){
      //      @Override
        //    public void run() {
          //      /* Create an Intent that will start the Menu-Activity. */
            //    Intent mainIntent = new Intent(Splash_Mini_app_Chat.this,sociallogin.class);
            //    Splash_Mini_app_Chat.this.startActivity(mainIntent);
              //  Splash_Mini_app_Chat.this.finish();
           // }
        //}, 4000);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mProgressStatus < 100) {
                    mProgressStatus++;
                    android.os.SystemClock.sleep(50);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            pb.setProgress(mProgressStatus);
                        }
                    });
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        final Intent t = new Intent(Splash_Mini_app_Chat.this, socialLogin.class);
                        Thread timer = new Thread() {
                            public void run() {
                                try {
                                    sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } finally {

                                    if(mAuth.getCurrentUser()!=null){
                                        Intent intent = new Intent(Splash_Mini_app_Chat.this, multiplechatview.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }else {
                                        startActivity(t);
                                    }
                                        finish();
                                }

                            }
                        };
                        timer.start();
                    }
                });
            }
        }).start();



    }
}