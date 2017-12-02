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
import android.widget.Toast;

import com.androidhari.tambola.HomeScreen;
import com.androidhari.tambola.Signin;
import com.androidhari.tambola.Wallet;

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


/**
 * A simple {@link Fragment} subclass.
 */
public class AddMoneyFrag extends Fragment {




    public static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json");

    String pass;
    View view;
    ProgressDialog pd;
    SharedPreferences sp;
    Button add;
    EditText money,voucher;
    public AddMoneyFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sp= this.getActivity().getSharedPreferences("login",MODE_PRIVATE);
        pass=sp.getString("token",null);


        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.addmoneyfrag, container, false);
        money = (EditText) view.findViewById(R.id.addmoney);
        voucher = (EditText) view.findViewById(R.id.voucher);
        add = (Button)view.findViewById(R.id.add);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addmoney();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }



    private void addmoney() {

        pd = new ProgressDialog(getContext());
        pd.setMessage("Adding Money to Your Wallet");
        pd.setCancelable(false);
        pd.show();


        final OkHttpClient client = new OkHttpClient();
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("money", money.getText().toString());
            postdata.put("voucherCode", voucher.getText().toString());
        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MEDIA_TYPE,
                postdata.toString());


        final Request request = new Request.Builder()
                .url("http://game-dev.techmech.men:8080/api/wallet")
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

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

Intent in = new Intent(getContext(), Wallet.class);

                            startActivity(in);
                            Toast.makeText(getContext(), "Money Added Successfully", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

                else {

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            try {
                                JSONObject json = new JSONObject(mMessage);

                                String s = json.getString("message");
                                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
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