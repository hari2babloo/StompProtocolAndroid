package com.androidhari.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidhari.ViewPager.WalletTransactions;
import com.androidhari.tambola.FirstPage;
import com.androidhari.tambola.HomeScreen;
import com.androidhari.tambola.Signin;
import com.androidhari.tambola.Wallet;
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

public class BankTransfer extends AppCompatActivity {

    SharedPreferences sp;
    ProgressDialog pd;
    String pass, gameid,amt;
    EditText name,accnumber,accno2,ifsc,bankdetails;
    Button transfer;
    AwesomeValidation awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
    public static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bank_transfer);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        sp=getSharedPreferences("login",MODE_PRIVATE);
        pass=sp.getString("token",null);
        gameid=sp.getString("gno",null);
        amt=sp.getString("amt",null);
        Log.e("amount",amt);
        name = (EditText)findViewById(R.id.name);
        accnumber =(EditText)findViewById(R.id.accno);
        accno2 =(EditText)findViewById(R.id.accno2);
        ifsc =(EditText)findViewById(R.id.ifsc);
        bankdetails = (EditText)findViewById(R.id.bank);
        transfer = (Button)findViewById(R.id.transfer2);


        transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(name.getText().toString())){

                    name.setError("Enter name");
                }
                else if (TextUtils.isEmpty(accnumber.getText().toString())){

                    accnumber.setError("Enter Account Number");
                }

                else if (TextUtils.isEmpty(accno2.getText().toString())){

                    accno2.setError(" Enter Your Account Number");
                }
                else if (!accnumber.getText().toString().equals(accno2.getText().toString())){

                    accno2.setError("Account Number DO not Match");
                }

                else if(TextUtils.isEmpty(ifsc.getText().toString())){
                    ifsc.setError("Enter IFSC Code");
                }
                else if (TextUtils.isEmpty(bankdetails.getText().toString())){
                    bankdetails.setError("Enter Bank Details");

                }

                else {

                    Transfer();
                }
            }
        });


//        ActionBar ab = getSupportActionBar();
//        // Enable the Up button
//        ab.setDisplayHomeAsUpEnabled(true);
    }


    private void Transfer() {

        pd = new ProgressDialog(BankTransfer.this);
        pd.setMessage("Transferring your Money");
        pd.setCancelable(false);
        pd.show();


        final OkHttpClient client = new OkHttpClient();
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("cashOutType", "BANK_ACC");
            postdata.put("accountName", name.getText().toString());
            postdata.put("accountNumber",accnumber.getText().toString());
            postdata.put("IFSCCode", ifsc.getText().toString());
            postdata.put("branchDetails",bankdetails.getText().toString());
            postdata.put("amount", amt);


        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MEDIA_TYPE,
                postdata.toString());

        Log.e("postvalue", String.valueOf(body));

        final Request request = new Request.Builder()
                .url("http://game-dev.techmech.men:8080/api/wallet/cashout")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization",pass)

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
                                Log.w("Response", String.valueOf(json));
//                                Toast.makeText(BankTransfer.this, json.getString("message").toString(), Toast.LENGTH_SHORT).show();


                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), json.getString("message").toString(), Snackbar.LENGTH_LONG);
                                View snackBarView = snackbar.getView();
                                snackBarView.setBackgroundColor(Color.parseColor("#FF9800"));
                                TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
                                textView.setTextColor(Color.WHITE);
                                snackbar.show();
                            Intent intent = new Intent(BankTransfer.this,WalletTransactions.class);
                                startActivity(intent);
                                //   Toast.makeText(Signin.this, s, Toast.LENGTH_SHORT).show();
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
                                JSONObject json = new JSONObject(mMessage);


//                                Toast.makeText(BankTransfer.this, json.getString("message").toString(), Toast.LENGTH_SHORT).show();


                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), json.getString("message").toString(), Snackbar.LENGTH_LONG);
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.homemenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // getMenuInflater().inflate(R.menu.homemenu, (Menu) item);
        int id = item.getItemId();


        switch (item.getItemId()) {
//            case R.id.logout:
//
//                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
//                // User chose the "Favorite" action, mark the current item
//                // as a favorite...
//                return true;

            case R.id.action_item_one:

                Intent intent = new Intent(BankTransfer.this, HomeScreen.class);
                startActivity(intent);
                // Do something
                return true;
            case R.id.action_item_two:

                Intent intent2 = new Intent(BankTransfer.this, WalletTransactions.class);
                startActivity(intent2);
                // Do something
                return true;
            case R.id.action_item_three:

                Logout();
                // Do something
                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);


        }

        // If we got here, the user's action was not recognized.
        // Invoke the superclass to handle it.


    }
    private void Logout() {

        pd = new ProgressDialog(BankTransfer.this);
        pd.setMessage("Logging You Out");
        pd.setCancelable(false);
        pd.show();
        final OkHttpClient client = new OkHttpClient();
        JSONObject postdata = new JSONObject();


        final Request request = new Request.Builder()
                .url("http://game-dev.techmech.men:8080/api/user/logout")
                .get()
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization",pass)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                pd.dismiss();
                pd.cancel();
                String mMessage = e.getMessage().toString();
                Log.w("failure Response", mMessage);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                pd.dismiss();
                pd.cancel();
                final String mMessage = response.body().string();
                if (response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject json = new JSONObject(mMessage);

                                String s = json.getString("message");


     //                           Toast.makeText(BankTransfer.this, s, Toast.LENGTH_SHORT).show();


                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), s, Snackbar.LENGTH_LONG);
                                View snackBarView = snackbar.getView();
                                snackBarView.setBackgroundColor(Color.parseColor("#FF9800"));
                                TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
                                textView.setTextColor(Color.WHITE);
                                snackbar.show();
                                sp = getSharedPreferences("login", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.clear();
                                editor.commit();
                                finish();

                                Intent in = new Intent(BankTransfer.this,FirstPage.class);
                                startActivity(in);

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
                                String status = json.getString("status");
                                String message = json.getString("message");
                                //title = name;

                                if (status.equalsIgnoreCase("401")){


                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.clear();
                                    editor.commit();


                                    Toast.makeText(BankTransfer.this, message, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(BankTransfer.this,Signin.class);
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

