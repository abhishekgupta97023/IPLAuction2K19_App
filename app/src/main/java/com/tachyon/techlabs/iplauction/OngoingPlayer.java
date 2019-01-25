package com.tachyon.techlabs.iplauction;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

import javax.annotation.Nullable;

public class OngoingPlayer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    TextView name1text,name2text,pointtext,matchtext,runtext,wickettext,basetext;
    DrawerLayout mDrawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle mToggle;
    Toolbar ongoing_toolbar;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String userEmail;
    int phase;
    int point;
    int currentAmount;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String id,boss_name;
    int current;
    Bundle extras;
    AfterRegistrationMainActivity afterRegistrationMainActivity = new AfterRegistrationMainActivity();
    TextView toolbar_text;
    FirebaseStorage storage;
    StorageReference storageRef;
    ImageView player_img;
    Handler handler;
    Runnable runnable;
    DocumentReference curr_doc;
    Query query;
    ListenerRegistration registration;
    String storyline,currentPlayer;
    DocumentReference ongoing_doc;
    String fname,sname,color,fullname;
    int point1,point2,point3;
    String base_price;
    ImageView point_back;
    Drawable drawable;
    GradientDrawable gradientDrawable;
    TextView my_team;
    String [] bp;
    double multiplier;
    int s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ongoing_player);

        storage = FirebaseStorage.getInstance();
        // Create a storage reference from our app
        storageRef = storage.getReference();

        name1text = findViewById(R.id.player_name1);
        name2text = findViewById(R.id.player_name2);
        pointtext = findViewById(R.id.point_text);
        point_back = findViewById(R.id.player_point_back);
        drawable = getDrawable(R.drawable.csk_point_back_circle);
        gradientDrawable = (GradientDrawable) drawable;
        //matchtext = findViewById(R.id.matchtextvalue);
        //runtext = findViewById(R.id.runstextvalue);
        //wickettext = findViewById(R.id.wicketstextvalue);
        basetext = findViewById(R.id.base_price_value);
        player_img = findViewById(R.id.player_img);
        extras = getIntent().getExtras();

        //my_team = findViewById(R.id.my_team_op);

/*        my_team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OngoingPlayer.this,MyTeamActivity.class));
                finish();
            }
        });
*/
        //toolbar_text = findViewById(R.id.app_toolbar_nametxt);

        ongoing_toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(ongoing_toolbar);
        //toolbar_text.setText(R.string.ongoing_player);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.placeholder);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        //getSupportActionBar().setHomeAsUpIndicator(getDrawable(android.R.drawable.ic_menu_close_clear_cancel));


        mDrawerLayout = (DrawerLayout) findViewById(R.id.mydrawer);
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,ongoing_toolbar,R.string.open,R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        NavigationView nav = (NavigationView) findViewById(R.id.navigation_view);
        //View headerView = navigationView.getHeaderView(0);

        //String name = getIntent().getExtras().getString("name");
        nav.setNavigationItemSelectedListener(this);

        handler = new Handler();

        getId();
    }

    private void readphase() {

        DocumentReference documentReference = db.collection(id).document("State");
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                {
                    try
                    {
                        String phase_state = documentSnapshot.getString("phase");


                    }
                    catch(Exception e)
                    {
                      //  Toast.makeText(OngoingPlayer.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }

    public void getId()
    {

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        userEmail = Objects.requireNonNull(currentUser).getEmail();

        /*
        id = extras.getString("roomid");
        userEmail = extras.getString("userEmail");
        boss_name = extras.getString("boss_name");
        getCurrent();
        */

        DocumentReference documentReference = db.collection("Players").document(userEmail);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
         @Override
         public void onSuccess(DocumentSnapshot documentSnapshot) {
             if (documentSnapshot.exists()) {
                 try {
                     id = documentSnapshot.getString("roomid");
                     getStatePlayer();
                     //  getphase_state();
                     getStoryLine();

                 } catch (Exception e) {
                     //Toast.makeText(OngoingPlayer.this, e.toString(), Toast.LENGTH_SHORT).show();
                 }
             }

         }
     });
    }

        /*    private void getphase_state() {

                DocumentReference doc = db.collection(id).document("State");
                doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        phase = Objects.requireNonNull(documentSnapshot.getLong("phase")).intValue();
                    }
                });
                logic_for_phases(phase);
            }

            private void logic_for_phases(int phase_current) {
                DocumentReference documentReference = db.collection("Players").document(userEmail);
                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists())
                        {
                            try
                            {
                                //id = documentSnapshot.getString("roomid");
                                currentAmount = Objects.requireNonNull(documentSnapshot.getLong("Current_Amount")).intValue();
                               }
                            catch(Exception e)
                            {
                                Toast.makeText(OngoingPlayer.this, e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });


                if(phase_current==1)
                {
                    int diff = point2 - point1;
                    switch (diff)
                    {
                        case 1: loss=(0.9*);


                        case  3:

                            loss=(0.2*);
                        case 5:
                            loss=(0.3*);
                    }
                }
                else
                {
                    int diff = point3 - point2;

                    switch (diff)
                    {


                        case 1: profit=(0.1*);


                        case  3: profit=(0.2*);


                        case 5:profit =(0.3*);

                    }


                }


        }

*/

    public void getStoryLine()
    {
        DocumentReference story_ref = db.collection(id).document("Story");
        story_ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                {
                    try
                    {
                        storyline = Objects.requireNonNull(documentSnapshot.get("story")).toString();
                        getCurrent();
                    }
                    catch(Exception e)
                    {
                       // Toast.makeText(OngoingPlayer.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }

    public void getCurrent()
    {
        curr_doc = db.collection(id).document("CurrentPlayer");
        curr_doc.addSnapshotListener(OngoingPlayer.this,new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists())
                {
                    try
                    {
                        currentPlayer = Objects.requireNonNull(Objects.requireNonNull(documentSnapshot).get("curr")).toString();
                        if(!currentPlayer.equals(""))
                        getTextPlayer();
                    }
                    catch(Exception exp)
                    {
                        exp.printStackTrace();
                       // Toast.makeText(OngoingPlayer.this, exp.toString(), Toast.LENGTH_SHORT).show();
                    }
                }

                //Toast.makeText(OngoingPlayer.this, current+"", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void getStatePlayer()
    {
        DocumentReference doc = db.collection(id).document("State");
        doc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                s = Objects.requireNonNull(Objects.requireNonNull(documentSnapshot).getLong("state")).intValue();
            }
        });

    }

    public void getTextPlayer()
    {

        /*
        String fullname = allPlayerInfo.fullname[current]+"";
        String pt = allPlayerInfo.points[current]+"";
        String match = allPlayerInfo.match[current]+"";
        String run = allPlayerInfo.run[current]+"";
        String wicket = allPlayerInfo.wicket[current]+"";
        String base = allPlayerInfo.basecost[current]+"";
        name1text.setText(allPlayerInfo.fname[current]);
        name2text.setText(allPlayerInfo.sname[current]);
        pointtext.setText(pt);
        //matchtext.setText(match);
        //runtext.setText(run);
        //wickettext.setText(wicket);
        basetext.setText(base);

        */
        if(s==0)
        {
            ongoing_doc = db.collection(storyline).document(currentPlayer);
            ongoing_doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.exists())
                    {
                        try
                        {
                            fname = Objects.requireNonNull(documentSnapshot.getString("fname")).toString();
                            sname = Objects.requireNonNull(documentSnapshot.getString("sname")).toString();
                            point1 = Objects.requireNonNull(documentSnapshot.getLong("p1")).intValue();
                            point2 = Objects.requireNonNull(documentSnapshot.getLong("p2")).intValue();
                            point3 = Objects.requireNonNull(documentSnapshot.getLong("p3")).intValue();

                            //base_price = Objects.requireNonNull(documentSnapshot.getLong("Price")).intValue();
                            base_price = documentSnapshot.getString("Price");
                            //color = Objects.requireNonNull(documentSnapshot.getString("color")).toString();
                            setTextPlayer();
                        }
                        catch(Exception e)
                        {
                           // Toast.makeText(OngoingPlayer.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }


                }
            });
        }
        else if(s==1)
        {
            ongoing_doc = db.collection("Fixed Players").document(currentPlayer);
            ongoing_doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.exists())
                    {
                        try
                        {
                            fname = Objects.requireNonNull(documentSnapshot.getString("fname")).toString();
                            sname = Objects.requireNonNull(documentSnapshot.getString("sname")).toString();
                            point1 = Objects.requireNonNull(documentSnapshot.getLong("p")).intValue();
                            //base_price = Objects.requireNonNull(documentSnapshot.getLong("Price")).intValue();
                            base_price = documentSnapshot.getString("price");
                            //color = Objects.requireNonNull(documentSnapshot.getString("color")).toString();
                            setTextPlayer();
                        }
                        catch(Exception e)
                        {
                         //   Toast.makeText(OngoingPlayer.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }


                }
            });
        }



        //storageRef.child(fullname+".png");
        //GlideApp.with(OngoingPlayer.this).load(storageRef).into(player_img);
    }

    public void setTextPlayer()
    {
        name1text.setText(fname);
        name2text.setText(sname);
        String pricebase = base_price;
        base_price = base_price.replace(" ","#");
        bp = base_price.split("#");
        String mul = bp[1].toLowerCase();
        String total;
        double basep;

        if(mul.equals("cr"))
        {
            multiplier = 10000000;
            basep = Double.parseDouble(bp[0]) * multiplier;
            total = basep+"";
        }
        else if(mul.equals("lakhs"))
        {
            multiplier = 100000;
            basep =  Double.parseDouble(bp[0]) * multiplier;
            total = basep+"";
        }
        pointtext.setText(String.valueOf(point1));
        basetext.setText(pricebase);

        /*
        switch (color)
        {
            case "rcb" : point_back.setBackgroundResource(R.drawable.rcb_point_back_circle);
                break;
            case "mi" : point_back.setBackgroundResource(R.drawable.mi_point_back_circle);
                break;
            case "csk" : point_back.setBackgroundResource(R.drawable.csk_point_back_circle);
                break;
            case "rr" : point_back.setBackgroundResource(R.drawable.rr_point_back_circle);
                break;
            case "dc" : point_back.setBackgroundResource(R.drawable.dc_point_back_circle);
                break;
            case "kkr" : point_back.setBackgroundResource(R.drawable.kkr_point_back_circle);
                break;
            case "sh" : point_back.setBackgroundResource(R.drawable.sh_point_back_circle);
                break;
            case "kxip" : point_back.setBackgroundResource(R.drawable.kxip_point_back_circle);
                break;
            case "gl" : point_back.setBackgroundResource(R.drawable.gl_point_back_circle);
                break;
            case "rps" : point_back.setBackgroundResource(R.drawable.rps_point_back_circle);
                break;
            default:break;
        }*/
        //
        //fname = fname.replace(" ","");
        sname = sname.replace(" ","");
        fullname = fname.toLowerCase()+""+sname.toLowerCase();
        Log.d("playername",fullname);

        storageRef.child(fullname+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d("playerimg",uri.toString());
                Glide.with(getApplicationContext()).load(uri.toString()).apply(RequestOptions.circleCropTransform()).into(player_img);
                //GlideApp.with(OngoingPlayer.this).load(storageRef).into(player_img);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("playerimg","fail");
                        Toast.makeText(OngoingPlayer.this, "Not able to load player image", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id = menuItem.getItemId();

        switch (id)
        {
            case R.id.nav_home:
                break;

            case R.id.nav_players:
                //startActivity(new Intent(OngoingPlayer.this,PLAYERS.class));
                //finish();
                break;

            case R.id.nav_profile:
                //Intent prof = new Intent(AfterRegistrationMainActivity.this,ProfileActivity.class);
                //Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(OngoingPlayer.this,ProfileActivity.class));
                        finish();
                    }
                };
                handler.postDelayed(runnable,150);

                break;

            case R.id.nav_opponents:
                Runnable opp_runnable = new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(OngoingPlayer.this,OpponentsActivity.class));
                        finish();
                    }
                };
                handler.postDelayed(opp_runnable,150);
                break;

            case R.id.nav_payments_info:
                Runnable dev_runnable = new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(OngoingPlayer.this,PaymentInfo.class));
                        finish();
                    }
                };
                handler.postDelayed(dev_runnable,150);
                break;
            case R.id.reveal:
                Runnable rev_runnable = new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(OngoingPlayer.this,Reveal.class));
                        finish();
                    }
                };
                handler.postDelayed(rev_runnable,150);
                break;


            case R.id.nav_cards:
                //afterRegistrationMainActivity.storagepermission();
                storagepermission();

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
                        startActivity(new Intent(OngoingPlayer.this,about_developers.class));
                        finish();
                    }
                };
                handler.postDelayed(pay_runnable,150);
                break;

            case R.id.nav_about_app:
                Runnable about_runnable = new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(OngoingPlayer.this,About.class));
                        finish();
                    }
                };
                handler.postDelayed(about_runnable,150);
                break;


            case R.id.nav_logout:
                afterRegistrationMainActivity.signOut();

                break;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.mydrawer);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void storagepermission()
    {

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
        }
        else if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED)
        {
            Handler handlerCards = new Handler();
            Runnable runnableCards = new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(OngoingPlayer.this,PowerCards.class));
                    finish();
                }
            };
            handlerCards.postDelayed(runnableCards,250);

        }
        else {
            Handler handlerCards = new Handler();
            Runnable runnableCards = new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(OngoingPlayer.this,PowerCards.class));
                    finish();
                }
            };
            handlerCards.postDelayed(runnableCards,250);


        }
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
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START))
        {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Exit Game?");
            builder.setMessage("Do you really wish to leave the room and exit?");
            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    db.collection(id).document(userEmail).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            DocumentReference updateRef = db.collection("Players").document(userEmail);
                            updateRef.update("inRoom", 0).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Handler handler = new Handler();
                                    Runnable runnable = new Runnable() {
                                        @Override
                                        public void run() {
                                            startActivity(new Intent(OngoingPlayer.this, AfterRegistrationMainActivity.class));
                                            finish();
                                        }
                                    };
                                    handler.postDelayed(runnable, 500);
                                    //finish();
                                    //Toast.makeText(WaitingForPlayersActivity.this, "Left the room", Toast.LENGTH_SHORT).show();
                                    //Toast.makeText(WaitingForPlayersActivity.this, "User details updated", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }                     //DocumentReference start_game = db.collection(id).document(Objects.requireNonNull("START GAME"));
                    });
                }
            });
            builder.setNegativeButton(R.string.no,null);
            builder.show();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
//        query = db.collection(id);
//        registration = query.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//
//            }
//        });
//        if(registration!=null){
//            registration.remove();
//        }


    }

    @Override
    protected void onDestroy() {
//        query = db.collection(id);
//        registration = query.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//
//            }
//        });
//        registration.remove();
        super.onDestroy();
    }
}

