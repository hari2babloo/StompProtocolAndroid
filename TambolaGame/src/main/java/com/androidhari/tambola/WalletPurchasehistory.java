package com.androidhari.tambola;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.icu.text.SimpleDateFormat;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import ua.naiksoftware.tambola.MainActivity;
import ua.naiksoftware.tambola.R;

public class WalletPurchasehistory extends AppCompatActivity {

    ArrayList<prizes> dataModels = new ArrayList<>();
    ListView plist;


    ProgressDialog pd;
    public static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json");
    SharedPreferences sp;
    String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.wallet_purchasehistory);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        sp=getSharedPreferences("login",MODE_PRIVATE);
        pass=sp.getString("token",null);
        plist = (ListView)findViewById(R.id.prizelist);

        getdata();
    }

    private void getdata() {
        pd = new ProgressDialog(WalletPurchasehistory.this);
        pd.setMessage("Getting your Games");
        pd.setCancelable(false);
        pd.show();

        final OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url("http://game-dev.techmech.men:8080/api/game/user/games")
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
                //               Log.w("Response", mMessage);
                if (response.isSuccessful()){

                    pd.dismiss();
                    pd.cancel();
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
//                                    Log.e("status",status);
                                    String getname = json_data.getString("name");


                                    String longV = json_data.getString("startTime");
                                    long millisecond = Long.parseLong(longV);
                                    // or you already have long value of date, use this instead of milliseconds variable.
                                    String starttime = DateFormat.format("dd/MM/yyyy hh:mm:ss a", new Date(millisecond)).toString();
 //                                   Log.e("startime",starttime);
                                    String gno = json_data.getString("id");

  //                                  Log.e("fdsfsdf",getname+starttime+gno);
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
                            pd.dismiss();
                            pd.cancel();
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
//                case R.id.item_info:
//                    Snackbar.make(v, "Release date " +dataModel.getGname(), Snackbar.LENGTH_LONG)
//                            .setAction("No action", null).show();
//                    break;
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

                //       viewHolder.info = (ImageView) convertView.findViewBy-Id(R.id.item_info);

                result=convertView;

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (CustomAdapter.ViewHolder) convertView.getTag();
                result=convertView;
            }

            Typeface face = Typeface.createFromAsset(getAssets(),
                    "fonts/segoeuil.ttf");

//            Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
//            result.startAnimation(animation);
            lastPosition = position;

            //          viewHolder.txtName.setText(dataModel.getPcost());
            viewHolder.gno.setText("Game Number:"+dataModel.getGno());
            viewHolder.gno.setTypeface(face);
            viewHolder.gname.setText("Game Name:"+dataModel.getGname());
            viewHolder.gname.setTypeface(face);
            viewHolder.gstime.setText("Start Time:" +dataModel.getGstime());
            viewHolder.gstime.setTypeface(face);

            if (dataModel.status.equalsIgnoreCase("COMPLETED")){

                viewHolder.status.setText("FINISHED");
                viewHolder.status.setEnabled(false);
                viewHolder.status.setBackgroundColor(Color.RED);

            }
            else {

                viewHolder.status.setText("PLAY");
                viewHolder.status.setBackgroundColor(Color.parseColor("#FFFF8800"));
                viewHolder.status.setEnabled(true);
            }
            //  viewHolder.status.setText(dataModel.status);

            viewHolder.status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    SharedPreferences.Editor e = sp.edit();
                    e.putString("gno",dataModel.getGno());
                    e.putString("gstime",dataModel.getGstime());
                    e.commit();


                    Date date = new Date() ;
                    java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd/MM/yyyy hh:mm:ss a") ;
                    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+05:30"));
                    dateFormat.format(date);
                    System.out.println(dateFormat.format(date));
                    System.out.println(dataModel.getGstime());

                    try {
                        if(dateFormat.parse(dateFormat.format(date)).after(dateFormat.parse(dataModel.getGstime())))
                        {
                            Intent in = new Intent(mContext, MainActivity.class);
                            startActivity(in);

                            System.out.println("Current time is greater than 12.07");
                        }else{
                            Intent in = new Intent(mContext, Countdown.class);
                            startActivity(in);
                            System.out.println("Current time is less than 12.07");
                        }
                    } catch (ParseException X) {
                        X.printStackTrace();
                    }

//                    try{
//
//                        SimpleDateFormat formatter = new SimpleDateFormat("dd/M/yyyy hh:mm:ss a");
//
//
//                        String str1 = dataModel.getGstime();
//
//
//                        String str2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a").format(new Date());
                       // String str2 = "13/10/2013";
//                        Date date = new Date() ;
//                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a") ;
//                        dateFormat.format(date);
//
//
//                        if(dateFormat.parse(dateFormat.format(date)).after(dateFormat.parse(dataModel.getGstime())))
//                        {
//
//
//                            System.out.println("Current time is greater than 12.07");
//                        }else{
//
//
//                            System.out.println("Current time is less than 12.07");
//                        }
//
//                    }catch (ParseException e1){
//                        e1.printStackTrace();
//                    }

                    Toast.makeText(mContext, dataModel.getGstime(), Toast.LENGTH_SHORT).show();

                }
            });
            viewHolder.img.setImageResource(R.drawable.img);
            return convertView;
        }
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

                Intent intent = new Intent(WalletPurchasehistory.this, Wallet.class);
                startActivity(intent);
                // Do something
                return true;
            case R.id.action_item_one:

                Intent intent2 = new Intent(WalletPurchasehistory.this, HomeScreen.class);
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

        pd = new ProgressDialog(WalletPurchasehistory.this);
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


                                Toast.makeText(WalletPurchasehistory.this, s, Toast.LENGTH_SHORT).show();

                                sp = getSharedPreferences("login", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.clear();
                                editor.commit();
                                finish();

                                Intent in = new Intent(WalletPurchasehistory.this,FirstPage.class);
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
                            Toast.makeText(WalletPurchasehistory.this, "Fail", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
    }
}

