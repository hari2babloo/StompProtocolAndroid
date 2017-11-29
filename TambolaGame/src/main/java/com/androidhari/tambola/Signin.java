package com.androidhari.tambola;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import ua.naiksoftware.tambola.R;

public class Signin extends Activity {


    TextView forgotpass,signup;
    SharedPreferences sp;
    ProgressDialog pd;


    AwesomeValidation awesomeValidation;

    EditText username,pass;
    Button login;
    public static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);
        sp=getSharedPreferences("login",MODE_PRIVATE);

        login =(Button)findViewById(R.id.submit);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        username = (EditText)findViewById(R.id.username);
        pass = (EditText)findViewById(R.id.pass);


        forgotpass = (TextView)findViewById(R.id.forgotpass);
        signup = (TextView)findViewById(R.id.signup);


        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/segoeuil.ttf");

       // String regexPassword = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}";
      //  awesomeValidation.addValidation(Signin.this,R.id.pass,regexPassword, R.string.errpass);
        awesomeValidation.addValidation(Signin.this,R.id.username,android.util.Patterns.EMAIL_ADDRESS,R.string.errfname);

        forgotpass.setTypeface(face);
        signup.setTypeface(face);





        logincheck();


        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(Signin.this,ForgotPass.class);
                startActivity(intent);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(Signin.this,SignUp.class);
                startActivity(intent);
            }
        });



    }

    private void logincheck() {



        String pass = sp.getString("token",null);
        if (pass!=null && !pass.isEmpty()){

            Toast.makeText(this, "Welcome Back Gamer", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,HomeScreen.class));

        }

        else {

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (awesomeValidation.validate()){
                    Authenticate();
                }


            }
        });

           Toast.makeText(this, "Signin", Toast.LENGTH_SHORT).show();
        }

    }

    private void Authenticate() {

        pd = new ProgressDialog(Signin.this);
        pd.setMessage("Signing In");
        pd.setCancelable(false);
        pd.show();


        final OkHttpClient client = new OkHttpClient();
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("username", username.getText().toString());
            postdata.put("password", pass.getText().toString());
        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MEDIA_TYPE,
                postdata.toString());


        final Request request = new Request.Builder()
                .url("http://game-dev.techmech.men:8080/auth")
                .post(body)
                .addHeader("Content-Type", "application/json")

                .build();


        Log.e("dasdasd", body.toString());



        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                pd.cancel();
                pd.cancel();

                String mMessage = e.getMessage().toString();
                Log.w("failure Response", mMessage);

//                Toast.makeText(Signin.this, mMessage, Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String mMessage = response.body().string();
                pd.cancel();
                pd.dismiss();

                Log.w("Response", mMessage);
                if (response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                JSONObject json = new JSONObject(mMessage);
                                String s = json.getJSONObject("data").getString("token");
                                String st = json.getString("message");
                                Toast.makeText(Signin.this, st, Toast.LENGTH_SHORT).show();

                                Log.w("Response",st);
                                //   Toast.makeText(Signin.this, s, Toast.LENGTH_SHORT).show();

                                SharedPreferences.Editor e = sp.edit();
                                e.putString("token",s);

                                e.commit();
                                Intent in = new Intent(Signin.this,HomeScreen.class);
                                startActivity(in);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


//                            Toast.makeText(Signin.this, "Success", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

                else {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            pd.cancel();
                            pd.dismiss();

                            Toast.makeText(Signin.this, "User already logged in other system, please logout", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
    }
}
