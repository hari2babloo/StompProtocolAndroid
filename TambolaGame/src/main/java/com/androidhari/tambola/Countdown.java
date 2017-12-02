package com.androidhari.tambola;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bcgdv.asia.lib.ticktock.TickTockView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


import ua.naiksoftware.tambola.MainActivity;
import ua.naiksoftware.tambola.R;


public class Countdown extends AppCompatActivity {

    Button stargame;
    SharedPreferences sp;
    private AdapterFish Adapter;
    ProgressDialog pd;
    String pass,gameid,gamestarttime,gstime,longV;
    ArrayList row1 = new ArrayList();
    ArrayList<String> tktrow1 = new ArrayList<String>();
    ArrayList<String> tktrow2 = new ArrayList<String>();
    ArrayList<String> tktrow3 = new ArrayList<String>();

    List<DataFish> filterdata=new ArrayList<>();
    long elapsedDays;
    long elapsedHours ;
    long elapsedMinutes;
    long elapsedSeconds;
    private TickTockView mCountDown = null;
    private TextView mTxtHeadline = null;
    TextView textView,timer,timer0,timer1,timer2;
      private RecyclerView mRVFishPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.countdown);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        sp=getSharedPreferences("login",MODE_PRIVATE);
        pass=sp.getString("token",null);
        gameid=sp.getString("gno",null);
        gamestarttime=sp.getString("gstime",null);


        timer =(TextView)findViewById(R.id.timer);
        timer0 =(TextView)findViewById(R.id.timer0);
        timer1 =(TextView)findViewById(R.id.timer1);
        timer2=(TextView)findViewById(R.id.timer2);
        pd = new ProgressDialog(Countdown.this);
        pd.setMessage("Getting Server Time");
        pd.setCancelable(false);
        pd.show();



        final OkHttpClient client = new OkHttpClient();



        final Request request = new Request.Builder()
                .url("http://game-dev.techmech.men:8080/api/game/time")
                .get()
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization",pass)
                .build();




        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                pd.cancel();
                pd.cancel();

                String mMessage = e.getMessage().toString();
 //               Log.w("failure Response", mMessage);

//                Toast.makeText(Signin.this, mMessage, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                 longV = response.body().string();
                pd.cancel();
                pd.dismiss();

                ///    Log.w("Response", mMessage);
                if (response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            //        JSONObject json = new JSONObject(mMessage);

                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");

                            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+05:30"));

                            long millisecond = Long.parseLong(longV);
                            // or you already have long value of date, use this instead of milliseconds variable.
                            String endtime = DateFormat.format("dd/MM/yyyy hh:mm:ss a", new Date(millisecond)).toString();
                            Date date1 = null;
                            Date date2 = null;
                            try {
                                date2 = simpleDateFormat.parse(gamestarttime);
                                date1 = simpleDateFormat.parse(endtime);

                                Log.e("dadsadas000", date2.toString() +    date1.toString());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            printDifference(date1,date2);

                            Authenticate();

//                            Toast.makeText(Signin.this, "Success", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

                else {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            pd.cancel();
                            pd.dismiss();

                            Toast.makeText(Countdown.this, "Cancel", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });

  //      Toast.makeText(this, gameid, Toast.LENGTH_SHORT).show();

        mTxtHeadline = (TextView) findViewById(R.id.txt_headline);

        mCountDown = (TickTockView) findViewById(R.id.view_ticktock_countdown);
        mCountDown.setVisibility(View.GONE);

        textView = (TextView)findViewById(R.id.timer);

        textView.setText("Game Start Time : "+ gamestarttime);


        //Toast.makeText(this, gameid  + gamestarttime, Toast.LENGTH_SHORT).show();

        if (mCountDown != null) {
            mCountDown.setOnTickListener(new TickTockView.OnTickListener() {


                @Override
                public String getText(long timeRemaining) {
                    int seconds = (int) (timeRemaining / 1000) % 60;
                    int minutes = (int) ((timeRemaining / (1000 * 60)) % 60);
                    int hours = (int) ((timeRemaining / (1000 * 60 * 60)) % 24);
                    int days = (int) (timeRemaining / (1000 * 60 * 60 * 24));
                    boolean hasDays = days > 0;



                    if (timeRemaining==0){


                        mTxtHeadline.setText("times up  ");
                       opengame();

                        mCountDown.stop();
                    }

                    String Timer = String.format("%1$02d%4$s%2$02d%5$s%3$02d%6$s",
                            hasDays ? days : hours,
                            hasDays ? hours : minutes,
                            hasDays ? minutes : seconds,
                            hasDays ? "d" : "h",
                            hasDays ? "h" : "m",
                            hasDays ? "m" : "s");

                    Log.e("timeer", Timer);

                    String string = Timer;

                    if (string.substring(0, 3).contains("d")){


                        timer0.setText(string.substring(0, 2)+"\nDays");
                    }
                    else {
                        timer0.setText(string.substring(0, 2)+"\nHours");

                    }

                    if (string.substring(3, 6).contains("h")){


                        timer1.setText(string.substring(3, 5)+"\nHours");
                    }
                    else {
                        timer1.setText(string.substring(3, 5)+"\nMins");

                    }
                    if (string.substring(6, 9).contains("m")){


                        timer2.setText(string.substring(6, 8)+"\nMins");
                    }
                    else {
                        timer2.setText(string.substring(6, 8)+"\nSecs");

                    }


//                    timer0.setText(string.substring(0, 3));
//                    timer1.setText(string.substring(3, 6));
//                    timer2.setText(string.substring(6, 9));

//                    System.out.println(string.substring(0, 3));
//                    System.out.println(string.substring(3, 6));
//                    System.out.println(string.substring(6, 9));
//                    System.out.println(string.substring(6, 8));


                    mTxtHeadline.setText("Game is Going to Start in:");

                  //  Log.e("time",s);
                    return String.format("%1$02d%4$s %2$02d%5$s %3$02d%6$s",
                            hasDays ? days : hours,
                            hasDays ? hours : minutes,
                            hasDays ? minutes : seconds,
                            hasDays ? "d" : "h",
                            hasDays ? "h" : "m",
                            hasDays ? "m" : "s");


                }
            });
        }


           }

    private void opengame() {

        SharedPreferences.Editor e = sp.edit();
        e.putString("gno",gameid);
        e.putString("gstime",gamestarttime);

        e.commit();

        Intent intent = new Intent(Countdown.this,MainActivity.class);
        startActivity(intent);
        Countdown.this.finish();
        finishAffinity();
    }


    private void Authenticate() {

        pd = new ProgressDialog(Countdown.this);
        pd.setMessage("Getting Your Tickets");
        pd.setCancelable(false);
        pd.show();



        final OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url("http://game-dev.techmech.men:8080/api/game/user/tickets/"+gameid)


                .get()
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization",pass)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                pd.cancel();
                pd.dismiss();

                String mMessage = e.getMessage().toString();
 //               Log.w("failure Response", mMessage);
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

                                pd.cancel();
                                pd.dismiss();

                                String mm = mMessage;
                                mm=mMessage.replace("null","' '");
                                JSONObject json = new JSONObject(mm);

                                JSONArray jsonArray = json.getJSONArray("data");

                                JSONObject json_data2 = jsonArray.getJSONObject(0).getJSONObject("game");

                                JSONArray sds = json_data2.getJSONArray("prizes");
                                LinearLayout linearLayout = new LinearLayout(Countdown.this);
                                linearLayout.setOrientation(LinearLayout.VERTICAL);
                                for (int i = 0; i < sds.length(); i++) {

                                    JSONObject fd = sds.getJSONObject(i);

                                    String s= fd.getString("prizeName");
                                    String d = fd.getString("prizeCost");

//
//                                    TextView msg = new TextView(Countdown.this);
////                                    msg.setBackgroundResource(R.drawable.rectangle);
//                                    msg.setText(s+"       :  "+d);
//                                    msg.setPadding(2, 2, 2, 2);
//                                    msg.setTextColor(getResources().getColor(R.color.colorPrimary));
//                                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, Gravity.LEFT);
//
//                                    msg.setLayoutParams(params);
//                                    LinearLayout chat = (LinearLayout) findViewById(R.id.hari);
//                                    chat.addView(msg);

//                                    Log.d("erewrwer",s+d);

                                }


                                for (int i = 0; i < jsonArray.length(); i++){
                                    JSONObject json_data = jsonArray.getJSONObject(i).getJSONObject("ticket");
                                    DataFish data = new DataFish();
                                    String ticket = json_data.getString("ticket");
                                    JSONArray row1 = (JSONArray) new JSONArray(ticket).get(0);
                                    JSONArray row2 = (JSONArray) new JSONArray(ticket).get(1);
                                    JSONArray row3 = (JSONArray) new JSONArray(ticket).get(2);


                                    String s = json_data.getString("id");

                                    for ( int j = 0; j <row1.length();j++ ){



                                        tktrow1.add(row1.getString(j));

   //                                     Log.w("Row 1 ", String.valueOf(tktrow1));

                                    }

                                    for ( int j = 0; j <row2.length();j++ ){



                                        tktrow2.add(row2.getString(j));

  //                                      Log.w("Row 2", String.valueOf(tktrow2));

                                    }


                                    for ( int j = 0; j <row3.length();j++ ){



                                        tktrow2.add(row3.getString(j));

    //                                    Log.w("Row 3", String.valueOf(tktrow3));

                                    }
                                    data.id = s;
                                    data.t1 = row1.getString(0);
                                    data.t2 = row1.getString(1);
                                    data.t3 = row1.getString(2);
                                    data.t4 = row1.getString(3);
                                    data.t5 = row1.getString(4);
                                    data.t6 = row1.getString(5);
                                    data.t7 = row1.getString(6);
                                    data.t8 = row1.getString(7);
                                    data.t9 = row1.getString(8);
                                    data.t10 = row2.getString(0);
                                    data.t11 = row2.getString(1);
                                    data.t12 = row2.getString(2);
                                    data.t13 = row2.getString(3);
                                    data.t14 = row2.getString(4);
                                    data.t15 = row2.getString(5);
                                    data.t16 = row2.getString(6);
                                    data.t17 = row2.getString(7);
                                    data.t18 = row2.getString(8);
                                    data.t19 = row3.getString(0);
                                    data.t20 = row3.getString(1);
                                    data.t21 = row3.getString(2);
                                    data.t22 = row3.getString(3);
                                    data.t23 = row3.getString(4);
                                    data.t24 = row3.getString(5);
                                    data.t25 = row3.getString(6);
                                    data.t26 = row3.getString(7);
                                    data.t27 = row3.getString(8);


                                    filterdata.add(data);
//                                    Log.w("Response", data.t1);

                                }

//                                Log.e("Data", String.valueOf(row1));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


//                            Table();
                            mRVFishPrice = (RecyclerView)findViewById(R.id.fishPriceList);

                            Adapter = new AdapterFish(Countdown.this,filterdata);
                            mRVFishPrice.setAdapter(Adapter);
//                            mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));

                            mRVFishPrice.setLayoutManager(new GridLayoutManager(Countdown.this,1));
                            //      mRVFishPrice.setLayoutManager(new LinearLayoutManager(MainActivity.this,LinearLayoutManager.HORIZONTAL,true));
//
//                            Toast.makeText(Countdown.this, "PASS", Toast.LENGTH_SHORT).show();


                        }
                    });


//                    mAdapter.notifyDataSetChanged();

                }

                else runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pd.dismiss();
                        pd.cancel();

                        Toast.makeText(Countdown.this, "FAIL", Toast.LENGTH_SHORT).show();

                    }
                });


            }


        });
    }


    public void printDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();
        System.out.println("startDate : " + startDate);
        System.out.println("endDate : "+ endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;


        elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        elapsedSeconds = different / secondsInMilli;

        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n" + "Elapse",
                elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);


        Calendar end = Calendar.getInstance();
        end.add(Calendar.DAY_OF_MONTH, (int) elapsedDays);
        end.add(Calendar.HOUR, (int) elapsedHours);
        end.add(Calendar.MINUTE, (int) elapsedMinutes);
        end.add(Calendar.SECOND, (int) elapsedSeconds);

        Calendar start = Calendar.getInstance();

//        start.add(Calendar.DAY_OF_MONTH, (int) elapsedDays);
//        start.add(Calendar.HOUR, (int) elapsedHours);
//        start.add(Calendar.MINUTE, (int) elapsedMinutes);
//        start.add(Calendar.SECOND, (int) elapsedSeconds);
        start.add(Calendar.SECOND, -1);

        if (mCountDown != null) {
            mCountDown.start(start, end);
        }



    }
    @Override
    protected void onStart() {
        super.onStart();



    }

    @Override
    protected void onStop() {
        super.onStop();
       mCountDown.stop();

    }

    @Override
    public void onActionModeFinished(ActionMode mode) {
        super.onActionModeFinished(mode);
    }

    public class DataFish {



        public String preferredName;
        public String presentCount;
        public String rollGroup;
        public String id;
        public String t1;
        public String t2;
        public String t3;
        public String t4;
        public String t5;
        public String t6;
        public String t7;
        public String t8;
        public String t9;
        public String t10;
        public String t11;
        public String t12;
        public String t13;
        public String t14;
        public String t15;
        public String t16;
        public String t17;
        public String t18;
        public String t19;
        public String t20;
        public String t21;
        public String t22;
        public String t23;
        public String t24;
        public String t25;
        public String t26;
        public String t27;



    }
    public class AdapterFish extends RecyclerView.Adapter<RecyclerView.ViewHolder> {




        List<DataFish> data = Collections.emptyList();
        DataFish current;
        int currentPos = 0;
        private Context context;
        private LayoutInflater inflater;

        // create constructor to innitilize context and data sent from MainActivity
        public AdapterFish(Context context, List<DataFish> data) {
            this.context = context;
            inflater = LayoutInflater.from(context);
            this.data = data;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.ticket, parent, false);

            final AdapterFish.MyHolder holder = new AdapterFish.MyHolder(view);



            return holder;
        }


        // Bind data
        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {


            // Get current position of item in recyclerview to bind data and assign values from list
            final AdapterFish.MyHolder myHolder = (AdapterFish.MyHolder) holder;

         final  DataFish current = data.get(position);
//            myHolder.one.setText("Name: " + current.preferredName + "  " + current.surname);

//            myHolder.id.setText( "Ticket ID:  "+current.id);
            myHolder.one.setText(current.t1);
            myHolder.two.setText( current.t2);
            myHolder.three.setText(current.t3);
            myHolder.four.setText(current.t4);
            myHolder.five.setText(current.t5);
            myHolder.six.setText(current.t6);
            myHolder.seven.setText(current.t7);
            myHolder.eight.setText(current.t8);
            myHolder.nine.setText(current.t9);
            myHolder.ten.setText(current.t10);
            myHolder.eleven.setText( current.t11);
            myHolder.twelve.setText(current.t12);
            myHolder.thirteen.setText(current.t13);
            myHolder.fourteen.setText(current.t14);
            myHolder.fifteen.setText(current.t15);
            myHolder.sixteen.setText(current.t16);
            myHolder.seventeen.setText(current.t17);
            myHolder.eighteen.setText(current.t18);
            myHolder.nineteen.setText(current.t19);
            myHolder.twenty.setText( current.t20);
            myHolder.twentyone.setText(current.t21);
            myHolder.twentytwo.setText(current.t22);
            myHolder.twentythree.setText(current.t23);
            myHolder.twentyfour.setText(current.t24);
            myHolder.twentyfive.setText(current.t25);
            myHolder.twentysix.setText(current.t26);
            myHolder.twentyseven.setText(current.t27);


        }

        // return total item from List
        @Override
        public int getItemCount() {
            return data.size();
        }


        class MyHolder extends RecyclerView.ViewHolder {

            TextView one;

            TextView id;
            CheckBox checkBox;

            TextView two;
            TextView three;
            TextView four;
            TextView five;
            TextView six;
            TextView seven;
            TextView eight;
            TextView nine;
            TextView ten;
            TextView eleven;
            TextView twelve;
            TextView thirteen;
            TextView fourteen;
            TextView fifteen;
            TextView sixteen;
            TextView seventeen;
            TextView eighteen;
            TextView nineteen;
            TextView twenty;
            TextView twentyone;
            TextView twentytwo;
            TextView twentythree;
            TextView twentyfour;
            TextView twentyfive;
            TextView twentysix;
            TextView twentyseven;
            Button claim;


            // create constructor to get widget reference
            public MyHolder(View itemView) {
                super(itemView);
                //  id= (TextView)itemView.findViewById(R.id.id);
                claim = (Button)itemView.findViewById(R.id.claim);
                claim.setVisibility(View.GONE);
                checkBox = (CheckBox)itemView.findViewById(R.id.checkedTextView);
                checkBox.setVisibility(View.GONE);
                id= (TextView) itemView.findViewById(R.id.id);
                id.setVisibility(View.GONE);
                one = (TextView) itemView.findViewById(R.id.t1);
                two = (TextView) itemView.findViewById(R.id.t2);
            three = (TextView) itemView.findViewById(R.id.t3);

                four = (TextView) itemView.findViewById(R.id.t4);

                five = (TextView) itemView.findViewById(R.id.t5);


                six = (TextView) itemView.findViewById(R.id.t6);


                seven = (TextView) itemView.findViewById(R.id.t7);


                eight = (TextView) itemView.findViewById(R.id.t8);

                nine = (TextView) itemView.findViewById(R.id.t9);


                ten = (TextView)itemView.findViewById(R.id.t10);


                eleven= (TextView)itemView.findViewById(R.id.t11);


                twelve= (TextView)itemView.findViewById(R.id.t12);


                thirteen =  (TextView)itemView.findViewById(R.id.t13);
                 fourteen= (TextView)itemView.findViewById(R.id.t14);


                fifteen= (TextView)itemView.findViewById(R.id.t15);
                 sixteen= (TextView)itemView.findViewById(R.id.t16);

                 seventeen= (TextView)itemView.findViewById(R.id.t17);
                 eighteen= (TextView)itemView.findViewById(R.id.t18);

                 nineteen= (TextView)itemView.findViewById(R.id.t19);


                twenty = (TextView)itemView.findViewById(R.id.t20);


                twentyone= (TextView)itemView.findViewById(R.id.t21);
                    twentytwo= (TextView)itemView.findViewById(R.id.t22);


                twentythree= (TextView)itemView.findViewById(R.id.t23);


                twentyfour= (TextView)itemView.findViewById(R.id.t24);

                twentyfive= (TextView)itemView.findViewById(R.id.t25);

                twentysix= (TextView)itemView.findViewById(R.id.t26);

                twentyseven= (TextView)itemView.findViewById(R.id.t27);



            }


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

                Intent intent = new Intent(Countdown.this, Wallet.class);
                mCountDown.stop();
                startActivity(intent);
                // Do something
                return true;
            case R.id.action_item_one:

                Intent intent2 = new Intent(Countdown.this, HomeScreen.class);
                mCountDown.stop();
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

        pd = new ProgressDialog(Countdown.this);
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


                                Toast.makeText(Countdown.this, s, Toast.LENGTH_SHORT).show();

                                sp = getSharedPreferences("login", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.clear();
                                editor.commit();
                                finish();

                                Intent in = new Intent(Countdown.this,FirstPage.class);
                                mCountDown.stop();
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
                            Toast.makeText(Countdown.this, "Fail", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        mCountDown.stop();
    }
}
