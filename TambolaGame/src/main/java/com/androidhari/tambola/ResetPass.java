package com.androidhari.tambola;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidhari.db.TinyDB;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;

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

public class ResetPass extends AppCompatActivity {

    private static final MediaType MEDIA_TYPE =   MediaType.parse("application/json");;
    Button submit;
    TextView resettext;
    ProgressDialog pd;
    EditText email;
    SharedPreferences sp;
    AwesomeValidation awesomeValidation;
    TinyDB tinydb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.reset_pass);

        resettext = (TextView)findViewById(R.id.resettext);
        email = (EditText)findViewById(R.id.email);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        tinydb  = new TinyDB(this);
        addValidationToViews();

        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/segoeuil.ttf");
        resettext.setTypeface(face);

        submit = (Button)findViewById(R.id.resetpass);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (awesomeValidation.validate()){

                    Resetpassword();
//
                }
            }
        });
    }

    private void addValidationToViews() {


        awesomeValidation.addValidation(ResetPass.this,R.id.email, RegexTemplate.NOT_EMPTY, R.string.empty);
    }

    private void Resetpassword() {


        pd = new ProgressDialog(ResetPass.this);
        pd.setMessage("Validating your Details");
        pd.setCancelable(false);
        pd.show();


        final OkHttpClient client = new OkHttpClient();
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("email", email.getText().toString());
        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MEDIA_TYPE,
                postdata.toString());


        final Request request = new Request.Builder()
                .url("http://game-dev.techmech.men:8080/api/resetpassword/mobile")
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
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), mMessage, Snackbar.LENGTH_LONG);

                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#FF9800"));
                TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                snackbar.show();

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

                                JSONObject json2 = new JSONObject(mMessage).getJSONObject("data");
                                String d = json2.getString("sessionToken");
                                Log.d(d, String.valueOf(json2));
//                                String s = json.getJSON   Object("data").getString("token");
                                String msg = json.getString("message");
                                String status = json.getString("message");
                           //     Toast.makeText(ResetPass.this, msg, Toast.LENGTH_LONG).show();
                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG);

                                View snackBarView = snackbar.getView();
                                snackBarView.setBackgroundColor(Color.parseColor("#FF9800"));
                                TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
                                textView.setTextColor(Color.WHITE);
                                snackbar.show();
//                                if (status.equalsIgnoreCase("SUCCESS")){
//
//                                    tinydb.putString("otptype", "reset");
//                                    Intent in = new Intent(ResetPass.this,OTP.class);
//                                    startActivity(in);
//                                }

                                //   Toast.makeText(Signin.this, s, Toast.LENGTH_SHORT).show();

//                                SharedPreferences.Editor e = sp.edit();
//                                e.putString("token",s);
//
//                                e.commit();
                                tinydb.putString("sessionToken",json2.getString("sessionToken"));
                                tinydb.putString("resetidentity",email.getText().toString());
                                tinydb.putString("otptype", "reset");
                                Intent in = new Intent(ResetPass.this,OTP.class);
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
                            try {

                                pd.cancel();
                                pd.dismiss();
                                JSONObject json = new JSONObject(mMessage);
                                String status = json.getString("status");
                                String message = json.getString("message");
                                Toast.makeText(ResetPass.this, message, Toast.LENGTH_SHORT).show();
                                if (status.equalsIgnoreCase("401")){

                                    sp = getSharedPreferences("login", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.clear();
                                    editor.commit();

                                   Toast.makeText(ResetPass.this, message, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ResetPass.this,Signin.class);
                                    startActivity(intent);
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    });
                }

            }
        });
    }
}
