package com.androidhari.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.androidhari.tambola.FirstPage;
import com.androidhari.tambola.HomeScreen;
import com.androidhari.tambola.Wallet;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import ua.naiksoftware.tambola.R;

public class BankTransfer extends AppCompatActivity {

    SharedPreferences sp;
    ProgressDialog pd;
    String pass, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bank_transfer);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        sp = getSharedPreferences("login", MODE_PRIVATE);
        pass = sp.getString("token", null);


//        ActionBar ab = getSupportActionBar();
//        // Enable the Up button
//        ab.setDisplayHomeAsUpEnabled(true);
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

                Intent intent2 = new Intent(BankTransfer.this, Wallet.class);
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


                                Toast.makeText(BankTransfer.this, s, Toast.LENGTH_SHORT).show();

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
                            pd.cancel();
                            pd.dismiss();
                            Toast.makeText(BankTransfer.this, "Fail", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
    }



}

