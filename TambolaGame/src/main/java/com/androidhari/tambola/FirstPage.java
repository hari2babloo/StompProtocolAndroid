package com.androidhari.tambola;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import ua.naiksoftware.tambola.R;

public class FirstPage extends AppCompatActivity {

    Button btnsignin,btnsignup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.first_page);

        TextView textView = (TextView)findViewById(R.id.textView);

        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/segoeuil.ttf");

        textView.setTypeface(face);
        btnsignin = (Button)findViewById(R.id.signin);
        btnsignup = (Button)findViewById(R.id.btnsignup);


        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FirstPage.this,SignUp.class);
                startActivity(intent);
            }
        });
        btnsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FirstPage.this,Signin.class);
                startActivity(intent);


            }
        });


    }
}
