package com.androidhari.tambola;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidhari.db.TinyDB;
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

public class ChangePassword extends AppCompatActivity {


    AwesomeValidation awesomeValidation;
    EditText confirmpass,newpass;
    Button submit;
    String otp;
    private static final MediaType MEDIA_TYPE =   MediaType.parse("application/json");

    TinyDB tinydb;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);

        tinydb   = new TinyDB(this);
        otp = tinydb.getString("otp");

        tinydb = new TinyDB(this);
        confirmpass = (EditText)findViewById(R.id.confirmpass);
        newpass = (EditText)findViewById(R.id.newpass);
        submit = (Button)findViewById(R.id.submit);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        addValidationToViews();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (awesomeValidation.validate()){

                    changepass();
//
                }

            }
        });




    }

    private void addValidationToViews() {

        awesomeValidation.addValidation(ChangePassword.this,R.id.confirmpass,R.id.newpass,R.string.errcnfpass);
    }

    private void changepass() {

        pd = new ProgressDialog(ChangePassword.this);
        pd.setMessage("Resetting your password");
        pd.setCancelable(false);
        pd.show();


        final OkHttpClient client = new OkHttpClient();
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("password", newpass.getText().toString());
            postdata.put("token", otp);
        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MEDIA_TYPE,
                postdata.toString());


        final Request request = new Request.Builder()
                .url("http://game-dev.techmech.men:8080/api/resetpassword/update")
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
//                                String s = json.getJSON   Object("data").getString("token");
                                String msg = json.getString("message");
                                String status = json.getString("message");
                                Toast.makeText(ChangePassword.this, msg, Toast.LENGTH_LONG).show();

                                //   Toast.makeText(Signin.this, s, Toast.LENGTH_SHORT).show();

//                                SharedPreferences.Editor e = sp.edit();
//                                e.putString("token",s);
//
//                                e.commit();

                                Intent in = new Intent(ChangePassword.this,Signin.class);
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
                                Toast.makeText(ChangePassword.this, message, Toast.LENGTH_SHORT).show();
                                if (status.equalsIgnoreCase("401")){
                                    Toast.makeText(ChangePassword.this, message, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ChangePassword.this,Signin.class);
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
