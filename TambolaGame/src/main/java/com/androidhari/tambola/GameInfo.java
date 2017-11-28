package com.androidhari.tambola;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
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
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ua.naiksoftware.tambola.R;

public class GameInfo extends AppCompatActivity {


    public static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json");



    Button buytickets;
    TextView gname,gtime,tcost,pmoney,createdby,passcode;
    ListView plist;
    ProgressDialog pd;


    ArrayList<prizes> dataModels = new ArrayList<>();

    ArrayList<prizes> list = new ArrayList<>();

    SharedPreferences sp;
    String pass,gid,gamestarttime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_info);
        sp=getSharedPreferences("login",MODE_PRIVATE);
        pass=sp.getString("token",null);
        gid=sp.getString("id",null);
        buytickets = (Button)findViewById(R.id.buyticket);
        buytickets.setEnabled(false);
        updateview();
    }

    private void updateview() {
        gname = (TextView)findViewById(R.id.gname);
        gtime = (TextView)findViewById(R.id.gtime);
        tcost = (TextView)findViewById(R.id.tcost);
        pmoney = (TextView)findViewById(R.id.Pmoney);

        plist = (ListView)findViewById(R.id.prizelist);
        filldata();
    }

    private void filldata() {

        pd = new ProgressDialog(GameInfo.this);
        pd.setMessage("Getting Game Information");
        pd.setCancelable(false);
        pd.show();
        final OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url("http://game-dev.techmech.men:8080/api/game/"+gid)
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

                pd.dismiss();
                pd.cancel();

                Log.w("Response", mMessage);
                if (response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                JSONObject json = new JSONObject(mMessage);
                                Typeface face = Typeface.createFromAsset(getAssets(),
                                        "fonts/segoeuil.ttf");
                                gname.setText("Game Name: " + json.getJSONObject("data").getString("name"));

                                gname.setTypeface(face);
                                String longV = json.getJSONObject("data").getString("startTime");
                                long millisecond = Long.parseLong(longV);
                                // or you already have long value of date, use this instead of milliseconds variable.


                                gamestarttime = DateFormat.format("dd/MM/yyyy hh:mm a", new Date(millisecond)).toString();




                                gtime.setTypeface(face);
                                gtime.setText("Date & Start Time : "+gamestarttime);
                                tcost.setText("Ticket Cost : " +json.getJSONObject("data").getString("ticketCost"));
                                tcost.setTypeface(face);
                                pmoney.setText("Prize Money : "+json.getJSONObject("data").getString("prizeMoney"));

                                pmoney.setTypeface(face);


                                JSONObject json2 = json.getJSONObject("data");
                                JSONArray jsonArray = json2.getJSONArray("prizes");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject json_data = jsonArray.getJSONObject(i);

                                    buytickets.setEnabled(true);

                                    buytickets.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            SharedPreferences.Editor e = sp.edit();
                                            e.putString("gid",gid);
                                            e.putString("gstime",gamestarttime);

                                            e.commit();

                                            Intent in = new Intent(GameInfo.this,PurchaseTicket.class);


                                            startActivity(in);
                                        }
                                    });


                                    dataModels.add(new prizes(json_data.getString("prizeName"),json_data.getString("prizeCost")));
//                                    dataModels.add(new prizes("Hello","Hello"));
//                                    dataModels.add(new prizes("Hello","Hello"));
//                                    dataModels.add(new prizes("Hello","Hello"));
//                                    dataModels.add(new prizes("Hello","Hello"));
//                                    dataModels.add(new prizes("Hello","Hello"));
//                                    dataModels.add(new prizes("Hello","Hello"));
//                                    dataModels.add(new prizes("Hello","Hello"));

                                    Log.e("sdfdsf", gid);


                                }



                                CustomAdapter   adapter= new CustomAdapter(dataModels,getApplicationContext());

                                plist.setAdapter(adapter);


                                //                              ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);

                                String s = json.getJSONObject("data").getString("name");
 //                               Toast.makeText(GameInfo.this, s, Toast.LENGTH_SHORT).show();

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

                            pd.dismiss();
                            pd.cancel();
                            Toast.makeText(GameInfo.this, "Fail", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
    }

    public class prizes{

        String pname;
        String pcost;


        public prizes(String pname, String pcost) {
            this.pname = pname;
            this.pcost = pcost;
        }

        public String getPname() {

            return pname;
        }

        public void setPname(String pname) {
            this.pname = pname;
        }

        public String getPcost() {
            return pcost;
        }

        public void setPcost(String pcost) {
            this.pcost = pcost;
        }
    }

    public class CustomAdapter extends ArrayAdapter<prizes>{

        private ArrayList<prizes> dataSet;
        Context mContext;

        // View lookup cache
        private  class ViewHolder {

            TextView txtType;
            TextView txtVersion;

        }

        public CustomAdapter(ArrayList<prizes> data, Context context) {
            super(context, R.layout.row_item, data);
            this.dataSet = data;
            this.mContext=context;

        }



        private int lastPosition = -1;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            prizes dataModel = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            ViewHolder viewHolder; // view lookup cache stored in tag

            final View result;

            if (convertView == null) {

                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.row_item, parent, false);

                viewHolder.txtType = (TextView) convertView.findViewById(R.id.type);

                viewHolder.txtVersion = (TextView) convertView.findViewById(R.id.version_number);


                result=convertView;

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
                result=convertView;
            }

//            Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
//            result.startAnimation(animation);
            lastPosition = position;

            //          viewHolder.txtName.setText(dataModel.getPcost());
            viewHolder.txtType.setText(dataModel.getPname());
            Typeface face = Typeface.createFromAsset(getAssets(),
                    "fonts/segoeuil.ttf");
            viewHolder.txtType.setTypeface(face);
            viewHolder.txtVersion.setText(dataModel.getPcost());
            viewHolder.txtVersion.setTypeface(face);
//            viewHolder.info.setOnClickListener(this);
//            viewHolder.info.setTag(position);
            // Return the completed view to render on screen
            return convertView;
        }
    }
}
