package com.tachyon.techlabs.iplauction;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryListViewAdapter extends ArrayAdapter {

    private String [] listtext;
    private ArrayList<String> listName = new ArrayList<>();
    private List<Long> listValue = new ArrayList<>();
    private TextView txtText,txtValue;
    private String valueText="";

    public OrderHistoryListViewAdapter(@NonNull Context context, String[] text ,List<Long> listValue) {
        super(context, R.layout.custom_orders_listview,R.id.history_order_id,text);
        this.listtext = text;
        this.listValue = listValue;
       // this.listName = text;
        //this.listValue = value;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.custom_orders_listview,parent,false);
        txtText = row.findViewById(R.id.history_order_id);
        txtValue = row.findViewById(R.id.history_orderValue_id);

        //Toast.makeText(row.getContext(),listtext[1], Toast.LENGTH_SHORT).show();

        txtText.setText(listtext[position]);
        valueText = "₹ "+listValue.get(position);
        txtValue.setText(valueText);

        return row;

    }
}
