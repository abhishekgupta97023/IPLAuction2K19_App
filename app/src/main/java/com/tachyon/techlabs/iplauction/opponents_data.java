package com.tachyon.techlabs.iplauction;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class opponents_data extends AppCompatActivity {
    private static final String[][] DATA_TO_SHOW = { { "This", "is", "a", "test" },
            { "and", "a", "second", "test" } };
    private static final String[] TABLE_HEADERS = { "This", "is", "a", "test" };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opponents_data);
        SortableTableView<String[]> tableView = findViewById(R.id.tableView);
        tableView.setDataAdapter(new SimpleTableDataAdapter(this, DATA_TO_SHOW));
        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(this, TABLE_HEADERS));
       // tableView.setColumnComparator(0, new Team_compare());
    }
}
