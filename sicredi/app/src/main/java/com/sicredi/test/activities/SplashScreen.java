package com.sicredi.test.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.sicredi.test.R;

public class SplashScreen extends AppCompatActivity {

    private int splashScreenDuration = 4000;
    Animation topAnim;
    ImageView ivLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        formElements();
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        ivLogo.setAnimation(topAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, MainDrawer.class);
                startActivity(intent);

                SplashScreen.this.finish();
            }
        }, splashScreenDuration);
    }

    public void formElements(){
        ivLogo = findViewById(R.id.ivLogo);
    }
}
