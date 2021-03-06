package com.androidhari.tambola;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidhari.db.TinyDB;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;

import org.json.JSONArray;
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

public class SignUp extends Activity {

    TextView login,fname,lname,email,phno,pass,cnfpass,refcode;
    AwesomeValidation awesomeValidation;
    Button submit;
    ProgressDialog pd;
    TextView loginlink;
    TinyDB tinydb;

    public static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.sign_up);

tinydb  = new TinyDB(this);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);


        submit = (Button)findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (awesomeValidation.validate()){

                    Signup();
//
                }

            }
        });

        login = (TextView)findViewById(R.id.loginlink2);
        fname=(TextView)findViewById(R.id.fname);
        lname=(TextView)findViewById(R.id.lname);
        email=(TextView)findViewById(R.id.email);
        phno=(TextView)findViewById(R.id.phno);
        pass=(TextView)findViewById(R.id.pass);
        cnfpass=(TextView)findViewById(R.id.cnfpass);
        refcode = (TextView)findViewById(R.id.refcode);

        addValidationToViews();



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent in = new Intent(getApplicationContext(),Signin.class);
                startActivity(in);
            }
        });


    }



    private void Signup() {


        pd = new ProgressDialog(SignUp.this);
        pd.setMessage("Creating Your Account");
        pd.setCancelable(false);
        pd.show();



        final OkHttpClient client = new OkHttpClient();

        JSONObject postdata = new JSONObject();
        JSONArray postdata2 = new JSONArray();
        postdata2.put("ROLE_PLAYER_SILVER");


        try {

            postdata.put("email", email.getText().toString());
            postdata.put("password", pass.getText().toString());
            postdata.put("first_name", fname.getText().toString());
            postdata.put("last_name", lname.getText().toString());
            postdata.put("phone_no", phno.getText().toString());
            postdata.put("referalCode",refcode.getText().toString());
            postdata.put("role",postdata2);


        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MEDIA_TYPE,
                postdata.toString());


        final Request request = new Request.Builder()
                .url("http://game-dev.techmech.men:8080/api/user/signup")
                .post(body)

                .addHeader("Content-Type", "application/json")
                .build();


        Log.e("Value",postdata.toString());
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

pd.dismiss();
                pd.cancel();
                Log.w("Response", mMessage);
                if (response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                JSONObject json = new JSONObject(mMessage);
                                JSONObject json2 = new JSONObject(mMessage).getJSONObject("data");
                                String d = json2.getString("sessionToken");
                                tinydb.putString("otptype", "signup");
                                tinydb.putString("sessionToken",json2.getString("sessionToken"));
                                Intent intent = new Intent(SignUp.this,OTP.class);
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
                                JSONObject json = new JSONObject(mMessage);


                                String s = json.getString("message");
                               // Toast.makeText(SignUp.this, s, Toast.LENGTH_SHORT).show();
                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), s, Snackbar.LENGTH_LONG);

                                View snackBarView = snackbar.getView();
                                snackBarView.setBackgroundColor(Color.parseColor("#FF9800"));
                                TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
                                textView.setTextColor(Color.WHITE);
                                snackbar.show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.d("reafa",mMessage);
                            pd.cancel();
                            pd.dismiss();
                        }
                    });
                }

            }
        });
    }

    private void addValidationToViews() {

      //  String regexPassword = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}";
        awesomeValidation.addValidation(SignUp.this,R.id.fname, "[a-zA-Z\\s]+",R.string.errfname);
        awesomeValidation.addValidation(SignUp.this,R.id.lname,"[a-zA-Z\\s]+",R.string.errlname);
        awesomeValidation.addValidation(SignUp.this,R.id.email, Patterns.EMAIL_ADDRESS,R.string.erremail);
        awesomeValidation.addValidation(SignUp.this,R.id.phno, RegexTemplate.TELEPHONE, R.string.errphne);
        //awesomeValidation.addValidation(SignUp.this,R.id.pass,regexPassword, R.string.errpass);
        awesomeValidation.addValidation(SignUp.this,R.id.cnfpass,R.id.pass,R.string.errcnfpass);
    }
}
