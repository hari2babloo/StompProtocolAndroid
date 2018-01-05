package com.androidhari.tambola;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidhari.db.TinyDB;
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;
import com.stfalcon.smsverifycatcher.OnSmsCatchListener;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import ua.naiksoftware.tambola.R;


public class OTP extends AppCompatActivity implements OTPListener{

    public static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json");
    EditText otp;
    ProgressDialog pd;
    TextView resend;
    String otptype;

    TinyDB tinydb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp);
        otp = (EditText)findViewById(R.id.otp);
        tinydb   = new TinyDB(this);
        otptype = tinydb.getString("otptype");
        resend = (TextView)findViewById(R.id.resend);


        int GET_MY_PERMISSION = 1;
        if(ContextCompat.checkSelfPermission(OTP.this,Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(OTP.this,
                    Manifest.permission.READ_SMS)){
            /* do nothing*/
            }
            else{

                ActivityCompat.requestPermissions(OTP.this,
                        new String[]{Manifest.permission.READ_SMS},GET_MY_PERMISSION);
            }
        }
        OtpReader.bind(this,"MD-CAMERA");

        otp.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                String str = otp.getText().toString();
//                len = str.length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int len=0;
                String str = otp.getText().toString();
                if(str.length()==6 && len <str.length()){//len check for backspace
                   // Toast.makeText(OTP.this, "6 Digits finished", Toast.LENGTH_SHORT).show();

                    if (otptype.equalsIgnoreCase("reset")){

                        resetotp();
                    }

                    else if (otptype.equalsIgnoreCase("signup")){

                        signupotp();
                    }


                }
            }


        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendotp();
            }
        });

    }

    private void resendotp() {


        pd = new ProgressDialog(OTP.this);
        pd.setMessage("Sending a new OTP..");
        pd.setCancelable(false);
        pd.show();
        otp.getText().clear();

        final OkHttpClient client = new OkHttpClient();
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("email", tinydb.getString("resetidentity"));
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
                                tinydb.putString("sessionToken",d);
//                                String s = json.getJSON   Object("data").getString("token");
                                String msg = json.getString("message");
                                String status = json.getString("message");
                                Toast.makeText(OTP.this, msg, Toast.LENGTH_LONG).show();
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
//                                tinydb.putString("resetidentity",email.getText().toString());
//                                tinydb.putString("otptype", "reset");
//                                Intent in = new Intent(ResetPass.this,OTP.class);
//                                startActivity(in);
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
                                Toast.makeText(OTP.this, message, Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    });
                }

            }
        });
    }

    private void resetotp() {

        pd = new ProgressDialog(OTP.this);
        pd.setMessage("Validating Your OTP");
        pd.setCancelable(false);
        pd.show();
        final OkHttpClient client = new OkHttpClient();
        JSONObject postdata = new JSONObject();

        try {

            postdata.put("token", otp.getText().toString());
            postdata.put("sessionToken",tinydb.getString("sessionToken"));


        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE,
                postdata.toString());

        Log.e("sdsad", String.valueOf(body));
        final Request request = new Request.Builder()
                .url("http://game-dev.techmech.men:8080/api/resetpassword/token/m")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();
        Log.e("dasdasd", body.toString());
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                pd.dismiss();
                pd.cancel();

                String mMessage = e.getMessage().toString();
                Log.w("failure Response", mMessage);
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), mMessage, Snackbar.LENGTH_LONG);

                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#FF9800"));
                TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                snackbar.show();

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String mMessage = response.body().string();
                Log.w("Response", mMessage);
                if (response.isSuccessful()){
                    pd.dismiss();
                    pd.cancel();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject json = new JSONObject(mMessage);
                                String s = json.getString("message");
                                Log.e("Message",s);
                                Toast.makeText(OTP.this, s, Toast.LENGTH_SHORT).show();

                               // tinydb.putString("otp",otp.getText().toString());
                                tinydb.putString("otp",otp.getText().toString());
                                Log.e("fdsfsdfsd",otp.getText().toString());
                                Intent intent = new Intent(OTP.this,ChangePassword.class);
                                startActivity(intent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
                                //title = name;

                                Toast.makeText(OTP.this, message, Toast.LENGTH_SHORT).show();
                                if (status.equalsIgnoreCase("401")){


                                    Intent intent = new Intent(OTP.this,Signin.class);
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
    @Override
    public void otpReceived(String messageText) {
        Toast.makeText(this,"Got "+messageText, Toast.LENGTH_LONG).show();
        Log.d("Otp",messageText);

        String code = parseCode(messageText);


        otp.setText(code);
    }

    private String parseCode(String messageText) {
        Pattern p = Pattern.compile("(|^)\\d{6}");
        Matcher m = p.matcher(messageText);
        String code = "";
        while (m.find()) {
            code = m.group(0);
        }
        return code;
    }
    private void signupotp() {
        pd = new ProgressDialog(OTP.this);
        pd.setMessage("Validating Your OTP");
        pd.setCancelable(false);
        pd.show();
        final OkHttpClient client = new OkHttpClient();
        JSONObject postdata = new JSONObject();

        try {

            postdata.put("sessionToken",tinydb.getString("sessionToken"));
            postdata.put("token", otp.getText().toString());


        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE,
                postdata.toString());

        Log.e("sdsad", String.valueOf(body));
        final Request request = new Request.Builder()
                .url("http://game-dev.techmech.men:8080/api/user/verify")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();
        Log.e("dasdasd", body.toString());
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                pd.dismiss();
                pd.cancel();

                String mMessage = e.getMessage().toString();
                Log.w("failure Response", mMessage);
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), mMessage, Snackbar.LENGTH_LONG);

                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#FF9800"));
                TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                snackbar.show();

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String mMessage = response.body().string();
                Log.w("Response", mMessage);
                if (response.isSuccessful()){
                    pd.dismiss();
                    pd.cancel();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject json = new JSONObject(mMessage);
                                String s = json.getString("message");
                                Log.e("Message",s);
                           //     Toast.makeText(OTP.this, s, Toast.LENGTH_SHORT).show();
                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), s, Snackbar.LENGTH_LONG);

                                View snackBarView = snackbar.getView();
                                snackBarView.setBackgroundColor(Color.parseColor("#FF9800"));
                                TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
                                textView.setTextColor(Color.WHITE);
                                snackbar.show();

                                Intent intent = new Intent(OTP.this,Signin.class);
                                startActivity(intent);
                               // Toast.makeText(OTP.this, s, Toast.LENGTH_SHORT).show();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

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
                                //title = name;
                              //  Toast.makeText(OTP.this, message, Toast.LENGTH_SHORT).show();
                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);

                                View snackBarView = snackbar.getView();
                                snackBarView.setBackgroundColor(Color.parseColor("#FF9800"));
                                TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
                                textView.setTextColor(Color.WHITE);
                                snackbar.show();

                                if (status.equalsIgnoreCase("401")){

                                 SharedPreferences   sp = getSharedPreferences("login", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.clear();
                                    editor.commit();



                                     Intent intent = new Intent(OTP.this,Signin.class);
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
