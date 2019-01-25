package com.tachyon.techlabs.iplauction;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tachyon.techlabs.iplauction.adapter.OpponentsDataAdapter;

import java.util.List;
import java.util.Objects;

public class OpponentsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    RecyclerView recyclerView;
    RecyclerView.Adapter opponents_data_adapter;
    Context context;
    Drawable drawable;
    //GradientDrawable gradientDrawable;
    Resources resources;
    RecyclerView.LayoutManager layoutManager;
    DrawerLayout mDrawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle mToggle;
    Toolbar opp_toolbar;
    Handler handler;
    AfterRegistrationMainActivity afterRegistrationMainActivity = new AfterRegistrationMainActivity();
    OngoingPlayer ongoingPlayer = new OngoingPlayer();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String userEmail,id;
    List<String> opp;
    List<Integer> budget;
    long [] bal = new long[10];
    String [] teams = {"MI", "CSK", "KKR", "RR","DC","RCB","SH","KXIP","GL","RPS"};
    //int [] players = {2,5,9,2,5,8,2,3,6,2};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opponents);



        long [] balance = {100000000,100000000,100000000,100000000,100000000,100000000,100000000,100000000,100000000,100000000};

        userEmail = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();

        opp_toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(opp_toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.placeholder);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.mydrawer);
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,opp_toolbar,R.string.open,R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        NavigationView nav = (NavigationView) findViewById(R.id.navigation_view);
        //View headerView = navigationView.getHeaderView(0);

        //String name = getIntent().getExtras().getString("name");
        nav.setNavigationItemSelectedListener(this);

        handler = new Handler();

        recyclerView = (RecyclerView) findViewById(R.id.opponents_recycler_view);
        context = getApplicationContext();

        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        resources = getResources();

        getRoom();

    }

    public void getRoom()
    {
        DocumentReference room_doc = db.collection("Players").document(userEmail);
        room_doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                {
                    try
                    {
                        id = documentSnapshot.getString("roomid");
                        getTeamName();
                    }
                    catch(Exception e)
                    {
                        Toast.makeText(OpponentsActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void getTeamName()
    {
        DocumentReference getTeam_doc = db.collection(id).document("Opponents");
        getTeam_doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                bal[0] = Objects.requireNonNull(documentSnapshot.getLong("MI")).intValue();
                bal[1] = Objects.requireNonNull(documentSnapshot.getLong("CSK")).intValue();
                bal[2] = Objects.requireNonNull(documentSnapshot.getLong("KKR")).intValue();
                bal[3] = Objects.requireNonNull(documentSnapshot.getLong("RR")).intValue();
                bal[4] = Objects.requireNonNull(documentSnapshot.getLong("DD")).intValue();
                bal[5] = Objects.requireNonNull(documentSnapshot.getLong("RCB")).intValue();
                bal[6] = Objects.requireNonNull(documentSnapshot.getLong("SH")).intValue();
                bal[7] = Objects.requireNonNull(documentSnapshot.getLong("KXIP")).intValue();
                bal[8] = Objects.requireNonNull(documentSnapshot.getLong("GL")).intValue();
                bal[9] = Objects.requireNonNull(documentSnapshot.getLong("RPS")).intValue();

                setOpp();
            }
        });
    }

    public void setOpp()
    {
        opponents_data_adapter = new OpponentsDataAdapter(context,teams,bal,resources);

        recyclerView.setAdapter(opponents_data_adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id = menuItem.getItemId();

        switch (id)
        {
            case R.id.nav_home:
                break;

            case R.id.nav_players:
                Runnable player_runnable = new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(OpponentsActivity.this,OngoingPlayer.class));
                        finish();
                    }
                };
                handler.postDelayed(player_runnable,150);
                //startActivity(new Intent(OngoingPlayer.this,PLAYERS.class));
                //finish();
                break;

            case R.id.reveal:
                Runnable rev_runnable = new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(OpponentsActivity.this,Reveal.class));
                        finish();
                    }
                };
                handler.postDelayed(rev_runnable,150);
                break;

            case R.id.nav_profile:
                //Intent prof = new Intent(AfterRegistrationMainActivity.this,ProfileActivity.class);
                //Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(OpponentsActivity.this,ProfileActivity.class));
                        finish();
                    }
                };
                handler.postDelayed(runnable,150);

                break;

            case R.id.nav_opponents:
                break;

            case R.id.nav_payments_info:
                Runnable dev_runnable = new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(OpponentsActivity.this,PaymentInfo.class));
                        finish();
                    }
                };
                handler.postDelayed(dev_runnable,150);
                break;

            case R.id.nav_cards:
                //afterRegistrationMainActivity.storagepermission();
                Runnable card_runnable = new Runnable() {
                    @Override
                    public void run() {
                        ongoingPlayer.storagepermission();
                        finish();
                    }
                };
                handler.postDelayed(card_runnable,150);

                break;

            case R.id.nav_share:
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                startActivity(Intent.createChooser(share,"Share Via"));
                break;

            case R.id.nav_rules:
                Uri uri = Uri.parse("https://drive.google.com/open?id=1JfX0bJFk_twjF4v_DdErvxr9O_5oVlB5"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;

            case R.id.nav_developer:
                //Handler handler = new Handler();
                Runnable pay_runnable = new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(OpponentsActivity.this,about_developers.class));
                        finish();
                    }
                };
                handler.postDelayed(pay_runnable,150);
                break;

            case R.id.nav_about_app:
                break;

            case R.id.nav_logout:
                afterRegistrationMainActivity.signOut();

                break;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.mydrawer);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(OpponentsActivity.this,OngoingPlayer.class));
        finish();
        super.onBackPressed();
    }
}
