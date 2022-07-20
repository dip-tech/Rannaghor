package com.example.rannaghor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.rannaghor.R.drawable.onboarding_bg_1;
import static com.example.rannaghor.R.drawable.onboarding_bg_2;
import static com.example.rannaghor.R.drawable.onboarding_bg_3;
import static com.example.rannaghor.R.drawable.splash_screen_ic;

public class OnboardingScreenActivity extends AppCompatActivity {

    ViewPager2 OnboardingViewPage;
    LinearLayout OnboardingIndicators;
    Button OnboardingButton;
    TextView OnboardingtextViewSkip;
    ArrayList<Integer> ImageList=new ArrayList<>();
    SharedPreferences sharedPreferences;
    Boolean firstTimeShow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding_screen);

        // set status bar color....
        Window window = getWindow();
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.black));
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setNavigationBarColor(getResources().getColor(R.color.black));
        }
        //Attatched All Resources
        OnboardingViewPage=findViewById(R.id.onboarding_viewpager);
        OnboardingButton=findViewById(R.id.onboarding_button);
        OnboardingIndicators=findViewById(R.id.onboarding_LinearLayout);
        OnboardingtextViewSkip=findViewById(R.id.tv_skip);

        //Adding Images To the ArrayList..
        ImageList.add(onboarding_bg_1);
        ImageList.add(onboarding_bg_2);
        ImageList.add(onboarding_bg_3);
        sharedPreferences=getSharedPreferences("RannaghorStorage",MODE_PRIVATE);
        firstTimeShow=sharedPreferences.getBoolean("firstTime",true);
        if(firstTimeShow){
            //edit sharedpreferences....
            SharedPreferences.Editor editor=sharedPreferences.edit();
            firstTimeShow=false;
            editor.putBoolean("firstTime",firstTimeShow);
            editor.apply();

            // Create and Set Adaper for Onboarding View Pager....
            OnboardingAdapter onboardingAdapter=new OnboardingAdapter(ImageList);
            OnboardingViewPage.setAdapter(onboardingAdapter);

            // set indicators for Onboarding viewPager
            setIndicator();
            currentIndicator(0);

            // set a callback method when a page change state...
            OnboardingViewPage.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    currentIndicator(position);
                }
            });

            //set a onnclick listner for onboarding button...
            OnboardingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(OnboardingViewPage.getCurrentItem()+1<3){
                        OnboardingViewPage.setCurrentItem(OnboardingViewPage.getCurrentItem()+1);
                    }
                    else {
                        startActivity(new Intent(OnboardingScreenActivity.this,LoginAndRegistrationActivity.class));
                        finish();
                    }
                }
            });
        }
        else{
            startActivity(new Intent(OnboardingScreenActivity.this,LoginAndRegistrationActivity.class));
            finish();
        }


        OnboardingtextViewSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OnboardingScreenActivity.this,LoginAndRegistrationActivity.class));
                finish();
            }
        });
    }
    /// setIndicator function....
    public void setIndicator()
    {
        ImageView[] indicators=new ImageView[3];
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(8,0,8,0);
        for(int i=0;i<indicators.length;i++){
            indicators[i]=new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.onboard_indecator_inactive));
            indicators[i].setLayoutParams(layoutParams);
            OnboardingIndicators.addView(indicators[i]);
        }
    }

    // set current indicator function......
    public void currentIndicator(int index)
    {
        int childCount=OnboardingIndicators.getChildCount();
        for(int i=0;i<childCount;i++){
            ImageView imageView=(ImageView)OnboardingIndicators.getChildAt(i);
            if(i==index)
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.onboarding_indicator_active));
            else
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.onboard_indecator_inactive));
        }
        if(index==3-1){
            OnboardingButton.setText("Get Started");

        }
        else
        {
            OnboardingButton.setText("Next");
        }
    }
}