package ua.naiksoftware.tambola;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.androidhari.db.TinyDB;

import com.androidhari.tambola.HomeScreen;

import com.androidhari.tambola.Signin;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import org.java_websocket.WebSocket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.client.StompClient;

public class MainActivity extends AppCompatActivity {




    TextToSpeech textToSpeech;

    public static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json");
    String claimid,tktid;
    int claimposition;
    ProgressDialog pd;


    Collection firstlist = new ArrayList();
    private boolean tvSelected1 = true;
    private boolean tvSelected2 = true;
    private boolean tvSelected3 = true;
    private boolean tvSelected4 = true;
    private boolean tvSelected5 = true;
    private boolean tvSelected6 = true;
    private boolean tvSelected7 = true;
    private boolean tvSelected8 = true;
    private boolean tvSelected9 = true;
    private boolean tvSelected10 = true;
    private boolean tvSelected11 = true;
    private boolean tvSelected12 = true;
    private boolean tvSelected13 = true;
    private boolean tvSelected14 = true;
    private boolean tvSelected15 = true;
    private boolean tvSelected16 = true;
    private boolean tvSelected17 = true;
    private boolean tvSelected18 = true;
    private boolean tvSelected19 = true;
    private boolean tvSelected20 = true;
    private boolean tvSelected21 = true;
    private boolean tvSelected22 = true;
    private boolean tvSelected23 = true;
    private boolean tvSelected24 = true;
    private boolean tvSelected25 = true;
    private boolean tvSelected26 = true;
    private boolean tvSelected27 = true;

    private AdapterFish Adapter;
    private DataFish current;

    ArrayList<String> completednumbers = new ArrayList<>();
//    ArrayList<String> gamesaveid = new ArrayList<>();
//    JSONArray postdata2 = new JSONArray();
    ArrayList row1 = new ArrayList();
    ArrayList<String> tktrow1 = new ArrayList<String>();
    ArrayList<String> tktrow2 = new ArrayList<String>();
    ArrayList<String> tktrow3 = new ArrayList<String>();
//    ArrayList<String> reftktrow1 = new ArrayList<String>();
//    ArrayList<String> reftktrow2 = new ArrayList<String>();
//    ArrayList<String> reftktrow3 = new ArrayList<String>();
//    ArrayList<JSONObject> listdata = new ArrayList<>();

    TextView number,fastfive,firstrow,middlerow,lastrow,fullhouse,noofplayers;



    SharedPreferences sp;
    String pass,gameid,gamestarttime;
//    ArrayList<String> tktids = new ArrayList<>();

    List<DataFish> filterdata=new ArrayList<>();
  //  List<DataFish> refresheddata=new ArrayList<>();

    //   private RecyclerView mRVFishPrice;
    private static final String TAG = "MainActivity";

    private SimpleAdapter mAdapter;
    private List<String> mDataSet = new ArrayList<>();
    private StompClient mStompClient;
    private Disposable mRestPingDisposable;
    private RecyclerView mRVFishPrice;
    TinyDB tinydb;
  //  private final SimpleDateFormat mTimeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
   // private RecyclerView mRecyclerView;
    private Gson mGson = new GsonBuilder().create();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        sp=getSharedPreferences("login",MODE_PRIVATE);
        pass=sp.getString("token",null);
        gameid=sp.getString("gno",null);
        gamestarttime=sp.getString("gstime",null);
//        pass = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmNAYWJjLmNvbSIsImF1ZGllbmNlIjoid2ViIiwiY3JlYXRlZCI6MTUxMjM4MTY1NTg5MCwiZXhwIjoxNTEyOTg2NDU1fQ.oErSUt_gbWBCQpipqaJ4NAJPLG4kvP5z0wTMY_Fm3IJDuNiS2SzGjqs7_WK8fspAPoy8PVrs-SdqNWo1a3SgCQ";
//        gameid = "82";
        tinydb = new TinyDB(this);

        completednumbers = tinydb.getListString(gameid);

        if(completednumbers != null) {
            //has items here. The fact that has items does not mean that the items are != null.
            //You have to check the nullity for every item

            completednumbers.add("0");
            tinydb.putListString(gameid,completednumbers);
            Log.e("Completeed Numbers", String.valueOf(completednumbers));
            Log.e(" Exists","Exists");
        }
        else {

            completednumbers = tinydb.getListString(gameid);
            Log.e("Completeed Numbers", String.valueOf(completednumbers));
            Log.e(" Not Exists Exists","Exists");

// either there is no instance of ArrayList in arrayList or the list is empty.
        }







        number = (TextView)findViewById(R.id.Number);
        fastfive = (TextView)findViewById(R.id.fastfive);
        firstrow = (TextView)findViewById(R.id.firstrow);
        middlerow = (TextView)findViewById(R.id.middlerow);
        lastrow = (TextView)findViewById(R.id.lastrow);
        fullhouse = (TextView)findViewById(R.id.fullhouse);
        noofplayers = (TextView)findViewById(R.id.noofplayers);
        Authenticate();
//        new Thread(new Runnable() {
//            public v-oid run() {
//
//
//                connectStomp();
//            }
//        }).start();


        final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        executor.schedule(() -> connectStomp(), 2, TimeUnit.SECONDS);


        new Thread(new Runnable() {
            public void run() {


                 textToSpeech=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if(status != TextToSpeech.ERROR) {
                            textToSpeech.setLanguage(Locale.UK);
                        }
                    }
                });

//                System.out.println("Look at me, look at me...");
            }
        }).start();








 //       mRVFishPrice = (RecyclerView)findViewById(R.id.fishPriceList);
      //  mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new SimpleAdapter(mDataSet);
        mAdapter.setHasStableIds(true);
     //   mRecyclerView.setAdapter(mAdapter);
      //  mRecyclerView.setLayoutManager(new GridLayoutManager(this, 7));
    }


    public void disconnectStomp(View view) {
        mStompClient.disconnect();
    }

    public void connectStomp() {
//        mStompClient = Stomp.over(WebSocket.class, "ws://" + ANDROID_EMULATOR_LOCALHOST
//                + ":" + RestClient.SERVER_PORT + "/example-endpoint/websocket");

        mStompClient = Stomp.over(WebSocket.class, "ws://game-dev.techmech.men:8080/game-websocket/websocket");

        mStompClient.lifecycle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lifecycleEvent -> {
                    switch (lifecycleEvent.getType()) {
                        case OPENED:
  //                          toast("Stomp connection opened");
                            break;
                        case ERROR:
                            Log.e(TAG, "Stomp connection error", lifecycleEvent.getException());
  //                          toast("Stomp connection error");
                            break;
                        case CLOSED:
    //                        toast("Stomp connection closed");
                    }
                });

        // Receive greetings
        mStompClient.topic("/topic/game/"+gameid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topicMessage -> {
                    Log.d(TAG, "Received " + topicMessage.getPayload());
                    addItem(mGson.fromJson(topicMessage.getPayload(), EchoModel.class));





                });

        mStompClient.connect();
    }

//    public void sendEchoViaStomp(View v) {
//        mStompClient.send("/topic/hello-msg-mapping", "Echo STOMP " + mTimeFormat.format(new Date()))
//                .compose(applySchedulers())
//                .subscribe(aVoid -> {
//                    Log.d(TAG, "STOMP echo send successfully");
//                }, throwable -> {
//                    Log.e(TAG, "Error send STOMP echo", throwable);
//  //
//                    //                  toast(throwable.getMessage());
//                });
//    }

//    public void sendEchoViaRest(View v) {
//        mRestPingDisposable = RestClient.getInstance().getExampleRepository()
//                .sendRestEcho("Echo REST " + mTimeFormat.format(new Date()))
//                .compose(applySchedulers())
//                .subscribe(aVoid -> {
//                    Log.d(TAG, "REST echo send successfully");
//                }, throwable -> {
//                    Log.e(TAG, "Error send REST echo", throwable);
//  //                  toast(throwable.getMessage());
//                });
//    }

    private void addItem(EchoModel echoModel) {


        if (echoModel.getNumber()==0){

            number.setBackground(getDrawable(R.drawable.circle3));
//            number.setBackgroundColor(Color.RED);
//            Toast.makeText(this, "ZeroIsMaxDateTimeField", Toast.LENGTH_SHORT).show();
        }
        else if (echoModel.getNumber()!=0){
            number.setText(echoModel.getNumber().toString());
           number.setBackground(getDrawable(R.drawable.circle2));
    //        completednumbers = echoModel.getCompletedNumbers();
//            completednumbers = echoModel.getCompletedNumbers();
            Log.e("dsfsdfsdfs", String.valueOf(echoModel.getCompletedNumbers()));

            textToSpeech.speak(echoModel.getNumber().toString(), TextToSpeech.QUEUE_FLUSH, null);


        }

        if (echoModel.getMessage()!=null){

            Toast.makeText(this, echoModel.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
        else
        if (echoModel.getMessageList()!=null){

            Toast.makeText(this, echoModel.getMessageList().toString(), Toast.LENGTH_SHORT).show();
        }


//        if (echoModel.getCompletedNumbers()!=null){
//
//
//
//        }


        if (echoModel.getValidClaim()==true){
          //  completednumbers = (ArrayList<String>) echoModel.getCompletedNumbers();
            Log.e("complerted ", String.valueOf(completednumbers));
            Authenticate();


  //          Log.e("fdfsd", String.valueOf(echoModel.getCompletedNumbers()));
        }

         if (echoModel.getGameCompleted()==true){
            number.setBackgroundColor(Color.RED);
            Toast.makeText(this, "GAME FINISHED", Toast.LENGTH_SHORT).show();
            Intent in = new Intent(MainActivity.this, HomeScreen.class);
            startActivity(in);
            Log.e("Game","Game Finished");
        }


//        mDataSet.clear();
//
//
//        mDataSet.add(echoModel.getNumber().toString());
//
//
//        //    mDataSet.add(echoModel.getNumber() + " - " + mTimeFormat.format(new Date()));
//        mAdapter.notifyDataSetChanged();
     //   mRecyclerView.smoothScrollToPosition(mDataSet.size() - 1);
    }


    private void toast(String text) {
        Log.i(TAG, text);
      //  Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    protected <T> FlowableTransformer<T, T> applySchedulers() {
        return tFlowable -> tFlowable
                .unsubscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    protected void onDestroy() {
        mStompClient.disconnect();
        if (mRestPingDisposable != null) mRestPingDisposable.dispose();
        super.onDestroy();
    }


//    private void refreshticket() {
//
//
//            final OkHttpClient client = new OkHttpClient();
//            final Request request = new Request.Builder()
//                    .url("http://game-dev.techmech.men:8080/api/game/user/tickets/"+gameid)
//                    .get()
//                    .addHeader("Content-Type", "application/json")
//                    .addHeader("Authorization",pass)
//                    .build();
//
//            client.newCall(request).enqueue(new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//
//                    String mMessage = e.getMessage().toString();
//                    Log.w("failure Response", mMessage);
//
//
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//
//                    final String mMessage3 = response.body().string();
//
//
// //                   Log.w("Response", mMessage);
//                    if (response.isSuccessful()){
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                String mm2 = mMessage3;
//                                mm2=mMessage3.replace("null","' '");
//                                JSONObject json3 = null;
//                                try {
//                                    json3 = new JSONObject(mm2);
//                                    JSONArray jsonArray3 = json3.getJSONArray("data");
//                                    for (int m = 0; m < jsonArray3.length(); m++) {
//
//                                        JSONObject fd2 = jsonArray3.getJSONObject(m);
//
//                                        String s = fd2.getString("id");
//                                        secondlist.add(s);
//
//                                    }
//
//
//
//                                    Log.e("FirstList", String.valueOf(firstlist));
//                                    Log.e("Secondlist", String.valueOf(secondlist));
//
//
//
//                                    JSONObject json_data4 = jsonArray3.getJSONObject(0).getJSONObject("game");
//
//                                    JSONArray sds2 = json_data4.getJSONArray("prizes");
//
//                                    for (int i = 0; i < sds2.length(); i++) {
//
//                                        JSONObject fd = sds2.getJSONObject(i);
//
//                                        String prizename = fd.getString("prizeName");
//                                        String prizecost = fd.getString("prizeCost");
//                                        String prizeCompleted = fd.getString("prizeCompleted");
//
//                                        if (prizeCompleted.equalsIgnoreCase("TRUE")){
//
//
//
//                                            if(prizename.equalsIgnoreCase("FULL_HOUISE"))
//                                            {
//
//                                                fullhouse.setText("FULL_HOUISE: FINISHED "  );
//
//                                            }
//
//                                            else
//                                            if (prizename.equalsIgnoreCase("FIRST_ROW")){
//
//
//                                                firstrow.setText("FIRST ROW: FINISHED  ");
//
//                                            }
//                                            else
//                                            if (prizename.equalsIgnoreCase("MIDDLE_ROW")) {
//                                                middlerow.setText("MIDDLE_ROW: FINISHED ");
//
//                                            }
//                                            else
//                                            if (prizename.equalsIgnoreCase("FAST_FIVE")){
//
//                                                fastfive.setText("FAST FIVE: FINISHED  ");
//
//
//                                            }
//                                            else
//                                            if (prizename.equalsIgnoreCase("LAST_ROW")) {
//
//                                                lastrow.setText("LAST ROW: FINISHED ");
//
//
//                                            }
//
//
//
//                                        }
//
//                                        for (int n = 0; n < sds2.length(); n++){
//                                            JSONObject json_data = sds2.getJSONObject(i).getJSONObject("ticket");
//                                            DataFish data = new DataFish();
//                                            String ticket = json_data.getString("ticket");
//                                            JSONArray row1 = (JSONArray) new JSONArray(ticket).get(0);
//                                            JSONArray row2 = (JSONArray) new JSONArray(ticket).get(1);
//                                            JSONArray row3 = (JSONArray) new JSONArray(ticket).get(2);
//                                            String s = json_data.getString("id");
//
//                                            for ( int j = 0; j <row1.length();j++ ){
//
//
////
//  //                                              reftktrow1.add(row1.getString(j));
//
////                                                Log.w("Row 1 ", String.valueOf(reftktrow1));
//
//                                            }
//
//                                            for ( int j = 0; j <row2.length();j++ ){
//
//
//
//  //                                              reftktrow2.add(row2.getString(j));
//
//                                                Log.w("Row 2", String.valueOf(reftktrow2));
//
//                                            }
//
//
//                                            for ( int j = 0; j <row3.length();j++ ){
//
//
//
//                                                reftktrow3.add(row3.getString(j));
//
// //                                               Log.w("Row 3", String.valueOf(reftktrow3));
//
//                                            }
//                                            data.id = s;
//                                            data.t1 = row1.getString(0);
//                                            data.t2 = row1.getString(1);
//                                            data.t3 = row1.getString(2);
//                                            data.t4 = row1.getString(3);
//                                            data.t5 = row1.getString(4);
//                                            data.t6 = row1.getString(5);
//                                            data.t7 = row1.getString(6);
//                                            data.t8 = row1.getString(7);
//                                            data.t9 = row1.getString(8);
//                                            data.t10 = row2.getString(0);
//                                            data.t11 = row2.getString(1);
//                                            data.t12 = row2.getString(2);
//                                            data.t13 = row2.getString(3);
//                                            data.t14 = row2.getString(4);
//                                            data.t15 = row2.getString(5);
//                                            data.t16 = row2.getString(6);
//                                            data.t17 = row2.getString(7);
//                                            data.t18 = row2.getString(8);
//                                            data.t19 = row3.getString(0);
//                                            data.t20 = row3.getString(1);
//                                            data.t21 = row3.getString(2);
//                                            data.t22 = row3.getString(3);
//                                            data.t23 = row3.getString(4);
//                                            data.t24 = row3.getString(5);
//                                            data.t25 = row3.getString(6);
//                                            data.t26 = row3.getString(7);
//                                            data.t27 = row3.getString(8);
//
//
//                                            refresheddata.add(data);
////                                    Log.w("Response", data.t1);
//
//                                        }
//
//                                    }
//
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//
//
//                              //  mRVFishPrice = (RecyclerView)findViewById(R.id.fishPriceList);
//                                Adapter = new AdapterFish(MainActivity.this, refresheddata);
//
//
//
//                                mRVFishPrice.setAdapter(Adapter);
////                            mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
//
//                                mRVFishPrice.setLayoutManager(new GridLayoutManager(MainActivity.this,1));
//                                mRVFishPrice.setHasFixedSize(true);
//
//
//                            }
//                        });
//
//                    }
//
//                    else {
//
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//
//                            }
//                        });
//                    }
//
//                }
//            });
//
//
//    }


    private void Authenticate() {

        tktrow1.clear();
        tktrow2.clear();
        tktrow3.clear();
        filterdata.clear();
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

                String mMessage = e.getMessage().toString();
                Log.w("failure Response", mMessage);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                final String mMessage = response.body().string();



//                Log.w("Response", mMessage);

                if (response.isSuccessful()){

                        runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {

                                String mm = mMessage;
                                mm=mMessage.replace("null","' '");
                                JSONObject json = new JSONObject(mm);

                                JSONArray jsonArray = json.getJSONArray("data");


                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        JSONObject fd = jsonArray.getJSONObject(i);

                                        String s = fd.getString("id");


                                        firstlist.add(s);


                                    }
                                    Log.e("firstlist", String.valueOf(firstlist));
                                    JSONObject json_data2 = jsonArray.getJSONObject(0).getJSONObject("game");


                                noofplayers.setText( "No of Players:   "+json_data2.getString("noOfPlayers"));


                                JSONArray sds = json_data2.getJSONArray("prizes");
                                LinearLayout linearLayout = new LinearLayout(MainActivity.this);
                                linearLayout.setOrientation(LinearLayout.VERTICAL);
                                for (int i = 0; i < sds.length(); i++) {

                                    JSONObject fd = sds.getJSONObject(i);

                                    String prizename= fd.getString("prizeName");


                                    String prizecost = fd.getString("prizeCost");

                                    String prizeCompleted = fd.getString("prizeCompleted");

                                    Log.e("prizename",prizeCompleted +prizename+prizecost);



                                        if(prizename.equalsIgnoreCase("FULL_HOUISE"))
                                        {

                                            fullhouse.setText("FULL HOUSE: "+prizecost.toString());

                                        }
                                        else
                                        if (prizename.equalsIgnoreCase("FIRST_ROW")){


                                            firstrow.setText( "FIRST_ROW" +prizecost.toString());

                                        }

                                        else



                                        if (prizename.equalsIgnoreCase("MIDDLE_ROW")) {
                                            middlerow.setText("MIDDLE_ROW:  "+prizecost);

                                            //                                    middlerow.setText("hello");
                                        }

                                        else if (prizename.equalsIgnoreCase("FAST_FIVE")){

                                            fastfive.setText("FAST FIVE:  "+prizecost);


                                        }

                                        else


                                        if (prizename.equalsIgnoreCase("LAST_ROW")) {

                                            lastrow.setText("LASTROW:  "+prizecost);


                                        }

                                    if (prizeCompleted.equalsIgnoreCase("TRUE")){

                                        if(prizename.equalsIgnoreCase("FULL_HOUISE"))
                                        {

                                            fullhouse.setText("FULL HOUSE: FINISHED");
                                            fullhouse.setTextColor(Color.RED);

                                        }
                                        else

                                        if (prizename.equalsIgnoreCase("FIRST_ROW")){


                                            firstrow.setText( "FIRST_ROW: FINISHED");
                                            firstrow.setTextColor(Color.RED);

                                        }



                                        else



                                        if (prizename.equalsIgnoreCase("MIDDLE_ROW")) {
                                            middlerow.setText("MIDDLE_ROW: FINISHED ");
                                            middlerow.setTextColor(Color.RED);

                                            //                                    middlerow.setText("hello");
                                        }

                                        else if (prizename.equalsIgnoreCase("FAST_FIVE")){

                                            fastfive.setText("FAST FIVE: FINISHED ");
                                            fastfive.setTextColor(Color.RED);


                                        }

                                        else


                                        if (prizename.equalsIgnoreCase("LAST_ROW")) {

                                            lastrow.setText("LASTROW: FINISHED ");
                                            lastrow.setTextColor(Color.RED);


                                        }


                                    }

                                    }



//                                }


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

      //                                 Log.w("Row 1 ", String.valueOf(tktrow1));

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

                                Log.e("Data", String.valueOf(row1));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
//                            Table();
                            mRVFishPrice = (RecyclerView)findViewById(R.id.fishPriceList);
                            Adapter = new AdapterFish(MainActivity.this, filterdata);
                            Adapter.setHasStableIds(false);
                            mRVFishPrice.setAdapter(Adapter);

                            mRVFishPrice.setHasFixedSize(false);

  //                          mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
  //                          mRVFishPrice.setLayoutManager(new GridLayoutManager(MainActivity.this,1));
                                 mRVFishPrice.setLayoutManager(new LinearLayoutManager(MainActivity.this,LinearLayoutManager.VERTICAL,true));
  ///                          Toast.makeText(MainActivity.this, "PASS", Toast.LENGTH_SHORT).show();
                        }
                    });


//                    mAdapter.notifyDataSetChanged();

                }

                else runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        try {
                            JSONObject json = new JSONObject(mMessage);
                            String status = json.getString("status");
                            String message = json.getString("message");
                            //title = name;

                            if (status.equalsIgnoreCase("401")){


                                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this,Signin.class);
                                startActivity(intent);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

//                        Toast.makeText(MainActivity.this, "FAIL", Toast.LENGTH_SHORT).show();

                    }
                });


            }


        });
    }

//    private void Authenticate2() {
//
//
//        pd = new ProgressDialog(MainActivity.this);
//        pd.setMessage("Please Wait While we are Validating..");
//        pd.setCancelable(false);
//        pd.show();
//        final OkHttpClient client = new OkHttpClient();
//
//        final Request request = new Request.Builder()
//                .url("http://game-dev.techmech.men:8080/api/game/user/tickets/"+gameid)
//                .get()
//                .addHeader("Content-Type", "application/json")
//                .addHeader("Authorization",pass)
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//                pd.dismiss();
//                pd.cancel();
//                String mMessage = e.getMessage().toString();
//                Log.w("failure Response", mMessage);
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//
//                pd.dismiss();
//                pd.cancel();
//
//                final String mMessage = response.body().string();
//
//
//                tktrow1.clear();
//                tktrow2.clear();
//                tktrow3.clear();
//                filterdata.clear();
//
////                Log.w("Response", mMessage);
//
//                if (response.isSuccessful()){
//
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            try {
//
//                                String mm = mMessage;
//                                mm=mMessage.replace("null","' '");
//                                JSONObject json = new JSONObject(mm);
//
//                                JSONArray jsonArray = json.getJSONArray("data");
//
//
//                                for (int i = 0; i < jsonArray.length(); i++) {
//
//                                    JSONObject fd = jsonArray.getJSONObject(i);
//
//                                    String s = fd.getString("id");
//
//
//                                    firstlist.add(s);
//
//
//                                }
//                                Log.e("firstlist", String.valueOf(firstlist));
//                                JSONObject json_data2 = jsonArray.getJSONObject(0).getJSONObject("game");
//                                noofplayers.setText( "No of Players:   "+json_data2.getString("noOfPlayers"));
//
//
//                                JSONArray sds = json_data2.getJSONArray("prizes");
//                                LinearLayout linearLayout = new LinearLayout(MainActivity.this);
//                                linearLayout.setOrientation(LinearLayout.VERTICAL);
//                                for (int i = 0; i < sds.length(); i++) {
//
//                                    JSONObject fd = sds.getJSONObject(i);
//
//                                    String prizename= fd.getString("prizeName");
//                                    String prizecost = fd.getString("prizeCost");
//
//                                    String prizeCompleted = fd.getString("prizeCompleted");
//
//                                    if (prizeCompleted.equalsIgnoreCase("true")){
//
//
//                                        if(prizename.equalsIgnoreCase("FULL_HOUISE"))
//                                        {
//
//                                            fullhouse.setText("FULL_HOUISE: FINISHED" );
//
//                                        }
//
//                                        else
//                                        if (prizename.equalsIgnoreCase("FIRST_ROW")){
//
//
//                                            firstrow.setText("FIRST ROW:  FINISHED");
//
//                                        }
//                                        else
//                                        if (prizename.equalsIgnoreCase("MIDDLE_ROW")) {
//                                            middlerow.setText("MIDDLE_ROW:  FINISHED");
//
//                                        }
//                                        else
//                                        if (prizename.equalsIgnoreCase("FAST_FIVE")){
//
//                                            fastfive.setText("MIDDLE_ROW:  FINISHED");
//
//
//                                        }
//                                        else
//                                        if (prizename.equalsIgnoreCase("LAST_ROW")) {
//
//                                            lastrow.setText("MIDDLE_ROW:  FINISHED");
//
//
//                                        }
//
//
//
//                                    }
//
//
//
//
//                                }
//
//
//                                for (int i = 0; i < jsonArray.length(); i++){
//                                    JSONObject json_data = jsonArray.getJSONObject(i).getJSONObject("ticket");
//                                    DataFish data = new DataFish();
//                                    String ticket = json_data.getString("ticket");
//                                    JSONArray row1 = (JSONArray) new JSONArray(ticket).get(0);
//                                    JSONArray row2 = (JSONArray) new JSONArray(ticket).get(1);
//                                    JSONArray row3 = (JSONArray) new JSONArray(ticket).get(2);
//
//                                    String s = json_data.getString("id");
//
//                                    for ( int j = 0; j <row1.length();j++ ){
//
//
//
//                                        tktrow1.add(row1.getString(j));
//
////                                        Log.w("Row 1 ", String.valueOf(tktrow1));
//
//                                    }
//
//                                    for ( int j = 0; j <row2.length();j++ ){
//
//
//
//                                        tktrow2.add(row2.getString(j));
//
//   //                                     Log.w("Row 2", String.valueOf(tktrow2));
//
//                                    }
//
//
//                                    for ( int j = 0; j <row3.length();j++ ){
//
//
//
//                                        tktrow2.add(row3.getString(j));
//
// //                                       Log.w("Row 3", String.valueOf(tktrow3));
//
//                                    }
//                                    data.id = s;
//                                    data.t1 = row1.getString(0);
//                                    data.t2 = row1.getString(1);
//                                    data.t3 = row1.getString(2);
//                                    data.t4 = row1.getString(3);
//                                    data.t5 = row1.getString(4);
//                                    data.t6 = row1.getString(5);
//                                    data.t7 = row1.getString(6);
//                                    data.t8 = row1.getString(7);
//                                    data.t9 = row1.getString(8);
//                                    data.t10 = row2.getString(0);
//                                    data.t11 = row2.getString(1);
//                                    data.t12 = row2.getString(2);
//                                    data.t13 = row2.getString(3);
//                                    data.t14 = row2.getString(4);
//                                    data.t15 = row2.getString(5);
//                                    data.t16 = row2.getString(6);
//                                    data.t17 = row2.getString(7);
//                                    data.t18 = row2.getString(8);
//                                    data.t19 = row3.getString(0);
//                                    data.t20 = row3.getString(1);
//                                    data.t21 = row3.getString(2);
//                                    data.t22 = row3.getString(3);
//                                    data.t23 = row3.getString(4);
//                                    data.t24 = row3.getString(5);
//                                    data.t25 = row3.getString(6);
//                                    data.t26 = row3.getString(7);
//                                    data.t27 = row3.getString(8);
//
//
//                                    filterdata.add(data);
////                                    Log.w("Response", data.t1);
//
//                                }
//
//                                Log.e("Data", String.valueOf(row1));
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
////                            Table();
////                            mRVFishPrice = (RecyclerView)findViewById(R.id.fishPriceList);
//                            Adapter = new AdapterFish(MainActivity.this, filterdata);
//                            mRVFishPrice.setAdapter(Adapter);
////                            mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
//
//                            mRVFishPrice.setLayoutManager(new GridLayoutManager(MainActivity.this,1));
//                            //      mRVFishPrice.setLayoutManager(new LinearLayoutManager(MainActivity.this,LinearLayoutManager.HORIZONTAL,true));
//                            ///                          Toast.makeText(MainActivity.this, "PASS", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//
//
////                    mAdapter.notifyDataSetChanged();
//
//                }
//
//                else runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        pd.dismiss();
//                        pd.cancel();
//
////                        Toast.makeText(MainActivity.this, "FAIL", Toast.LENGTH_SHORT).show();
//
//                    }
//                });
//
//
//            }
//
//
//        });
//    }

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

            final MyHolder holder = new MyHolder(view);


            return holder;
        }


        // Bind data
        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

            // Get current position of item in recyclerview to bind data and assign values from list
            final MyHolder myHolder = (MyHolder) holder;


        //    holder.setIsRecyclable(true);
            final DataFish current = data.get(position);

            holder.getLayoutPosition();

        //    setHasStableIds(true);




            myHolder.one.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (tvSelected1) {
                        myHolder.one.setBackground(getDrawable(R.drawable.ticketborder2));
                        tvSelected1 = false;
                    }
                    else {
                        myHolder.one.setBackground(getDrawable(R.drawable.ticketbrder));
                        tvSelected1 = true;
                    }

                    if (completednumbers.contains(data.get(position).t1)){

                        completednumbers.remove(data.get(position).t1);
                    }
                    else {

                        completednumbers.add(data.get(position).t1);
                    }
                    Log.d("fdsf0", String.valueOf(completednumbers));
                }
            });

            myHolder.two.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (completednumbers.contains(data.get(position).t2)){

                        completednumbers.remove(data.get(position).t2);
                    }
                    else {

                        completednumbers.add(data.get(position).t2);
                    }


                    if (tvSelected2) {


                        myHolder.two.setBackground(getDrawable(R.drawable.ticketborder2));
                        tvSelected2 = false;
                    }
                    else {

                        myHolder.two.setBackground(getDrawable(R.drawable.ticketbrder));

                        tvSelected2 = true;
                    }

                    Log.d("fdsf0", String.valueOf(completednumbers));

                }
            });

            myHolder.claim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    number.setBackground(getDrawable(R.drawable.circle2));


//                    completednumbers.add("23");
//                    completednumbers.add("28");
//                    completednumbers.add("38");
//                    completednumbers.add("24");
//                    completednumbers.add("44");



                    tinydb.putListString(gameid,completednumbers);



//                    data.get(position);

//
  //                  claimid = ((MyHolder) holder).id.toString();

    //                myHolder.getAdapterPosition();

                    claimposition = position;
                    claimid = data.get(position).id;


                    Log.e("position", String.valueOf(claimposition));

                    Log.e("ticketid", String.valueOf(claimid));


                    final OkHttpClient client = new OkHttpClient();
                    JSONObject postdata = new JSONObject();
                    try {
                        postdata.put("gameId",gameid);
                        postdata.put("ticketId", claimid);
                    } catch(JSONException e){
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    RequestBody body = RequestBody.create(MEDIA_TYPE,
                            postdata.toString());


//                        Log.e("postclaim", String.valueOf(positionposition));

                    final Request request = new Request.Builder()
                            .url("http://game-dev.techmech.men:8080/api/game/claim")
                            .post(body)
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Authorization",pass)

                            .build();
                    //        Log.e("dasdasd", body.toString());



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
                                            String s = json.getString("status");
                                            String st = json.getString("message");
                                            Toast.makeText(MainActivity.this, st, Toast.LENGTH_SHORT).show();
//
                                            Log.w("Response",st+s);
                                            //   Toast.makeText(Signin.this, s, Toast.LENGTH_SHORT).show();

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
 //d                                       Toast.makeText(MainActivity.this, "Failed to Claim", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        }
                    });

                }
            });

//            myHolder.one.setText("Name: " + current.preferredName + "  " + current.surname);
       //     myHolder.checkBox.setVisibility(View.GONE);

            myHolder. three.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (completednumbers.contains(data.get(position).t3)){

                        completednumbers.remove(data.get(position).t3);
                    }
                    else {

                        completednumbers.add(data.get(position).t3);
                    }

                    if (tvSelected3) {

                        myHolder. three.setBackground(getDrawable(R.drawable.ticketborder2));
                        tvSelected3 = false;
                    }
                    else {

                        myHolder.three.setBackground(getDrawable(R.drawable.ticketbrder));
                        tvSelected3 = true;
                    }
                    Log.d("fdsf0", String.valueOf(completednumbers));
                }
            });

            myHolder.four.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (completednumbers.contains(data.get(position).t4)){

                        completednumbers.remove(data.get(position).t4);
                    }
                    else {

                        completednumbers.add(data.get(position).t4);
                    }


                    if (tvSelected4) {


                        myHolder.four.setBackground(getDrawable(R.drawable.ticketborder2));
                        tvSelected4 = false;
                    }
                    else {

                        myHolder.four.setBackground(getDrawable(R.drawable.ticketbrder));

                        tvSelected4 = true;
                    }

                    Log.d("fdsf0", String.valueOf(completednumbers));

                }
            });

            myHolder.five.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (completednumbers.contains(data.get(position).t5)){

                        completednumbers.remove(data.get(position).t5);
                    }
                    else {

                        completednumbers.add(data.get(position).t5);
                    }


                    if (tvSelected5) {


                        myHolder.five.setBackground(getDrawable(R.drawable.ticketborder2));
                        tvSelected5 = false;
                    }
                    else {

                        myHolder.five.setBackground(getDrawable(R.drawable.ticketbrder));

                        tvSelected5 = true;
                    }

                    Log.d("fdsf0", String.valueOf(completednumbers));

                }
            });

            myHolder.six.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (completednumbers.contains(data.get(position).t6)){

                        completednumbers.remove(data.get(position).t6);
                    }
                    else {

                        completednumbers.add(data.get(position).t6);
                    }


                    if (tvSelected6) {


                        myHolder.six.setBackground(getDrawable(R.drawable.ticketborder2));
                        tvSelected6 = false;
                    }
                    else {

                        myHolder.six.setBackground(getDrawable(R.drawable.ticketbrder));

                        tvSelected2 = true;
                    }

                    Log.d("fdsf0", String.valueOf(completednumbers));

                }
            });

            myHolder.seven.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (completednumbers.contains(data.get(position).t7)){

                        completednumbers.remove(data.get(position).t7);
                    }
                    else {

                        completednumbers.add(data.get(position).t7);
                    }


                    if (tvSelected7) {


                        myHolder.seven.setBackground(getDrawable(R.drawable.ticketborder2));
                        tvSelected7 = false;
                    }
                    else {

                        myHolder.seven.setBackground(getDrawable(R.drawable.ticketbrder));

                        tvSelected7 = true;
                    }

                    Log.d("fdsf0", String.valueOf(completednumbers));

                }
            });

            myHolder.eight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (completednumbers.contains(data.get(position).t8)){

                        completednumbers.remove(data.get(position).t8);
                    }
                    else {

                        completednumbers.add(data.get(position).t8);
                    }


                    if (tvSelected8) {


                        myHolder.eight.setBackground(getDrawable(R.drawable.ticketborder2));
                        tvSelected8 = false;
                    }
                    else {

                        myHolder.eight.setBackground(getDrawable(R.drawable.ticketbrder));

                        tvSelected8 = true;
                    }

                    Log.d("fdsf0", String.valueOf(completednumbers));

                }
            });
            myHolder.nine.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (completednumbers.contains(data.get(position).t9)){

                        completednumbers.remove(data.get(position).t9);
                    }
                    else {

                        completednumbers.add(data.get(position).t9);
                    }


                    if (tvSelected9) {


                        myHolder.nine.setBackground(getDrawable(R.drawable.ticketborder2));
                        tvSelected9 = false;
                    }
                    else {

                        myHolder.nine.setBackground(getDrawable(R.drawable.ticketbrder));

                        tvSelected9 = true;
                    }

                    Log.d("fdsf0", String.valueOf(completednumbers));

                }
            });

            myHolder.ten.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (completednumbers.contains(data.get(position).t10)){

                        completednumbers.remove(data.get(position).t10);
                    }
                    else {

                        completednumbers.add(data.get(position).t10);
                    }


                    if (tvSelected10) {


                        myHolder.ten.setBackground(getDrawable(R.drawable.ticketborder2));
                        tvSelected10 = false;
                    }
                    else {

                        myHolder.ten.setBackground(getDrawable(R.drawable.ticketbrder));

                        tvSelected10 = true;
                    }

                    Log.d("fdsf0", String.valueOf(completednumbers));

                }
            });

            myHolder.eleven.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (completednumbers.contains(data.get(position).t11)){

                        completednumbers.remove(data.get(position).t11);
                    }
                    else {

                        completednumbers.add(data.get(position).t11);
                    }


                    if (tvSelected11) {


                        myHolder.eleven.setBackground(getDrawable(R.drawable.ticketborder2));
                        tvSelected11 = false;
                    }
                    else {

                        myHolder.eleven.setBackground(getDrawable(R.drawable.ticketbrder));

                        tvSelected11 = true;
                    }

                    Log.d("fdsf0", String.valueOf(completednumbers));

                }
            });

            myHolder.twelve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (completednumbers.contains(data.get(position).t12)){

                        completednumbers.remove(data.get(position).t12);
                    }
                    else {

                        completednumbers.add(data.get(position).t12);
                    }


                    if (tvSelected12) {


                        myHolder.twelve.setBackground(getDrawable(R.drawable.ticketborder2));
                        tvSelected12 = false;
                    }
                    else {

                        myHolder.twelve.setBackground(getDrawable(R.drawable.ticketbrder));

                        tvSelected12 = true;
                    }

                    Log.d("fdsf0", String.valueOf(completednumbers));

                }
            });

            myHolder.thirteen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (completednumbers.contains(data.get(position).t13)){

                        completednumbers.remove(data.get(position).t13);
                    }
                    else {

                        completednumbers.add(data.get(position).t13);
                    }


                    if (tvSelected13) {


                        myHolder.thirteen.setBackground(getDrawable(R.drawable.ticketborder2));
                        tvSelected13 = false;
                    }
                    else {

                        myHolder.thirteen.setBackground(getDrawable(R.drawable.ticketbrder));

                        tvSelected13 = true;
                    }

                    Log.d("fdsf0", String.valueOf(completednumbers));

                }
            });

            myHolder.fourteen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (completednumbers.contains(data.get(position).t14)){

                        completednumbers.remove(data.get(position).t14);
                    }
                    else {

                        completednumbers.add(data.get(position).t14);
                    }


                    if (tvSelected14) {


                        myHolder.fourteen.setBackground(getDrawable(R.drawable.ticketborder2));
                        tvSelected14 = false;
                    }
                    else {

                        myHolder.fourteen.setBackground(getDrawable(R.drawable.ticketbrder));

                        tvSelected14 = true;
                    }

                    Log.d("fdsf0", String.valueOf(completednumbers));

                }
            });

            myHolder.fifteen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (completednumbers.contains(data.get(position).t15)){

                        completednumbers.remove(data.get(position).t15);
                    }
                    else {

                        completednumbers.add(data.get(position).t15);
                    }


                    if (tvSelected15) {


                        myHolder.fifteen.setBackground(getDrawable(R.drawable.ticketborder2));
                        tvSelected15 = false;
                    }
                    else {

                        myHolder.fifteen.setBackground(getDrawable(R.drawable.ticketbrder));

                        tvSelected15 = true;
                    }

                    Log.d("fdsf0", String.valueOf(completednumbers));

                }
            });

            myHolder.sixteen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (completednumbers.contains(data.get(position).t16)){

                        completednumbers.remove(data.get(position).t16);
                    }
                    else {

                        completednumbers.add(data.get(position).t16);
                    }


                    if (tvSelected16) {


                        myHolder.sixteen.setBackground(getDrawable(R.drawable.ticketborder2));
                        tvSelected16 = false;
                    }
                    else {

                        myHolder.sixteen.setBackground(getDrawable(R.drawable.ticketbrder));

                        tvSelected16 = true;
                    }

                    Log.d("fdsf0", String.valueOf(completednumbers));

                }
            });

            myHolder.seventeen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (completednumbers.contains(data.get(position).t17)){

                        completednumbers.remove(data.get(position).t17);
                    }
                    else {

                        completednumbers.add(data.get(position).t17);
                    }


                    if (tvSelected17) {


                        myHolder.seventeen.setBackground(getDrawable(R.drawable.ticketborder2));
                        tvSelected17 = false;
                    }
                    else {

                        myHolder.seventeen.setBackground(getDrawable(R.drawable.ticketbrder));

                        tvSelected17 = true;
                    }

                    Log.d("fdsf0", String.valueOf(completednumbers));

                }
            });

            myHolder.eighteen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (completednumbers.contains(data.get(position).t18)){

                        completednumbers.remove(data.get(position).t18);
                    }
                    else {

                        completednumbers.add(data.get(position).t18);
                    }


                    if (tvSelected18) {


                        myHolder.eighteen.setBackground(getDrawable(R.drawable.ticketborder2));
                        tvSelected18 = false;
                    }
                    else {

                        myHolder.eighteen.setBackground(getDrawable(R.drawable.ticketbrder));

                        tvSelected18 = true;
                    }

                    Log.d("fdsf0", String.valueOf(completednumbers));

                }
            });

            myHolder.nineteen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (completednumbers.contains(data.get(position).t19)){

                        completednumbers.remove(data.get(position).t19);
                    }
                    else {

                        completednumbers.add(data.get(position).t19);
                    }


                    if (tvSelected19) {


                        myHolder.nineteen.setBackground(getDrawable(R.drawable.ticketborder2));
                        tvSelected19 = false;
                    }
                    else {

                        myHolder.nineteen.setBackground(getDrawable(R.drawable.ticketbrder));

                        tvSelected19 = true;
                    }

                    Log.d("fdsf0", String.valueOf(completednumbers));

                }
            });

            myHolder.twenty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (completednumbers.contains(data.get(position).t20)){

                        completednumbers.remove(data.get(position).t20);
                    }
                    else {

                        completednumbers.add(data.get(position).t20);
                    }


                    if (tvSelected20) {


                        myHolder.twenty.setBackground(getDrawable(R.drawable.ticketborder2));
                        tvSelected20 = false;
                    }
                    else {

                        myHolder.twenty.setBackground(getDrawable(R.drawable.ticketbrder));

                        tvSelected20 = true;
                    }

                    Log.d("fdsf0", String.valueOf(completednumbers));

                }
            });

            myHolder.twentyone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (completednumbers.contains(data.get(position).t21)){

                        completednumbers.remove(data.get(position).t21);
                    }
                    else {

                        completednumbers.add(data.get(position).t21);
                    }


                    if (tvSelected21) {


                        myHolder.twentyone.setBackground(getDrawable(R.drawable.ticketborder2));
                        tvSelected21 = false;
                    }
                    else {

                        myHolder.twentyone.setBackground(getDrawable(R.drawable.ticketbrder));

                        tvSelected21 = true;
                    }

                    Log.d("fdsf0", String.valueOf(completednumbers));

                }
            });

            myHolder.twentytwo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (completednumbers.contains(data.get(position).t22)){

                        completednumbers.remove(data.get(position).t22);
                    }
                    else {

                        completednumbers.add(data.get(position).t22);
                    }


                    if (tvSelected22) {


                        myHolder.twentytwo.setBackground(getDrawable(R.drawable.ticketborder2));
                        tvSelected22 = false;
                    }
                    else {

                        myHolder.twentytwo.setBackground(getDrawable(R.drawable.ticketbrder));

                        tvSelected22 = true;
                    }

                    Log.d("fdsf0", String.valueOf(completednumbers));

                }
            });

            myHolder.twentythree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (completednumbers.contains(data.get(position).t23)){

                        completednumbers.remove(data.get(position).t23);
                    }
                    else {

                        completednumbers.add(data.get(position).t23);
                    }


                    if (tvSelected23) {


                        myHolder.twentythree.setBackground(getDrawable(R.drawable.ticketborder2));
                        tvSelected23 = false;
                    }
                    else {

                        myHolder.twentythree.setBackground(getDrawable(R.drawable.ticketbrder));

                        tvSelected23 = true;
                    }

                    Log.d("fdsf0", String.valueOf(completednumbers));

                }
            });


            myHolder.twentyfour.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (completednumbers.contains(data.get(position).t24)){

                        completednumbers.remove(data.get(position).t24);
                    }
                    else {
                        completednumbers.add(data.get(position).t24);
                    }


                    if (tvSelected24) {


                        myHolder.twentyfour.setBackground(getDrawable(R.drawable.ticketborder2));
                        tvSelected24 = false;
                    }
                    else {

                        myHolder.twentyfour.setBackground(getDrawable(R.drawable.ticketbrder));

                        tvSelected24 = true;
                    }

                    Log.d("fdsf0", String.valueOf(completednumbers));

                }
            });


            myHolder.twentyfive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (completednumbers.contains(data.get(position).t25)){

                        completednumbers.remove(data.get(position).t25);
                    }
                    else {

                        completednumbers.add(data.get(position).t25);
                    }


                    if (tvSelected25) {


                        myHolder.twentyfive.setBackground(getDrawable(R.drawable.ticketborder2));
                        tvSelected25 = false;
                    }
                    else {

                        myHolder.twentyfive.setBackground(getDrawable(R.drawable.ticketbrder));

                        tvSelected25 = true;
                    }

                    Log.d("fdsf0", String.valueOf(completednumbers));

                }
            });

            myHolder.twentysix.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (completednumbers.contains(data.get(position).t26)){

                        completednumbers.remove(data.get(position).t26);
                    }
                    else {

                        completednumbers.add(data.get(position).t26);
                    }


                    if (tvSelected26) {


                        myHolder.twentysix.setBackground(getDrawable(R.drawable.ticketborder2));
                        tvSelected26 = false;
                    }
                    else {

                        myHolder.twentysix.setBackground(getDrawable(R.drawable.ticketbrder));

                        tvSelected26 = true;
                    }

                    Log.d("fdsf0", String.valueOf(completednumbers));

                }
            });

            myHolder.twentyseven.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (completednumbers.contains(data.get(position).t27)){

                        completednumbers.remove(data.get(position).t27);
                    }
                    else {

                        completednumbers.add(data.get(position).t27);
                    }


                    if (tvSelected27) {


                        myHolder.twentyseven.setBackground(getDrawable(R.drawable.ticketborder2));
                        tvSelected27 = false;
                    }
                    else {

                        myHolder.twentyseven.setBackground(getDrawable(R.drawable.ticketbrder));

                        tvSelected27 = true;
                    }

                    Log.d("fdsf0", String.valueOf(completednumbers));

                }
            });

            if (completednumbers.contains(current.t1)){

                myHolder.one.setText(current.t1);

                myHolder.one.setBackground(getDrawable(R.drawable.ticketborder2));

            }
            else {

                myHolder.one.setText(current.t1);
            }


            if (completednumbers.contains(current.t2)){

                myHolder.two.setText(current.t2);

                myHolder.two.setBackground(getDrawable(R.drawable.ticketborder2));

            }
            else {

                myHolder.two.setText(current.t2);
            }

            if (completednumbers.contains(current.t3)){

                myHolder.three.setText(current.t3);

                myHolder.three.setBackground(getDrawable(R.drawable.ticketborder2));

            }
            else {

                myHolder.three.setText(current.t3);
            }

            if (completednumbers.contains(current.t4)){
                myHolder.four.setText(current.t4);

                myHolder.four.setBackground(getDrawable(R.drawable.ticketborder2));

            }
            else {

                myHolder.four.setText(current.t4);
            }

            if (completednumbers.contains(current.t5)){

                myHolder.five.setText(current.t5);

                myHolder.five.setBackground(getDrawable(R.drawable.ticketborder2));

            }
            else {

                myHolder.five.setText(current.t5);
            }

            if (completednumbers.contains(current.t6)){

                myHolder.six.setText(current.t6);
                myHolder.six.setBackground(getDrawable(R.drawable.ticketborder2));

            }
            else {

                myHolder.six.setText(current.t6);
            }
            if (completednumbers.contains(current.t7)){

                myHolder.seven.setText(current.t7);
                myHolder.seven.setBackground(getDrawable(R.drawable.ticketborder2));

            }
            else {

                myHolder.seven.setText(current.t7);
            }

            if (completednumbers.contains(current.t8)){
                myHolder.eight.setText(current.t8);

                myHolder.eight.setBackground(getDrawable(R.drawable.ticketborder2));

            }
            else {

                myHolder.eight.setText(current.t8);
            }
            if (completednumbers.contains(current.t9)){
                myHolder.nine.setText(current.t9);

                myHolder.nine.setBackground(getDrawable(R.drawable.ticketborder2));

            }
            else {

                myHolder.nine.setText(current.t9);
            }
            if (completednumbers.contains(current.t10)){
                myHolder.ten.setText(current.t10);
                myHolder.ten.setBackground(getDrawable(R.drawable.ticketborder2));

            }
            else {

                myHolder.ten.setText(current.t10);
            }
            if (completednumbers.contains(current.t11)){

                myHolder.eleven.setBackground(getDrawable(R.drawable.ticketborder2));
                myHolder.eleven.setText(current.t11);
            }
            else {

                myHolder.eleven.setText(current.t11);
            }


            if (completednumbers.contains(current.t12)){

                myHolder.twelve.setText(current.t12);
                myHolder.twelve.setBackground(getDrawable(R.drawable.ticketborder2));

            }
            else {

                myHolder.twelve.setText(current.t12);
            }

            if (completednumbers.contains(current.t13)){


                myHolder.thirteen.setText(current.t13);
                myHolder.thirteen.setBackground(getDrawable(R.drawable.ticketborder2));

            }
            else {

                myHolder.thirteen.setText(current.t13);
            }
            if (completednumbers.contains(current.t14)){
                myHolder.fourteen.setText(current.t14);
                myHolder.fourteen.setBackground(getDrawable(R.drawable.ticketborder2));

            }
            else {

                myHolder.fourteen.setText(current.t14);
            }
            if (completednumbers.contains(current.t15)){

                myHolder.fifteen.setText(current.t15);
                myHolder.fifteen.setBackground(getDrawable(R.drawable.ticketborder2));

            }
            else {

                myHolder.fifteen.setText(current.t15);
            }

            if (completednumbers.contains(current.t16)){
                myHolder.sixteen.setText(current.t16);
                myHolder.sixteen.setBackground(getDrawable(R.drawable.ticketborder2));

            }
            else {

                myHolder.sixteen.setText(current.t16);
            }

            if (completednumbers.contains(current.t17)){

                myHolder.seventeen.setText(current.t17);
                myHolder.seventeen.setBackground(getDrawable(R.drawable.ticketborder2));

            }
            else {

                myHolder.seventeen.setText(current.t17);
            }

            if (completednumbers.contains(current.t18)){

                myHolder.eighteen.setText(current.t18);
                myHolder.eighteen.setBackground(getDrawable(R.drawable.ticketborder2));

            }
            else {

                myHolder.eighteen.setText(current.t18);
            }

            if (completednumbers.contains(current.t19)){



                myHolder.nineteen.setText(current.t19);

                myHolder.nineteen.setBackground(getDrawable(R.drawable.ticketborder2));

            }
            else {

                myHolder.nineteen.setText(current.t19);
            }

            if (completednumbers.contains(current.t20)){

                myHolder.twenty.setText(current.t20);
                myHolder.twenty.setBackground(getDrawable(R.drawable.ticketborder2));

            }
            else {

                myHolder.twenty.setText(current.t20);
            }

            if (completednumbers.contains(current.t21)){
                myHolder.twentyone.setText(current.t21);

                myHolder.twentyone.setBackground(getDrawable(R.drawable.ticketborder2));

            }
            else {

                myHolder.twentyone.setText(current.t21);
            }
            if (completednumbers.contains(current.t22)){

                myHolder.twentytwo.setText(current.t22);
                myHolder.twentytwo.setBackground(getDrawable(R.drawable.ticketborder2));

            }
            else {

                myHolder.twentytwo.setText(current.t22);
            }
            if (completednumbers.contains(current.t23)){
                myHolder.twentythree.setText(current.t23);
                myHolder.twentythree.setBackground(getDrawable(R.drawable.ticketborder2));

            }
            else {

                myHolder.twentythree.setText(current.t23);
            }

            if (completednumbers.contains(current.t24)){

                myHolder.twentyfour.setText(current.t24);
                myHolder.twentyfour.setBackground(getDrawable(R.drawable.ticketborder2));

            }
            else {

                myHolder.twentyfour.setText(current.t24);
            }

            if (completednumbers.contains(current.t25)){

                myHolder.twentyfive.setText(current.t25);
                myHolder.twentyfive.setBackground(getDrawable(R.drawable.ticketborder2));

            }
            else {

                myHolder.twentyfive.setText(current.t25);
            }

            if (completednumbers.contains(current.t26)){

                myHolder.twentysix.setText(current.t26);
                myHolder.twentysix.setBackground(getDrawable(R.drawable.ticketborder2));

            }
            else {

                myHolder.twentysix.setText(current.t26);
            }
            if (completednumbers.contains(current.t27)){
                myHolder.twentyseven.setText(current.t27);
                myHolder.twentyseven.setBackground(getDrawable(R.drawable.ticketborder2));

            }
            else {

                myHolder.twentyseven.setText(current.t27);
            }


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
                checkBox =(CheckBox)itemView.findViewById(R.id.checkedTextView);
                checkBox.setVisibility(View.GONE);

                id = (TextView)itemView.findViewById(R.id.id);
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
    public void onBackPressed() {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
        builder1.setMessage("Do you want to Save the Game?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();


                        tinydb.putListString(gameid,completednumbers);
                        MainActivity.this.finish();

                        textToSpeech.shutdown();
                        finishAffinity();
                        Intent intent = new Intent(MainActivity.this,HomeScreen.class);
                        startActivity(intent);



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

    @Override
    protected void onStop() {
        super.onStop();
   //     mStompClient.disconnect();
    }

    @Override
    protected void onPause() {
        super.onPause();
   //     mStompClient.disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        mStompClient.connect();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
  //      mStompClient.connect();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    //    mStompClient.connect();
    }
}