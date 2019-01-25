package com.tachyon.techlabs.iplauction;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileListViewAdapter extends ArrayAdapter {

    private String [] listtext;
    private int [] listvalue;
    private TextView txtText,txtValue;
    private String valueText="";

    public ProfileListViewAdapter(@NonNull Context context, String [] text ,int [] value) {
        super(context, R.layout.custom_listview_profile,R.id.listtext,text);
        this.listtext = text;
        this.listvalue = value;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.custom_listview_profile,parent,false);
        txtText = row.findViewById(R.id.listtext);
        txtValue = row.findViewById(R.id.listvalue);
        Toast.makeText(row.getContext(), listtext[1], Toast.LENGTH_SHORT).show();
        txtText.setText(listtext[position]);
        if(position==2 || (position==3))
        {
            valueText = "₹ "+listvalue[position];
        }
        else
        {
            valueText = listvalue[position]+"";
        }

        txtValue.setText(valueText);

        return row;
    }
}
