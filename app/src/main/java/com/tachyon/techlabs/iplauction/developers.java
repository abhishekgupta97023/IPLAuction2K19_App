package com.tachyon.techlabs.iplauction;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

//import com.ramotion.foldingcell.FoldingCell;

public class developers extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developers);

        if(Build.VERSION.SDK_INT>22)
        {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        TextView textViewRoomName = findViewById(R.id.app_toolbar_nametxt);
        textViewRoomName.setText("Developers");


        TextView pranjalc = (TextView)findViewById(R.id.pranjal);
        TextView ag = (TextView)findViewById(R.id.abhishek);
        TextView pranjalcdev = (TextView)findViewById(R.id.developer1);
        TextView agddev = (TextView)findViewById(R.id.developer2);

        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/allura_regular.otf");

       // pranjalc.setTypeface(custom_font);
        //ag.setTypeface(custom_font);
        pranjalcdev.setTypeface(custom_font);
        agddev.setTypeface(custom_font);




/*
        // get our folding cell
        final FoldingCell fc = (FoldingCell) findViewById(R.id.folding_cell);
        final FoldingCell pc = (FoldingCell) findViewById(R.id.folding_cell_2);
    //    final FoldingCell pc = (FoldingCell) findViewById(R.id.folding_cell_pranjal);
        // attach click listener to folding cell
        fc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fc.toggle(false);
                fc.initialize(30, 2000, Color.WHITE, 3);
            }
        });
        pc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pc.toggle(false);
                pc.initialize(30, 2000, Color.WHITE, 3);
            }
        });

     /*   pc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pc.toggle(false);
            }
        }); */
    }
}
