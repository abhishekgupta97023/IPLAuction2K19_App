package com.tachyon.techlabs.iplauction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Objects;

import javax.annotation.Nullable;


public class Reveal extends AppCompatActivity {
    TextView fianl_amount_revealed;
    String correct_password,users_password;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth;
    long revealed_amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reveal);
        fianl_amount_revealed=findViewById(R.id.revealed_amount);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        DocumentReference reveal_password=db.collection("Passwords").document("Reveal");
        reveal_password.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                correct_password=documentSnapshot.getString("Pass");


            }
        });
        passwordDialog();


        DocumentReference documentReference = db.collection("Players").document(Objects.requireNonNull(currentUser.getEmail()));
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                if(documentSnapshot.exists())
                {
                    try
                    {
                        revealed_amount = Objects.requireNonNull(documentSnapshot.getLong("temp_curr_amount")).intValue();

                        }
                    catch(Exception exp)
                    {
                       // Toast.makeText(Reveal.this, exp.toString(), Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });






    }


    private void passwordDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();

        final View viewlayout = inflater.inflate(R.layout.dialog_password, null);


        builder.setCancelable(false);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(viewlayout)
                // Add action buttons
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //dialog.cancel();
                        // sign in the user ...
                        // Toast.makeText(this,"Joined successfully",Toast.LENGTH_SHORT).show();
                        fianl_amount_revealed.setText("₹"+revealed_amount+"");
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText user_password_field= viewlayout.findViewById(R.id.password);
                users_password = user_password_field.getText().toString();

                if(  check_pass(correct_password,users_password)) {


                    Toast.makeText(Reveal.this, "Correct Password", Toast.LENGTH_SHORT).show();
                    alert.dismiss();
                }
                else
                    Toast.makeText(Reveal.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                }
        });



    }

    private boolean check_pass(String passwordd,String users_password) {

        if(users_password.matches(passwordd))
        {
            Toast.makeText(Reveal.this, "Correct Password", Toast.LENGTH_SHORT).show();
            return true;
        }
        else
            return false;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,OngoingPlayer.class));
        finish();
    }
}

