package com.androidhari.tambola;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidhari.ViewPager.WalletTransactions;
import com.github.rubensousa.gravitysnaphelper.GravityPagerSnapHelper;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import ua.naiksoftware.tambola.R;

import static android.support.design.R.attr.windowActionBar;
import static android.view.View.GONE;

public class HomeScreen extends AppCompatActivity {

    public static final String ORIENTATION = "orientation";
    RecyclerView mRecyclerView;
    private boolean mHorizontal;

    Button logout;
    public static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json");
    SharedPreferences sp;
    ProgressDialog pd;
    String pass,id;
    //List<App> apps = getApps();

    List<App> apps = new ArrayList<>();
    List<App> apps1 = new ArrayList<>();
    List<App> apps2 = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.home_screen);


        sp= getSharedPreferences("login",MODE_PRIVATE);
        pass= sp.getString("token",null);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

//        ActionBar ab = getSupportActionBar();
//        // Enable the Up button
//        ab.setDisplayHomeAsUpEnabled(true);

        getdata();


//        if(pass != null && !pass.isEmpty()){
//            Toast.makeText(HomeScreen.this, pass, Toast.LENGTH_SHORT).show();
//
//            //finish current activity
//        }

//        logout = (Button)findViewById(R.id.logout);
        mRecyclerView= (RecyclerView) findViewById(R.id.horizontal_recycler_view);
        //      Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.inflateMenu(R.menu.main);
//        toolbar.setOnMenuItemClickListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);

        if (savedInstanceState == null) {
            mHorizontal = true;
        } else {
            mHorizontal = savedInstanceState.getBoolean(ORIENTATION);
        }

    }

    private void setupAdapter() {



        SnapAdapter snapAdapter = new SnapAdapter();
        if (mHorizontal) {

            snapAdapter.addSnap(new Snap(Gravity.START, "Bumper Games", apps1));
            snapAdapter.addSnap(new Snap(Gravity.CENTER_HORIZONTAL, "Upcoming Games   ", apps));
            snapAdapter.addSnap(new Snap(Gravity.END, "Private Games", apps2));
//            snapAdapter.addSnap(new Snap(Gravity.CENTER, "Top Games", apps));
            mRecyclerView.setAdapter(snapAdapter);

        } else {
            final Adapter adapter = new Adapter(false, false, apps);

            mRecyclerView.setAdapter(adapter);

            new GravitySnapHelper(Gravity.TOP, false, new GravitySnapHelper.SnapListener() {
                @Override
                public void onSnap(int position) {
                    Log.d("Snapped", position + "");
                }
            }).attachToRecyclerView(mRecyclerView);

        }


    }
    private void getdata() {

        pd = new ProgressDialog(HomeScreen.this);
        pd.setMessage("Getting Games Data");
        pd.setCancelable(false);
        pd.show();
        final OkHttpClient client = new OkHttpClient();
        JSONObject postdata = new JSONObject();

        RequestBody body = RequestBody.create(MEDIA_TYPE,
                postdata.toString());
        final Request request = new Request.Builder()
                .url("http://game-dev.techmech.men:8080/api/game")
                .get()
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization",pass)
                .build();
        Log.e("dasdasd", body.toString());
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                String mMessage = e.getMessage().toString();
                Log.w("failure Response", mMessage);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                pd.cancel();
                pd.dismiss();
                final String mMessage = response.body().string();
                if (response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject json = new JSONObject(mMessage);
                                JSONArray jsonArray = json.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject json_data = jsonArray.getJSONObject(i);

                                    String gameType = json_data.getString("gameType");

                                    if (gameType.equalsIgnoreCase("0")){

                                        apps.add(new App(json_data.getString("name"),R.drawable.img , 4.6f,json_data.getString("id")));
                                    }

                                    else

                                    if (gameType.equalsIgnoreCase("1")){

                                        apps1.add(new App(json_data.getString("name"),R.drawable.img , 4.6f,json_data.getString("id")));
                                    }

                                    else

                                    if (gameType.equalsIgnoreCase("2")){

                                        apps2.add(new App(json_data.getString("name"),R.drawable.img , 4.6f,json_data.getString("id")));
                                    }

                                    setupAdapter();

                                    Log.e("sdfdsf", json_data.getString("id"));

                                }


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
                            Toast.makeText(HomeScreen.this, "Fail", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });


    }

    public  class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

        private List<App> mApps;
        private boolean mHorizontal;
        private boolean mPager;

        public Adapter(boolean horizontal, boolean pager, List<App> apps) {
            mHorizontal = horizontal;
            mApps = apps;
            mPager = pager;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (mPager) {
                return new ViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.adapter_pager, parent, false));
            } else {
                return mHorizontal ? new ViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.adapter, parent, false)) :
                        new ViewHolder(LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.adapter_vertical, parent, false));
            }
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            App app = mApps.get(position);
            holder.imageView.setImageResource(app.getDrawable());
            holder.nameTextView.setText(app.getName());
            holder.ratingTextView.setText(String.valueOf(app.getRating()));

        }


        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        @Override
        public int getItemCount() {
            return mApps.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public ImageView imageView;
            public TextView nameTextView;
            public TextView ratingTextView;

            public ViewHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                imageView = (ImageView) itemView.findViewById(R.id.imageView);
                nameTextView = (TextView) itemView.findViewById(R.id.nameTextView);
                ratingTextView = (TextView) itemView.findViewById(R.id.ratingTextView);
                ratingTextView.setVisibility(GONE);
            }

            @Override
            public void onClick(View v) {


                SharedPreferences.Editor e = sp.edit();
                e.putString("id",mApps.get(getAdapterPosition()).getId());

                e.commit();


                HomeScreen.this.finish();
                Intent intent = new Intent(HomeScreen.this,GameInfo.class);
                startActivity(intent);
                Toast.makeText(HomeScreen.this, mApps.get(getAdapterPosition()).getId(), Toast.LENGTH_SHORT).show();

//                Log.d("App", mApps.get(getAdapterPosition()).getName());
            }
        }

    }

    public class SnapAdapter extends RecyclerView.Adapter<SnapAdapter.ViewHolder> implements GravitySnapHelper.SnapListener {

        public static final int VERTICAL = 0;
        public static final int HORIZONTAL = 1;

        private ArrayList<Snap> mSnaps;
        // Disable touch detection for parent recyclerView if we use vertical nested recyclerViews
        private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        };

        public SnapAdapter() {
            mSnaps = new ArrayList<>();
        }

        public void addSnap(Snap snap) {
            mSnaps.add(snap);
        }

        @Override
        public int getItemViewType(int position) {
            Snap snap = mSnaps.get(position);
            switch (snap.getGravity()) {
                case Gravity.CENTER_VERTICAL:
                    return VERTICAL;
                case Gravity.CENTER_HORIZONTAL:
                    return HORIZONTAL;
                case Gravity.START:
                    return HORIZONTAL;
                case Gravity.TOP:
                    return VERTICAL;
                case Gravity.END:
                    return HORIZONTAL;
                case Gravity.BOTTOM:
                    return VERTICAL;
            }
            return HORIZONTAL;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = viewType == VERTICAL ? LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_snap_vertical, parent, false)
                    : LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_snap, parent, false);

            if (viewType == VERTICAL) {
                view.findViewById(R.id.recyclerView).setOnTouchListener(mTouchListener);
            }

            return new ViewHolder(view);

        }



        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Snap snap = mSnaps.get(position);
            holder.snapTextView.setText(snap.getText());

            if (snap.getGravity() == Gravity.START || snap.getGravity() == Gravity.END) {
                holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder
                        .recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false));
                new GravitySnapHelper(snap.getGravity(), false, this).attachToRecyclerView(holder.recyclerView);
            } else if (snap.getGravity() == Gravity.CENTER_HORIZONTAL ||
                    snap.getGravity() == Gravity.CENTER_VERTICAL) {
                holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder
                        .recyclerView.getContext(), snap.getGravity() == Gravity.CENTER_HORIZONTAL ?
                        LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
                new LinearSnapHelper().attachToRecyclerView(holder.recyclerView);
            } else if (snap.getGravity() == Gravity.CENTER) { // Pager snap
                holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder
                        .recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false));
                new GravityPagerSnapHelper(Gravity.START).attachToRecyclerView(holder.recyclerView);
            } else { // Top / Bottom
                holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder
                        .recyclerView.getContext()));
                new GravitySnapHelper(snap.getGravity()).attachToRecyclerView(holder.recyclerView);
            }


            holder.recyclerView.setAdapter(new HomeScreen.Adapter(snap.getGravity() == Gravity.START
                    || snap.getGravity() == Gravity.END
                    || snap.getGravity() == Gravity.CENTER_HORIZONTAL,
                    snap.getGravity() == Gravity.CENTER, snap.getApps()));
        }

        @Override
        public int getItemCount() {
            return mSnaps.size();
        }

        @Override
        public void onSnap(int position) {
            Log.d("Snapped: ", position + "");
        }

        public  class ViewHolder extends RecyclerView.ViewHolder {

            public TextView snapTextView;
            public RecyclerView recyclerView;

            public ViewHolder(View itemView) {
                super(itemView);
                snapTextView = (TextView) itemView.findViewById(R.id.snapTextView);
                recyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerView);
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
//            case R.id.logout:
//
//                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
//                // User chose the "Favorite" action, mark the current item
//                // as a favorite...
//                return true;

            case R.id.action_item_one:

                Intent intent = new Intent(HomeScreen.this, HomeScreen.class);
                startActivity(intent);
                // Do something
                return true;
            case R.id.action_item_two:

                Intent intent2 = new Intent(HomeScreen.this, WalletTransactions.class);
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

        // If we got here, the user's action was not recognized.
        // Invoke the superclass to handle it.


        }
    private void Logout() {

        pd = new ProgressDialog(HomeScreen.this);
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


                                Toast.makeText(HomeScreen.this, s, Toast.LENGTH_SHORT).show();

                                sp = getSharedPreferences("login", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.clear();
                                editor.commit();
                                finish();

                                Intent in = new Intent(HomeScreen.this,FirstPage.class);
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
                            Toast.makeText(HomeScreen.this, "Fail", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
    }


    @Override
    public void onBackPressed() {

    }
}

