package com.business.smart.smart_hr;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by ESRAA on 2016-07-10.
 */
public class SplashActivity extends Activity{

    Handler handler;
    TextView mSwitcher;

    Animation in;
    Animation out;

    int fadeCount;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        // SMART HR word appear from this
        fadeCount = 0;
        handler = new Handler();
        mSwitcher = (TextView) findViewById(R.id.app_Title);
         mSwitcher.setText("old text");
        in = new AlphaAnimation(0.0f, 1.0f);
        in.setDuration(5000);
        mSwitcher.setText("Smart HR");
        mSwitcher.startAnimation(in);
       /*
        mSwitcher.startAnimation(out);
        mSwitcher.setText("Text 2.");
        mSwitcher.startAnimation(in);
        */

//        handler.postDelayed(mFadeOut, 5000);

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    //time of the splash screen
                    sleep(5000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
            }
        };
        timerThread.start();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }
}
