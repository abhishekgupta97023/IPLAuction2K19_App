package com.tachyon.techlabs.iplauction;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.Result;

public class qr_scanner extends AppCompatActivity {
    private CodeScanner mCodeScanner;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userEmail=FirebaseAuth.getInstance().getCurrentUser().getEmail();
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(qr_scanner.this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                qr_scanner.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(qr_scanner.this, result.getText(), Toast.LENGTH_SHORT).show();
                        AfterRegistrationMainActivity obj=new AfterRegistrationMainActivity();
                       obj.room_join_function(result.getText(),userEmail);
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
}
