package com.androidhari.tambola;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ua.naiksoftware.tambola.R;

public class ForgotPass extends AppCompatActivity {

    Button resetpass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_pass);

        resetpass = (Button)findViewById(R.id.submit);
        resetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(ForgotPass.this,ResetPass.class);

                startActivity(in);
            }
        });
    }
}

