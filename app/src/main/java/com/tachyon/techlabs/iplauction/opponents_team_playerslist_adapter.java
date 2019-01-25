package com.tachyon.techlabs.iplauction;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class opponents_team_playerslist_adapter extends ArrayAdapter {

    private List<String> playername;
   // String point="33";
    private List<Long> playervalue=new ArrayList<>();
    private List<Integer> points=new ArrayList<>();
  //  private int [] points;
    private TextView txtplayer,txtValue,point;
    Object[] pv;
    private String valueText="";

    public opponents_team_playerslist_adapter(@NonNull Context context,List<String> text , List<Long> value, List<Integer> points) {
        super(context, R.layout.custom_listview_opponents_players,R.id.playertext,text);
        this.playername = text;
        this.playervalue = value;
        //pv=playervalue.toArray();
        Log.d("pv",playervalue+"");
        this.points=points;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.custom_listview_opponents_players,parent,false);
        txtplayer = row.findViewById(R.id.playertext);
        txtValue = row.findViewById(R.id.listvalue);
        point=row.findViewById(R.id.circulartext);
        txtplayer.setText(playername.get(position));
        //Toast.makeText(getContext(), playervalue.get(position)+"", Toast.LENGTH_SHORT).show();

        txtValue.setText("₹"+playervalue.get(position)+"");
        //Log.d("obj ",pv[position]+"");
        point.setText(points.get(position)+"");

       /* if(position<2)
        {
            valueText = "₹ "+listvalue[position];
        }
        else
        {
            valueText = listvalue[position]+"";
        }

        txtValue.setText(valueText);*/

        return row;
    }
}
