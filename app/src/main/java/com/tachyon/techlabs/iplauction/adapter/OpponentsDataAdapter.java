package com.tachyon.techlabs.iplauction.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tachyon.techlabs.iplauction.R;

public class OpponentsDataAdapter extends RecyclerView.Adapter<OpponentsDataAdapter.ViewHolder>{

    public Context context;
    public String [] teams;
    //public int [] players;
    public long [] balance;
    View view;
    ViewHolder viewHolder;
    CardView placeholder_team_cardview;
    Resources resources;
    TextView team_name_text;
    //Drawable drawable;
    //GradientDrawable gradientDrawable;

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView opp_team_name;
        //public TextView opp_team_player;
        public TextView opp_team_balance;
        public CardView opp_team_cardView;

        public ViewHolder(View view)
        {
            super(view);

            opp_team_name = (TextView) view.findViewById(R.id.player_name_mtda);
            //opp_team_player = (TextView) view.findViewById(R.id.player_oda_text);
            opp_team_balance = (TextView) view.findViewById(R.id.price_mtda);
            opp_team_cardView = (CardView) view.findViewById(R.id.custom_opponent_oda);
            placeholder_team_cardview = opp_team_cardView;
            team_name_text = opp_team_name;
        }

    }

    public OpponentsDataAdapter(Context context, String[] teams,long [] balance, Resources resources)
    {
        this.context = context;
        this.teams = teams;
        //this.players = players;
        this.balance = balance;
        this.resources = resources;
    }

    @NonNull
    @Override
    public OpponentsDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.opponents_data_adapter,parent,false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OpponentsDataAdapter.ViewHolder holder, int position) {
        holder.opp_team_name.setText(teams[position]);
        //holder.opp_team_player.setText(String.valueOf(players[position]));
        holder.opp_team_balance.setText(String.valueOf(balance[position]));


        switch (teams[position]) {
            case "MI":
            case "RR":
            case "DC":
                setBackColor(1,teams[position]);
                break;
            case "CSK":
            case "SH":
            case "GL":
                setBackColor(2,teams[position]);
                break;
            case "RCB":
            case "KXIP":
                setBackColor(3,teams[position]);
                break;
            case "KKR":
            case "RPS":
                setBackColor(4,teams[position]);
                break;
        }


    }

    public  void setBackColor(int num,String name)
    {
        switch (num) {
            case 1:
                placeholder_team_cardview.setCardBackgroundColor(resources.getColor(R.color.mi_opp_back));
                switch (name){
                    case "MI": team_name_text.setTextColor(resources.getColor(R.color.mi_text));
                        break;
                    case "RR": team_name_text.setTextColor(resources.getColor(R.color.rr_text));
                        break;
                    case "DC": team_name_text.setTextColor(resources.getColor(R.color.dc_text));
                        break;
                }

                break;
            case 2:
                placeholder_team_cardview.setCardBackgroundColor(resources.getColor(R.color.csk_opp_back));
                switch (name){
                    case "CSK": team_name_text.setTextColor(resources.getColor(R.color.csk_text));
                        break;
                    case "SH": team_name_text.setTextColor(resources.getColor(R.color.sh_text));
                        break;
                    case "GL": team_name_text.setTextColor(resources.getColor(R.color.gl_text));
                        break;
                }
                break;
            case 3:
                placeholder_team_cardview.setCardBackgroundColor(resources.getColor(R.color.rcb_opp_back));
                switch (name){
                    case "RCB": team_name_text.setTextColor(resources.getColor(R.color.rcb_text));
                        break;
                    case "KXIP": team_name_text.setTextColor(resources.getColor(R.color.kxip_text));
                        break;
                }
                break;
            case 4:
                placeholder_team_cardview.setCardBackgroundColor(resources.getColor(R.color.kkr_opp_back));
                switch (name){
                    case "KKR": team_name_text.setTextColor(resources.getColor(R.color.kkr_text));
                        break;
                    case "RPS": team_name_text.setTextColor(resources.getColor(R.color.rps_text));
                        break;
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return teams.length;
    }
}
