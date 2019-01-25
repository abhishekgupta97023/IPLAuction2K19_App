package com.tachyon.techlabs.iplauction.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tachyon.techlabs.iplauction.MyTeamActivity;
import com.tachyon.techlabs.iplauction.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MyTeamDataAdapter extends RecyclerView.Adapter<MyTeamDataAdapter.ViewHolder>{

    public Context context;
    public String [] players;
    //public long [] price;
    String user_email;
    int point1,point2,point3;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<Long> price;
    View view;
    MyTeamDataAdapter.ViewHolder viewHolder;
    CardView placeholder_player_cardview;
    Resources resources;
    TextView player_name_text;
    MyTeamActivity myTeamActivity = new MyTeamActivity();
    String id,story;
    int pos,phase;
    long pl_price;
    double loss,profit,tempcurr;
    SharedPreferences sp;
    SharedPreferences.Editor ed;
    int flag=0;
    View views;
    String team;

    class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView player_textview;
        public TextView price_textview;
        CardView player_cardview;
        public ViewHolder(View view)
        {
            super(view);

            views = view;

            player_textview = (TextView) view.findViewById(R.id.player_name_mtda);
            price_textview = (TextView) view.findViewById(R.id.price_mtda);
            player_cardview = (CardView) view.findViewById(R.id.custom_myteam_mtda);
            placeholder_player_cardview = player_cardview;

            player_cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pos = getAdapterPosition();
                    getPlayerInfo(players[pos]);
                  //  myTeamActivity.showAlert(pos,context);
                }
            });
        }
    }

    public void getPlayerInfo(String pl)
    {
        Log.d("platername",story);
        DocumentReference info_doc = db.collection(story).document(pl);
        info_doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                try
                {
                    point1 = Objects.requireNonNull(documentSnapshot.getLong("p1")).intValue();
                    point2 = Objects.requireNonNull(documentSnapshot.getLong("p2")).intValue();
                    point3 = Objects.requireNonNull(documentSnapshot.getLong("p3")).intValue();
                    flag=1;

//                    Log.d("points",point1+"");
//                    Log.d("points",point2+"");
//                    Log.d("points",point3+"");

                    showBox();
                }
                catch(Exception e)
                {
                    Toast.makeText(context, "Fixed players cannot be sold", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void showBox()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(views.getContext());
        builder.setTitle("Sell Player");
        builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, final int which) {
                user_email=Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
                DocumentReference docRef = db.collection("Players").document(user_email).collection("MyTeam").document("1");
                // Remove the 'capital' field from the document
                calculate(price.get(pos));
                Map<String,Object> updates = new HashMap<>();

                updates.put(players[pos], FieldValue.delete());
                docRef.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(context, "Successfully Sold", Toast.LENGTH_SHORT).show();

                    }
                });



            }
        });
        builder.setNegativeButton(R.string.CANCEL,null);
        builder.show();
    }

    private void calculate(Long aLong)
    {
        pl_price = aLong;

        DocumentReference documentReference = db.collection("Players").document(user_email);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                {
                    try
                    {
                        //id = documentSnapshot.getString("roomid");
                       double currentAmount = Objects.requireNonNull(documentSnapshot.getDouble("Current_Amount")).intValue();
                       double temp = Objects.requireNonNull(documentSnapshot.getDouble("temp_curr_amount")).intValue();
                       calculateProfitLoss(currentAmount,pl_price,temp);
                    }
                    catch(Exception e)
                    {
                      //  Toast.makeText(OngoingPlayer.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });



    /*    if(phase_current==1)
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
*/



    }

    public void calculateProfitLoss(double cur,long play,double tem)
    {
        if(phase==0)
        {
            int diff = point2 - point1;
            switch (diff)
            {
                case 0: loss = play;
                    tempcurr = tem + loss;
                    cur = cur + play;
                    break;
                case 1: loss = (0.9*play);
                        tempcurr = tem + loss;
                        Log.d("lossis",tempcurr+"");
                        cur = cur + play;
                        break;
                case 3: loss = (0.8*play);
                    tempcurr = tem + loss;
                    cur = cur + play;
                        break;
                case 5: loss = (0.7*play);
                    tempcurr = tem + loss;
                    cur = cur + play;
                        break;
                case -1: loss = (1.1*play);
                    tempcurr = tem + loss;
                    cur = cur + play;
                        break;
                case -3: loss = (1.2*play);
                    tempcurr = tem + loss;
                    cur = cur + play;
                        break;
                case -5: loss = (1.3*play);
                    tempcurr = tem + loss;
                    cur = cur + play;
                        break;
                default:break;
            }
        }
        else
        {
            int diff = point3 - point2;

            switch (diff)
            {
                case 0: loss = play;
                    tempcurr = tem + loss;
                    cur = cur + play;
                    break;
                case 1: loss = (0.9*play);
                    tempcurr = tem + loss;
                    Log.d("lossis",tempcurr+"");
                    cur = cur + play;
                break;
                case 3: loss = (0.8*play);
                    tempcurr = tem + loss;
                    cur = cur + play;
                    break;
                case 5: loss = (0.7*play);
                    tempcurr = tem + loss;
                    cur = cur + play;
                    break;
                case -1: loss = (1.1*play);
                    tempcurr = tem + loss;
                    cur = cur + play;
                    break;
                case -3: loss = (1.2*play);
                    tempcurr = tem + loss;
                    cur = cur + play;
                    break;
                case -5: loss = (1.3*play);
                    tempcurr = tem + loss;
                    cur = cur + play;
                    break;
                default:break;

            }


        }

        DocumentReference opp_doc = db.collection(id).document("Opponents");

        DocumentReference setCurr_doc = db.collection("Players").document(user_email);
        setCurr_doc.update("temp_curr_amount",tempcurr);
        setCurr_doc.update("Current_Amount",cur);
        opp_doc.update(team,cur);


        //Log.d("tempamount",tempcurr+"");
        //Log.d("tempamount",cur+"");
    }

    public MyTeamDataAdapter(Context context, String[] players,List<Long> price, Resources resources,String id,String story,int phase,String team)
    {
        this.context = context;
        this.players = players;
        this.price = price;
        this.resources = resources;
        this.id = id;
        this.story = story;
        this.phase = phase;
        this.team = team;
        sp= context.getSharedPreferences("Story", 0); // 0 - for private mode
        this.story = sp.getString("Story","");
        Log.d("phasesad",this.phase+"");
    }

    @NonNull
    @Override
    public MyTeamDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_team_data_adapter,parent,false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyTeamDataAdapter.ViewHolder holder, int position) {
        holder.player_textview.setText(players[position]);
        holder.price_textview.setText(String.valueOf(price.get(position)));

    }

    @Override
    public int getItemCount() {
        return players.length;
    }


}
