package com.tachyon.techlabs.iplauction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nullable;

public class WaitingForPlayersActivity extends AppCompatActivity {

    Toolbar toolbarRoomWait;
    TextView textViewRoomName;
    String member,roomid,key,boss_namee;
    String boss,roomtext;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth;
    TextView bossTextView,joinCodeDisplay,boss_name;
    ListView members_joined;
    List<String> list;
    ArrayAdapter<String> adapter;
    String [] players;
    Button startgame;
    int value,storyFlag=0;
    Map<String, Object> gamestart = new HashMap<>();
    Map<String, String> teammap = new HashMap<>();
    Map<String, Integer> opp_bugdet = new HashMap<>();
    Map<String, Integer> statemap = new HashMap<>();
    Map<String, Integer> opp_map = new HashMap<>();
    String [] teams,story;
    List<String> teamnames,storynames,opp;
    List<Integer> money;
    String myteam,teamuser,selectedStory="";
    private ValueEventListener mReferenceListener;
    DocumentReference teamdoc;
    DocumentReference start_game;
    Query query;
    ListenerRegistration registration;
    int idx;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_for_players);
        startgame=findViewById(R.id.button_start_game);

        member = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        teams = getResources().getStringArray(R.array.team_names);
        teamnames = new ArrayList<>(Arrays.asList(teams));
        story = getResources().getStringArray(R.array.story_lines);
        storynames = new ArrayList<>(Arrays.asList(story));
        money = new ArrayList<>();
        //teamnames = Arrays.asList(teams);

        DocumentReference docname = db.collection("Players").document(member);

       docname.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if(documentSnapshot.exists())
                {
                    try
                    {
                        roomid = documentSnapshot.getString("roomid");
                        key = documentSnapshot.getString("joinkey");
                        boss_namee = documentSnapshot.getString("Owner");
                        //getBossName();
                        setTexts();
                        getPlayersName();
                    }
                    catch(Exception e)
                    {
                        Toast.makeText(WaitingForPlayersActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        members_joined= (ListView) findViewById(R.id.waiting_player_listview);

        roomtext = getString(R.string.yourroom);
        toolbarRoomWait = findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbarRoomWait);
        Objects.requireNonNull(getSupportActionBar()).setTitle(roomtext);
        /*
        textViewRoomName = findViewById(R.id.app_toolbar_nametxt);
        textViewRoomName.setText(roomtext);
        textViewRoomName.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) textViewRoomName.getLayoutParams();
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        textViewRoomName.setLayoutParams(lp);
        */

        bossTextView = findViewById(R.id.boss_text);
        boss_name = findViewById(R.id.boss_name);

        joinCodeDisplay = findViewById(R.id.join_code_display);


       // assert roomid != null;
        /*
        DocumentReference docRef = db.collection(roomid).document(member);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                boss = documentSnapshot.getString("Owner");
                setTexts();
            }
        });*/


    }

    public void setPlayerNames()
    {
        players = new String[list.size()];
        players = list.toArray(players);
       // Toast.makeText(this, players.toString(), Toast.LENGTH_SHORT).show();
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,players);
        members_joined.setAdapter(adapter);

        members_joined.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                myteam = teams[0];
                teamuser = list.get(position);
                if(boss_namee.equals("true"))
                    setTeam();
            }
        });
    }

    public  void setTeam()
    {

        if(teamuser.equals(member))
        {
            storyline_selector_dialog();
        }
        else
        {
            teams = teamnames.toArray(new String[0]);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select Your Team");
            builder.setSingleChoiceItems(teams, 0, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    myteam = teamnames.get(which);
                }
            });
            builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, final int which) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(WaitingForPlayersActivity.this);
                    builder.setTitle("Confirm Team");
                    builder.setMessage("Are You Sure ?");
                    builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setPlayerTeam();


                        }
                    });
                    builder.setNegativeButton(R.string.no,null);
                    builder.show();



                /*
                final LinearLayout lyt_progress = (LinearLayout) findViewById(R.id.lyt_progress);
                lyt_progress.setVisibility(View.VISIBLE);
                DocumentReference documentReference = db.collection("Players").document(teamuser);
                documentReference.update("myteam",myteam).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //teamnames.remove(myteam);
                        lyt_progress.setVisibility(View.INVISIBLE);
                    }
                });
                teamnames.remove(myteam);
                Log.d("teanmane123",teamnames.toString());
                //onResume();*/
                }
            });
            builder.setNegativeButton(R.string.CANCEL,null);
            builder.show();
        }

    }

    public void setPlayerTeam()
    {
        //final LinearLayout lyt_progress = (LinearLayout) findViewById(R.id.lyt_progress);
        //lyt_progress.setVisibility(View.VISIBLE);
        //DocumentReference documentReference = db.collection("Players").document(teamuser);
        teamdoc = db.collection("Players").document(teamuser);
        //teammap.clear();
        //teammap.put(teamuser,myteam);
        teamdoc.update("myteam",myteam).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                teamnames.remove(myteam);
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(WaitingForPlayersActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setTexts()
    {
        joinCodeDisplay.setText(key);

        if(boss_namee.equals("true"))
        {
            startgame.setVisibility(View.VISIBLE);
            bossTextView.setText(R.string.bosstextview);
            boss_name.setVisibility(View.GONE);

        }
        else
        {
          //  String text = R.string.membertextview+"";
            boss_name.setText(R.string.placeholder);
            bossTextView.setText(R.string.placeholder);
            startgame.setVisibility(View.GONE);
        }
    }

    public void getBossName()
    {
        DocumentReference doc = db.collection("keyValues").document(key);
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                {
                    try
                    {
                        boss_namee = documentSnapshot.getString("owner");
                        setTexts();
                    }
                    catch(Exception e)
                    {
                        Toast.makeText(WaitingForPlayersActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }



    public void getPlayersName()
    {
        db.collection(roomid).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                list = new ArrayList<>();
                for (QueryDocumentSnapshot document : Objects.requireNonNull(queryDocumentSnapshots)) {
                    if(!document.getId().equals("CurrentPlayer") && !document.getId().equals("START GAME") && !document.getId().equals("Story") && !document.getId().equals("State") && !document.getId().equals("Opponents"))
                    list.add(document.getId());
                    }
                // Log.d(TAG, list.toString());
                // Toast.makeText(WaitingForPlayersActivity.this, list.toString(), Toast.LENGTH_SHORT).show();
                setPlayerNames();

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        DocumentReference docname = db.collection("Players").document(member);

        docname.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                {
                    try
                    {
                        String temp_roomid = documentSnapshot.getString("roomid");
                        key = documentSnapshot.getString("joinkey");
                        getstartgame_status(temp_roomid,boss_namee);
                    }
                    catch(Exception e)
                    {
                        Toast.makeText(WaitingForPlayersActivity.this,e.toString() , Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });
           //getstartgane_status(roomid);







    }

    public void  getstartgame_status(final String roomid,final  String bossName)
    {

        start_game = db.collection(roomid).document("START GAME");
        query = db.collection(roomid);

        start_game.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (Objects.requireNonNull(documentSnapshot).exists()) {

                    try
                    {
                        value = Objects.requireNonNull(documentSnapshot.getLong("start")).intValue();
                        if(value==1)
                        {
                            Log.d("bossnameiswhat",value+"");
                            Toast.makeText(WaitingForPlayersActivity.this, value+"", Toast.LENGTH_SHORT).show();
                            //startActivity(new Intent(WaitingForPlayersActivity.this,Start_Game.class));
                            if(bossName.equals("true"))
                            {
                                Log.d("bosssnameiswhat",bossName);
                                Intent admin = new Intent(WaitingForPlayersActivity.this,AdminOngoingPlayer.class);
                                //admin.putExtra("roomid",roomid);
                                //admin.putExtra("userEmail",member);
                                //admin.putExtra("boss_name",boss_namee);
                                startActivity(admin);
                                finish();
                            }
                            else
                            {
                                Log.d("bosssnameiswhat",bossName);
                                Intent member = new Intent(WaitingForPlayersActivity.this,OngoingPlayer.class);
                                //member.putExtra("roomid",roomid);
                                //member.putExtra("userEmail",member);
                                //member.putExtra("boss_name",boss_namee);
                                startActivity(member);
                                finish();
                            }

                        }
                        else
                        {
                            Log.d("bosssnameiswhat",value+"");
                            Toast.makeText(WaitingForPlayersActivity.this, value+"", Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch(Exception exp)
                    {
                        Toast.makeText(WaitingForPlayersActivity.this,exp.toString() , Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    public void qr_code(View view) {
        Intent qrcode=new Intent(this,qr_code_generator.class);
        qrcode.putExtra("Join Code",key);
        startActivity(qrcode);
        finish();

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit Room?");
        builder.setMessage("Do you really wish to leave the room?");
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db.collection(roomid).document(member).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if(boss_namee.equals("true"))
                        {
                            db.collection(roomid).document("CurrentPlayer").delete();
                        }

                        DocumentReference updateRef = db.collection("Players").document(member);
                        updateRef.update("inRoom",0).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                startActivity(new Intent(WaitingForPlayersActivity.this,AfterRegistrationMainActivity.class));
                                finish();
                                //Toast.makeText(WaitingForPlayersActivity.this, "Left the room", Toast.LENGTH_SHORT).show();
                                //Toast.makeText(WaitingForPlayersActivity.this, "User details updated", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(WaitingForPlayersActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        builder.setNegativeButton(R.string.no,null);
        builder.show();
    }

    public void start_game(View view) {
        Intent admin = new Intent(WaitingForPlayersActivity.this,AdminOngoingPlayer.class);
        //admin.putExtra("roomid",roomid);
        //admin.putExtra("userEmail",member);
        //admin.putExtra("boss_name",boss_namee);

        //storyline_selector_dialog();
        //getOpponetsBudget(1);
        //setOpponentsBudget(0);
        startActivity(admin);

        setStart();

        finish();

        /*
        if(storyFlag==1)
        {


        }

        */


        Log.d("startgameintent","called start game ");


    }

    public void storyline_selector_dialog() {

        //String [] storyline = {"StoryLine 1","StoryLine 2"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Storyline");
        builder.setSingleChoiceItems(story, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //List<Integer> list=new ArrayList<>();
                //list.add(1);
                //list.add(2);
                //list.add(3);
                selectedStory = storynames.get(which);
            }
        });
        builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, final int which) {
                    setStory();
                }
        });

        //builder.create();
        builder.show();

    }

    public void setStory()
    {
        DocumentReference story_doc = db.collection(roomid).document("Story");
        teammap.clear();
        teammap.put("story",selectedStory);
        story_doc.set(teammap);

        DocumentReference state = db.collection(roomid).document("State");
        statemap.put("state",0);
        statemap.put("phase",0);
        state.set(statemap);

        DocumentReference opp = db.collection(roomid).document("Opponents");
        opp_map.clear();
        opp_map.put("MI",800000000);
        opp_map.put("CSK",800000000);
        opp_map.put("RCB",800000000);
        opp_map.put("DD",800000000);
        opp_map.put("RR",800000000);
        opp_map.put("GL",800000000);
        opp_map.put("SH",800000000);
        opp_map.put("KXIP",800000000);
        opp_map.put("KKR",800000000);
        opp_map.put("RPS",800000000);
        opp.set(opp_map);

    }

    public void getOpponetsBudget(int index)
    {
        idx = index;
        DocumentReference getPrice = db.collection("Players").document(list.get(index));
        getPrice.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                {
                    try
                    {
                        money.add(Objects.requireNonNull(documentSnapshot.getLong("Current_Amount")).intValue());
                        opp.add(documentSnapshot.getString("myteam"));
                        if(idx<list.size())
                        {
                            getOpponetsBudget(idx+1);
                        }
                    }
                    catch(Exception e)
                    {
                        Toast.makeText(WaitingForPlayersActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
    public void setOpponentsBudget(int num)
    {
        //int i = num;
        DocumentReference opponent_doc = db.collection(roomid).document("Opponents Budget");
        for(int i=num;i<list.size();i++)
        {
            opp_bugdet.put(opp.get(num),money.get(num));
        }

        opponent_doc.set(opp_bugdet).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(WaitingForPlayersActivity.this, "Budget not set successfully", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void setStart()
    {
        gamestart.clear();
        gamestart.put("start",1);
        DocumentReference start_game = db.collection(roomid).document(Objects.requireNonNull("START GAME"));
        start_game.set(gamestart);
    }

    @Override
    protected void onPause() {
//        query = db.collection(roomid);
//        registration= query.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//
//            }
//        });
//        registration.remove();
//        query = db.collection("Players");
//        registration = query.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//
//            }
//        });
//        registration.remove();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
//        registration= query.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//
//            }
//        });
//        registration.remove();
//        query = db.collection("Players");
//        registration = query.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//
//            }
//        });
//        registration.remove();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
