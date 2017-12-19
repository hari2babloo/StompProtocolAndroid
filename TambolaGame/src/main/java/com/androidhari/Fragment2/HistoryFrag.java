package com.androidhari.Fragment2;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidhari.Fragment.AddMoneyFrag;
import com.androidhari.tambola.GameInfo;
import com.androidhari.tambola.Signin;
import com.androidhari.tambola.TransactionHistory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ua.naiksoftware.tambola.R;

import static android.content.Context.MODE_PRIVATE;

public class HistoryFrag extends Fragment {

    View view;

    SharedPreferences sp;
    String pass;
    ListView plist;
    ArrayList<prizes> dataModels = new ArrayList<>();

    ProgressDialog pd;

    public HistoryFrag() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sp = this.getActivity().getSharedPreferences("login", MODE_PRIVATE);
        pass = sp.getString("token", null);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.historyfrag, container, false);
        plist = (ListView) view.findViewById(R.id.prizelist);

        filldata();

        // Inflate the layout for this fragment
        return view;
    }

    private void filldata() {


        pd = new ProgressDialog(getContext());
        pd.setMessage("Getting Purchases");
        pd.setCancelable(false);
        pd.show();


        final OkHttpClient client = new OkHttpClient();


        final Request request = new Request.Builder()
                .url("http://game-dev.techmech.men:8080/api/wallet/transactions")
                .get()
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", pass)
                .build();

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
                if (response.isSuccessful()) {

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject json = new JSONObject(mMessage);


                                JSONArray jsonArray = json.getJSONArray("data");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject json_data = jsonArray.getJSONObject(i);
                                    String s = json_data.getString("id");


                                    String longV = json_data.getString("transactionTime");
                                    long millisecond = Long.parseLong(longV);
                                    // or you already have long value of date, use this instead of milliseconds variable.
                                    String dateString = DateFormat.format("dd/MM/yyyy HH:mm:ss", new Date(millisecond)).toString();
                                    Log.d("s", s);
                                    dataModels.add(new prizes(json_data.getString("description"), json_data.getString("gameId"),
                                            json_data.getString("id"),
                                            json_data.getString("transactionAmount"),
                                            dateString,
                                            json_data.getString("transactionType"),
                                            json_data.getString("userId")));
                                    Log.e("sdfdsf", s);


                                }
                                CustomAdapter adapter = new CustomAdapter(dataModels, getContext());

                                plist.setAdapter(adapter);


                                //                              ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);

                                String s = json.getJSONObject("data").getString("name");
                                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });

                } else {

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {

                                pd.cancel();
                                pd.dismiss();
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


    public class prizes {

        String description;
        String gameId;
        String id;
        String transactionAmount;
        String transactionTime;
        String transactionType;
        String userId;

        public prizes(String description, String gameId, String id, String transactionAmount, String transactionTime, String transactionType, String userId) {
            this.description = description;
            this.gameId = gameId;
            this.id = id;
            this.transactionAmount = transactionAmount;
            this.transactionTime = transactionTime;
            this.transactionType = transactionType;
            this.userId = userId;
        }
    }

    public class CustomAdapter extends ArrayAdapter<prizes> implements View.OnClickListener {

        private ArrayList<prizes> dataSet;
        Context mContext;

        // View lookup cache
        private class ViewHolder {
            TextView id;
            TextView transactionType;
            TextView transactionAmount;
            TextView transactionTime;
            TextView gameId;
            TextView description;
        }

        public CustomAdapter(ArrayList<prizes> data, Context context) {
            super(context, R.layout.transactiontemplate, data);
            this.dataSet = data;
            this.mContext = context;

        }

        @Override
        public void onClick(View v) {

            int position = (Integer) v.getTag();
            Object object = getItem(position);
            prizes dataModel = (prizes) object;


        }

        private int lastPosition = -1;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            prizes dataModel = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            CustomAdapter.ViewHolder viewHolder; // view lookup cache stored in tag
            final View result;
            if (convertView == null) {

                viewHolder = new CustomAdapter.ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.transactiontemplate, parent, false);
                viewHolder.id = (TextView) convertView.findViewById(R.id.id);
                viewHolder.transactionType = (TextView) convertView.findViewById(R.id.transactionType);
                viewHolder.transactionAmount = (TextView) convertView.findViewById(R.id.transactionAmount);
                viewHolder.transactionTime = (TextView) convertView.findViewById(R.id.transactionTime);
                //   viewHolder.gameId = (TextView) convertView.findViewById(R.id.gameId);
                viewHolder.description = (TextView) convertView.findViewById(R.id.description);

                result = convertView;

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (CustomAdapter.ViewHolder) convertView.getTag();
                result = convertView;
            }

//            Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
//            result.startAnimation(animation);
            lastPosition = position;

            //          viewHolder.txtName.setText(dataModel.getPcost());
            viewHolder.id.setText("ID: " + dataModel.id);
            viewHolder.transactionType.setText("Type: " + dataModel.transactionType);


            try {

                if (dataModel.transactionType.equalsIgnoreCase("DEBIT")){
                    viewHolder.transactionAmount.setText("Rs: " + dataModel.transactionAmount);
                    viewHolder.transactionType.setText(dataModel.transactionType);
                    viewHolder.transactionType.setTextColor(Color.RED);


                }

                else {

                    if (dataModel.transactionType.equalsIgnoreCase("CREDIT")){
                        viewHolder.transactionAmount.setText("Rs: " + dataModel.transactionAmount);
                        viewHolder.transactionType.setText(dataModel.transactionType);
                        viewHolder.transactionType.setTextColor(Color.GREEN);


                    }
                }


//                double value = Double.parseDouble(dataModel.transactionAmount);
//                if (value < 0) {
//                    viewHolder.transactionAmount.setText("Rs: " + dataModel.transactionAmount);
//                    viewHolder.transactionAmount.setTextColor(Color.RED);
//                    //  System.out.println(value + " is negative");
//                } else {
//                    viewHolder.transactionAmount.setText("Rs: +" + dataModel.transactionAmount);
//                    viewHolder.transactionAmount.setTextColor(Color.GREEN);
//                    //   System.out.println(value + " is possitive");
//
//                }
            } catch (NumberFormatException e) {
                System.out.println("String " + "is not a number");
            }

            viewHolder.transactionTime.setText(dataModel.transactionTime);

            viewHolder.description.setText(dataModel.description);

//            viewHolder.info.setOnClickListener(this);
//            viewHolder.info.setTag(position);
            // Return the completed view to render on screen
            return convertView;
        }
    }
}