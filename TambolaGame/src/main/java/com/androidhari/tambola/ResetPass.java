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

public class ResetPass extends AppCompatActivity {

    Button submit;
    TextView resettext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.reset_pass);

        resettext = (TextView)findViewById(R.id.resettext);


        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/segoeuil.ttf");
        resettext.setTypeface(face);

        submit = (Button)findViewById(R.id.resetpass);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResetPass.this,Signin.class);

                startActivity(intent);
            }
        });
    }
}
