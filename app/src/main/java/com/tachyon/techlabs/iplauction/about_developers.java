package com.tachyon.techlabs.iplauction;

import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

public class about_developers extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout mDrawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle mToggle;
    Toolbar dev_toolbar;
    CardView dev1_card,dev2_card;
    ImageView dev1_img,dev2_img;
  //  private DrawerLayout mDrawerLayout;
   // private ActionBarDrawerToggle mToggle;
 //   private NavigationView navigationView;

    TextView dev1_name_text,dev2_name_text,dev1_desc_text,dev2_desc_text;
    ConstraintLayout layout;
    ConstraintSet constraintSetOld = new ConstraintSet();
    ConstraintSet constraintSetNew = new ConstraintSet();
    boolean alt;
    AfterRegistrationMainActivity afterRegistrationMainActivity = new AfterRegistrationMainActivity();
    OngoingPlayer ongoingPlayer = new OngoingPlayer();
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_developers);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.mydrawer);
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);

//        mDrawerLayout.addDrawerListener(mToggle);
  //      mToggle.syncState();


        navigationView = (NavigationView) findViewById(R.id.nav_view);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.oneui_grey_back));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        dev1_card = findViewById(R.id.card_dev1);
        dev2_card = findViewById(R.id.card_dev2);
        dev1_img = findViewById(R.id.dev1_main_img);
        dev2_img = findViewById(R.id.dev2_main_img);
        dev1_name_text = findViewById(R.id.dev1_main_name);
        dev2_name_text = findViewById(R.id.dev2_main_name);
        dev1_desc_text = findViewById(R.id.dev1_main_descc);
        dev2_desc_text = findViewById(R.id.dev2_main_descc);

        dev_toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(dev_toolbar);
        //toolbar_text.setText(R.string.ongoing_player);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.placeholder);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.mydrawer);
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,dev_toolbar,R.string.open,R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        NavigationView nav = (NavigationView) findViewById(R.id.navigation_view);
        //View headerView = navigationView.getHeaderView(0);

        //String name = getIntent().getExtras().getString("name");
        nav.setNavigationItemSelectedListener(this);

        /*

        layout = findViewById(R.id.dev_layout);
        constraintSetOld.clone(layout);
        constraintSetNew.clone(this,R.layout.pranjal_developer);

        dev1_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(layout);

                if(!alt)
                {
                    constraintSetNew.applyTo(layout);
                    alt = true;
                }
                else
                {
                    constraintSetOld.applyTo(layout);
                    alt = false;
                }
            }
        });
        */

        dev1_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharedIntent = new Intent(about_developers.this,PranjalActivity.class);

                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View,String>(dev1_img,"dev_image_transition");
                //pairs[1] = new Pair<View,String>(dev1_name_text,"dev_name_transition");
                //pairs[2] = new Pair<View,String>(dev1_desc_text,"dev_desc_transition");

                //ReflowText.addExtras(sharedIntent,new ReflowText.ReflowableTextView(dev1_name_text));

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(about_developers.this,pairs);

                startActivity(sharedIntent,options.toBundle());
            }
        });

        dev2_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharedIntent = new Intent(about_developers.this,AbhishekActivity.class);

                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View,String>(dev2_img,"dev_image_transition");
                //pairs[1] = new Pair<View,String>(dev2_name_text,"dev_name_transition");
                //pairs[2] = new Pair<View,String>(dev2_desc_text,"dev_desc_transition");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(about_developers.this,pairs);

                startActivity(sharedIntent,options.toBundle());
            }
        });


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
                        startActivity(new Intent(about_developers.this,OngoingPlayer.class));
                        finish();
                    }
                };
                handler.postDelayed(player_runnable,150);
                //startActivity(new Intent(about_developers.this,PLAYERS.class));
                //finish();
                break;

            case R.id.nav_profile:
                //Intent prof = new Intent(AfterRegistrationMainActivity.this,ProfileActivity.class);
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(about_developers.this,ProfileActivity.class));
                        finish();
                    }
                };
                handler.postDelayed(runnable,150);

                break;

            case R.id.nav_opponents:
                Runnable opp_runnable = new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(about_developers.this,OpponentsActivity.class));
                        finish();
                    }
                };
                handler.postDelayed(opp_runnable,150);
                break;

            case R.id.reveal:
                Runnable rev_runnable = new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(about_developers.this,Reveal.class));
                        finish();
                    }
                };
                handler.postDelayed(rev_runnable,150);
                break;

            case R.id.nav_payments_info:
                Runnable dev_runnable = new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(about_developers.this,PaymentInfo.class));
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
                //startActivity(new Intent(about_developers.this,about_developers.class));
                //finish();
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
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START))
        {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            startActivity(new Intent(about_developers.this,OngoingPlayer.class));
            finish();
        }
    }

    /*
    @Override
    public void onBackPressed() {


        if(this.mDrawerLayout.isDrawerOpen(GravityCompat.START))
        {
            this.mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            startActivity(new Intent(about_developers.this,OngoingPlayer.class));
            finish();
        }
        super.onBackPressed();

    }

    */
}
