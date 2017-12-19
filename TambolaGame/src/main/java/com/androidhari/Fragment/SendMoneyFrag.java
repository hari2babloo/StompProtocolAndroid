package com.androidhari.Fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidhari.ViewPager.WalletTransactions;
import com.androidhari.tambola.HomeScreen;
import com.androidhari.tambola.Signin;

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

import static android.content.Context.MODE_PRIVATE;

public class SendMoneyFrag extends Fragment {

    View view;
    ProgressDialog pd;
    TextView balance;
    EditText mobile,amount;
    SharedPreferences sp;
    Button send;
    String pass;
    public static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json");
    public SendMoneyFrag() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.sendmoneyfrag, container, false);

        balance = (TextView)view.findViewById(R.id.balance);
        sp= this.getActivity().getSharedPreferences("login",MODE_PRIVATE);
        pass=sp.getString("token",null);

        mobile = (EditText)view.findViewById(R.id.mobile);
        amount = (EditText)view.findViewById(R.id.amount);
        send =(Button)view.findViewById(R.id.send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendmoney();
            }
        });


        GetBalance();
        // Inflate the layout for this fragment
        return view;
    }

    private void sendmoney() {

        pd = new ProgressDialog(getContext());
        pd.setMessage("Sending Money..");
        pd.setCancelable(false);
        pd.show();

        final OkHttpClient client = new OkHttpClient();
        JSONObject postdata = new JSONObject();
        try {
            //  postdata.put("money", money.getText().toString());
            postdata.put("money", amount.getText().toString());
            postdata.put("phoneNumber", mobile.getText().toString());

            Log.e("Postdata",amount.getText().toString() + mobile.getText().toString());

        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MEDIA_TYPE,
                postdata.toString());


        Log.d("data", String.valueOf(body));

        final Request request = new Request.Builder()
                .url("http://game-dev.techmech.men:8080/api/wallet/transfer")
                .post(body)
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

                final String mMessage = response.body().string();
                Log.w("Response", mMessage);
                pd.cancel();
                pd.dismiss();
                if (response.isSuccessful()){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject json = new JSONObject(mMessage);
                                String s = json.getString("message");

                               Log.e("Result", s);

                                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                                Intent in = new Intent(getContext(),WalletTransactions.class);
                                startActivity(in);
//
//                                balance.setText("  Your Wallet Balance Rs.  "+  roundOff);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
//                            Toast.makeText(SigninForm.this, "Success", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                else {

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject json = new JSONObject(mMessage);
                                String status = json.getString("status");
                                String message = json.getString("message");
                                //title = name;

                                if (status.equalsIgnoreCase("401")){


                                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getContext(),Signin.class);
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

    private void GetBalance() {

        pd = new ProgressDialog(getContext());
        pd.setMessage("Getting Your Wallet Balance");
        pd.setCancelable(false);
        pd.show();


        final OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url("http://game-dev.techmech.men:8080/api/wallet")
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

                final String mMessage = response.body().string();
                Log.w("Response", mMessage);
                pd.cancel();
                pd.dismiss();
                if (response.isSuccessful()){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject json = new JSONObject(mMessage);
                                String s = json.getJSONObject("data").getString("money");
                                Double value = Double.parseDouble(s);
                                double roundOff = (double) Math.round(value * 100) / 100;

                                Log.e("MOney",s);

                                balance.setText("  Your Wallet Balance Rs.  "+  roundOff);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
//                            Toast.makeText(SigninForm.this, "Success", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                else {

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject json = new JSONObject(mMessage);
                                String status = json.getString("status");
                                String message = json.getString("message");
                                //title = name;

                                if (status.equalsIgnoreCase("401")){


                                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getContext(),Signin.class);
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

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//            inflater.inflate(R.menu.homemenu, menu);
//            super.onCreateOptionsMenu(menu, inflater);
//    }

}
