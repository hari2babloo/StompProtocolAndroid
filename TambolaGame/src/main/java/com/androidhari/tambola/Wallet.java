package com.androidhari.tambola;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

public class Wallet extends AppCompatActivity {

    Button purchasehistory,history,addmoney;

    public static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json");

    ProgressDialog pd;
    SharedPreferences sp;
    String pass;
    TextView money;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        sp=getSharedPreferences("login",MODE_PRIVATE);
        pass=sp.getString("token",null);
        money = (TextView)findViewById(R.id.money);
        history = (Button)findViewById(R.id.hISTORY);


        addmoney =(Button)findViewById(R.id.addmoney);

        addmoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Wallet.this, AddMoney.class);
                startActivity(intent);
            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Wallet.this, TransactionHistory.class);
                startActivity(intent);
            }
        });

        purchasehistory = (Button)findViewById(R.id.purchasehistory);

        purchasehistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Wallet.this, WalletPurchasehistory.class);
                startActivity(intent);
            }
        });


//
//        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
//        tabLayout.addTab(tabLayout.newTab().setText("Purchases Games"));
//        tabLayout.addTab(tabLayout.newTab().setText("Created Games"));
//        tabLayout.addTab(tabLayout.newTab().setText("My History"));
//        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        GetBalance();

//        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
//        final PagerAdapter adapter = new PagerAdapter
//                (getSupportFragmentManager(), tabLayout.getTabCount());
//        viewPager.setAdapter(adapter);
//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                viewPager.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
    }

    private void GetBalance() {
        pd = new ProgressDialog(Wallet.this);
        pd.setMessage("Getting Your Wallet Balance");
        pd.setCancelable(false);
        pd.show();


        final OkHttpClient client = new OkHttpClient();
        JSONObject postdata = new JSONObject();
        RequestBody body = RequestBody.create(MEDIA_TYPE,
                postdata.toString());
        final Request request = new Request.Builder()
                .url("http://game-dev.techmech.men:8080/api/wallet")
                .get()
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization",pass)
                .build();
        Log.e("dasdasd", body.toString());
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

                final String mMessage = response.body().string();
                Log.w("Response", mMessage);
                pd.cancel();
                pd.dismiss();
                if (response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject json = new JSONObject(mMessage);
                                String s = json.getJSONObject("data").getString("money");
                                Log.e("MOney",s);

                                money.setText("Rs  "+  s);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
//                            Toast.makeText(SigninForm.this, "Success", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                else {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pd.cancel();
                            pd.dismiss();
                            Toast.makeText(Wallet.this, "Fail", Toast.LENGTH_SHORT).show();
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

            case R.id.action_item_two:

                Intent intent = new Intent(Wallet.this, Wallet.class);
                startActivity(intent);
                // Do something
                return true;
            case R.id.action_item_one:

                Intent intent2 = new Intent(Wallet.this, HomeScreen.class);
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

    }

    private void Logout() {

        pd = new ProgressDialog(Wallet.this);
        pd.setMessage("Logging You Out");
        pd.setCancelable(false);
        pd.show();
        final OkHttpClient client = new OkHttpClient();
        JSONObject postdata = new JSONObject();

        RequestBody body = RequestBody.create(MEDIA_TYPE,
                postdata.toString());
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


                                Toast.makeText(Wallet.this, s, Toast.LENGTH_SHORT).show();

                                sp = getSharedPreferences("login", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.clear();
                                editor.commit();
                                finish();

                                Intent in = new Intent(Wallet.this,FirstPage.class);
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
                            Toast.makeText(Wallet.this, "Fail", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
    }
}





