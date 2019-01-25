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

public class qrcode_scanner extends AppCompatActivity {
    private CodeScanner mCodeScanner;
    String userEmail;

    AfterRegistrationMainActivity obj=new AfterRegistrationMainActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_scanner);
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        userEmail=FirebaseAuth.getInstance().getCurrentUser().getEmail();
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                qrcode_scanner.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                       // String code_qrscan=result.getText().toString();
                        AfterRegistrationMainActivity obj=new AfterRegistrationMainActivity();
                        obj.room_join_function(result.getText(),userEmail);
                        Toast.makeText(qrcode_scanner.this, result.getText(), Toast.LENGTH_SHORT).show();

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

  /*  private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                // Camera permission has not been granted.

                requestCameraPermission();

            } else {

                // Camera permissions is already available, show the camera preview.
              //  Log.i(TAG,
                //        "CAMERA permission has already been granted. Displaying camera preview.");
               // showCameraPreview();
            }
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    private void requestCameraPermission() {
        // BEGIN_INCLUDE(camera_permission_request)
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
        } else {

            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    0);
        }
        // END_INCLUDE(camera_permission_request)
    }*/

}