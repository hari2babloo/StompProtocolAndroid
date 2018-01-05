package com.androidhari.Fragment;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.androidhari.tambola.HomeScreen;
import com.androidhari.tambola.Signin;
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

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class CashoutFrag extends Fragment {


    View view;

    RadioGroup radioGroup;
    SharedPreferences sp;

    ProgressDialog pd;

    String pass,gameid,amt,balanceamt;
    Button next;
    EditText amount;
    TextView bal;
    public CashoutFrag() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.cashoutfrag, container, false);
        sp=getContext().getSharedPreferences("login",MODE_PRIVATE);
        pass=sp.getString("token",null);

        gameid=sp.getString("gno",null);

        radioGroup = (RadioGroup) view.findViewById(R.id.radiogrp);
        radioGroup.clearCheck();
        next = (Button) view.findViewById(R.id.next);
        amount = (EditText)view.findViewById(R.id.amount);

        bal = (TextView)view.findViewById(R.id.bal);
        GetBalance();
        next.setOnClickListener(new View.OnClickListener() {
            @Override


            public void onClick(View v) {

                if(TextUtils.isEmpty(amount.getText().toString()))
                {
//                    Toast.makeText(getContext(), " Please Enter Amount", Toast.LENGTH_SHORT).show();
                    Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), "Please Enter Amount", Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#FF9800"));
                    TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();

                    return;
                }


                double bal = Double.parseDouble(balanceamt);


                double enteredamt = Double.parseDouble(amount.getText().toString());



                if (bal-enteredamt>100){


                    goahead();
                }
                else if (enteredamt>bal || enteredamt==0 || enteredamt<0){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                    builder1.setMessage("Sufficient Funds Not Available");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });



                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                }

                else {

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                    builder1.setMessage("Minimum Wallet balance should be 100 else your account will be deactivated, do you want to continue?");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    goahead();
                                }
                            });

                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
            }
        });

        return view;


    }

    private void goahead() {



        int selectedId = radioGroup.getCheckedRadioButtonId();


        Log.e("radio", String.valueOf(selectedId));

        // find the radiobutton by returned id

        RadioButton ra = (RadioButton) view.findViewById(selectedId);

        //  Toast.makeText(getContext(), ra.getText(), Toast.LENGTH_SHORT).show();

if (radioGroup.getCheckedRadioButtonId()==-1){

  //  Toast.makeText(getContext(), "Select Bank", Toast.LENGTH_SHORT).show();
    Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), "Select Bank", Snackbar.LENGTH_LONG);
    View snackBarView = snackbar.getView();
    snackBarView.setBackgroundColor(Color.parseColor("#FF9800"));
    TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
    textView.setTextColor(Color.WHITE);
    snackbar.show();
    radioGroup.clearCheck();

}
else

        if (ra.getText().toString().equalsIgnoreCase("Paytm")){

            SharedPreferences.Editor e = sp.edit();
            e.putString("amt",amount.getText().toString());


            e.commit();
            Intent inte = new Intent(getContext(),PaytmTransfer.class);
            startActivity(inte);
        }

        else if (ra.getText().toString().equalsIgnoreCase("Bank Account")){

            SharedPreferences.Editor e = sp.edit();
            e.putString("amt",amount.getText().toString());


            e.commit();
            Intent inte = new Intent(getContext(),BankTransfer.class);
            startActivity(inte);

        }
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
                                balanceamt = json.getJSONObject("data").getString("money");


                                Log.e("MOney",balanceamt);

                                bal.setText("  Your Wallet Balance Rs.  "+  balanceamt);

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

                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.clear();
                                    editor.commit();


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


}
