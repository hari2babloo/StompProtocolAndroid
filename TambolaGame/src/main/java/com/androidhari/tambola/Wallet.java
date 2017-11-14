package com.androidhari.tambola;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

    Button purchasehistory;

    public static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json");


    TextView money;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet);

        money = (TextView)findViewById(R.id.money);

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
        final OkHttpClient client = new OkHttpClient();
        JSONObject postdata = new JSONObject();
        RequestBody body = RequestBody.create(MEDIA_TYPE,
                postdata.toString());
        final Request request = new Request.Builder()
                .url("http://game-dev.techmech.men:8080/api/wallet")
                .get()
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization","eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmNAYWJjLmNvbSIsImF1ZGllbmNlIjoid2ViIiwiY3JlYXRlZCI6MTUxMDIwMjM0NDg1NywiZXhwIjoxNTEwODA3MTQ0fQ.Nxm8omG7rrcwnUyQr2qfwsx6VsRmNfu-FtsTTGb66mUuM8F1uQ5g1MPztIXL6k1wHkpzMHEWhKKf-wf5PN_LWQ")                .build();
        Log.e("dasdasd", body.toString());
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                String mMessage = e.getMessage().toString();
                Log.w("failure Response", mMessage);

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String mMessage = response.body().string();
                Log.w("Response", mMessage);
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
                            Toast.makeText(Wallet.this, "Fail", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
    }
}




