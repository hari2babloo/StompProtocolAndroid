package com.androidhari.tambola;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class WalletPurchasehistory extends AppCompatActivity {

    ArrayList<prizes> dataModels = new ArrayList<>();
    ListView plist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_purchasehistory);

        plist = (ListView)findViewById(R.id.prizelist);

        getdata();
    }

    private void getdata() {

        final OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url("http://game-dev.techmech.men:8080/api/game/user/games")
                .get()
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization","eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJyYWplc2gua29tYmF0aHVsYUBnbWFpbC5jb20iLCJhdWRpZW5jZSI6IndlYiIsImNyZWF0ZWQiOjE1MTAzMDg3NDc5NDgsImV4cCI6MTUxMDkxMzU0N30.cM4HMOE2yoMO78PF5sHstSEYOlME647R-cXiW3FF5TvCkdXx80sej3VfgPgxdtaIPbE4bgI_6MYWqPJ6ZVugnQ")

                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                String mMessage = e.getMessage().toString();
                Log.w("failure Response", mMessage);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String mMessage = response.body().string();
                //               Log.w("Response", mMessage);
                if (response.isSuccessful()){

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject json = new JSONObject(mMessage);
//                        JSONObject json2 = json.getJSONObject("data");
                                JSONArray jsonArray = json.getJSONArray("data");


                                //                       Log.e("data", String.valueOf(jsonArray));
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject json_data = jsonArray.getJSONObject(i).getJSONObject("game");
//                                    JSONObject json_data2 = jsonArray.getJSONObject(i);
                                    String status = json_data.getString("status");
                                    Log.e("status",status);
                                    String getname = json_data.getString("name");


                                    String longV = json_data.getString("startTime");
                                    long millisecond = Long.parseLong(longV);
                                    // or you already have long value of date, use this instead of milliseconds variable.
                                    String starttime = DateFormat.format("dd/MM/yyyy HH:mm", new Date(millisecond)).toString();
                                    Log.e("startime",starttime);
                                    String gno = json_data.getString("id");

                                    Log.e("fdsfsdf",getname+starttime+gno);
                                    dataModels.add(new prizes(gno,getname,starttime,status));
                                }
                                CustomAdapter adapter= new CustomAdapter(dataModels,getApplicationContext());

                                plist.setAdapter(adapter);

                                Log.e("datamodels", String.valueOf(dataModels));
                                //                              ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);

                                //                              String s = json.getJSONObject("data").getString("name");
                                //                             Toast.makeText(WalletPurchasehistory.this, s, Toast.LENGTH_SHORT).show();


                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });





                }

                else {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(WalletPurchasehistory.this, "Fail", Toast.LENGTH_SHORT).show();
                        }
                    });
                }


            }


        });
    }


    public class prizes{

        String gno;
        String gname;
        String gstime;
        String status;

        public prizes(String gno, String gname, String gstime, String status) {
            this.gno = gno;
            this.gname = gname;
            this.gstime = gstime;
            this.status = status;
        }

        public String getGno() {
            return gno;
        }

            public void setGno(String gno) {
            this.gno = gno;
        }

        public String getGname() {
            return gname;
        }

        public void setGname(String gname) {
            this.gname = gname;
        }

        public String getGstime() {
            return gstime;
        }

        public void setGstime(String gstime) {
            this.gstime = gstime;
        }

    }

    public class CustomAdapter extends ArrayAdapter<prizes> implements View.OnClickListener{

        private ArrayList<prizes> dataSet;
        Context mContext;

        // View lookup cache
        private  class ViewHolder {
            TextView gno;
            TextView gname;
            TextView gstime;
            Button status;
            ImageView img;
        }

        public CustomAdapter(ArrayList<prizes> data, Context context) {
            super(context, R.layout.tablist, data);
            this.dataSet = data;
            this.mContext=context;

        }

        @Override
        public void onClick(View v) {

            int position=(Integer) v.getTag();
            Object object= getItem(position);
            prizes dataModel=(prizes) object;

            switch (v.getId())
            {
                case R.id.item_info:
                    Snackbar.make(v, "Release date " +dataModel.getGname(), Snackbar.LENGTH_LONG)
                            .setAction("No action", null).show();
                    break;
                case R.id.status:
                    Toast.makeText(mContext, "Hllo", Toast.LENGTH_SHORT).show();

            }
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
                convertView = inflater.inflate(R.layout.tablist, parent, false);
                viewHolder.gno = (TextView) convertView.findViewById(R.id.gno);
                viewHolder.gname = (TextView) convertView.findViewById(R.id.gname);
                viewHolder.gstime = (TextView) convertView.findViewById(R.id.gstime);
                viewHolder.status = (Button)convertView.findViewById(R.id.status);
                viewHolder.img = (ImageView) convertView.findViewById(R.id.img);

                //       viewHolder.info = (ImageView) convertView.findViewById(R.id.item_info);

                result=convertView;

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (CustomAdapter.ViewHolder) convertView.getTag();
                result=convertView;
            }

//            Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
//            result.startAnimation(animation);
            lastPosition = position;

            //          viewHolder.txtName.setText(dataModel.getPcost());
            viewHolder.gno.setText("Game Number:"+dataModel.getGno());
            viewHolder.gname.setText("Game name:"+dataModel.getGname());

            viewHolder.gstime.setText("Start Time: " +dataModel.getGstime());

            if (dataModel.status.equalsIgnoreCase("NEW")){

                viewHolder.status.setText("PLAY GAME");
                viewHolder.status.setEnabled(true);
            }
            else {

                viewHolder.status.setText("FINISHED GAME");
                viewHolder.status.setEnabled(false);
            }
            //  viewHolder.status.setText(dataModel.status);

            viewHolder.status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(mContext, Countdown.class);
                    startActivity(in);
                }
            });
            viewHolder.img.setImageResource(R.drawable.img);
//            viewHolder.info.setOnClickListener(this);
//            viewHolder.info.setTag(position);
            // Return the completed view to render on screen
            return convertView;
        }
    }
}
