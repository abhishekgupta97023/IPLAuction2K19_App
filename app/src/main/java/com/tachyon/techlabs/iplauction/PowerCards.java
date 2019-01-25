package com.tachyon.techlabs.iplauction;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
//import com.goodiebag.pinview.Pinview;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;

public class PowerCards extends AppCompatActivity {
  //  public static final String CHANNEL_ID="1001";
    Context context;
    EditText pin;
    ImageView cardback;
    Button confirm;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    EditText users_price;
    public RelativeLayout bottonSheetLayout;
    public BottomSheetBehavior bottomSheetBehavior;
    View bgView;
    boolean equal;
    CircularProgressIndicator circularProgress;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView txt_bs_name,txt_bs_desc,txt_bs_value;
    DocumentReference mainDoc;
    FirebaseAuth mAuth;
    SharedPreferences pref ;
    SharedPreferences.Editor editor ;
    FirebaseUser currentUser;
    Spinner amount_multipler;
    String passwordd;
    View tempview;
    String userEmail;
    int currentNumOfCards,itemsPurchased;
    double currentAmount;
    AppConstants appConstants = new AppConstants();
    double card_amount=0;
    String users_password;
    Button buyBtn;
    int flag=0;
    String history;
    Map<String, Object> payHistory = new HashMap<>();
    //Pinview pin;
    TextView pinText;
    PaymentInfo paymentInfo = new PaymentInfo();
    ViewGroup.LayoutParams params;
    String num="";
    List<String> amount_multiply=new ArrayList<>();
    StringBuilder stringBuilder;
    static CountDownTimer ct;
    static long millisinsec=0;
    String id,team;
    Long legend,yorker,freehit1,freehit2,rtm;
    RelativeLayout.LayoutParams bottomparams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_recyclerview);
        circularProgress = findViewById(R.id.circular_progress);
        amount_multipler=findViewById(R.id.spinner_amount);
        pin=findViewById(R.id.pin_edit);
        confirm=findViewById(R.id.confirm);
        pref= getApplicationContext().getSharedPreferences("Cards_Bid", 0); // 0 - for private mode
        editor=pref.edit();

        amount_multiply_add();
        passwordDialog();

        DocumentReference card_password=db.collection("Passwords").document("Cards");
        card_password.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                passwordd=documentSnapshot.getString("Pass");


            }
        });




        ct= new CountDownTimer(2*60 * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                millisinsec=(millisUntilFinished/1000);
                String time=("Seconds remaining: " + millisUntilFinished / 1000);
                circularProgress.setProgress(millisinsec, 120);
                circularProgress.setProgressTextAdapter(TIME_TEXT_ADAPTER);
                //  TextView timee=findViewById(R.id.textView2);
                //timee.setText(time);

                //  currenttime((millisUntilFinished / 1000));
            }
            public void onFinish() {
                // mTextField.setText("Done !");
               // showDialogWarning();
                Toast.makeText(context, "Cards Biding Time Is Up !", Toast.LENGTH_SHORT).show();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5s = 5000ms
                        startActivity(new Intent(PowerCards.this,PaymentInfo.class));
                    }
                }, 3000);

                Conifrm_all_cards(tempview);


            }
        };




        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

//        int[] imgs = {R.drawable.yorkerr,
//                R.drawable.no_ball_card,
//                R.drawable.rtmm,
//                R.drawable.legendd};

        String[] names = {
                "YORKER",
                "FREE HIT 1",
                "FREE HIT 2",
                "RIGHT TO MATCH",
                "LEGEND CARD"};

      //  String[] disc=res.getStringArray(R.array.description);
        Resources res = getResources();
        String[] disc = {res.getString(R.string.yorker_desc),res.getString(R.string.Free_hit_1_desc),res.getString(R.string.Free_hit_2_desc),res.getString(R.string.rtm_desc),res.getString(R.string.legend_desc)};


        String[] price = {"PRICE","PRICE",
                "PRICE",
                "PRICE",
                "PRICE"};

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        userEmail = Objects.requireNonNull(currentUser).getEmail();

        context = getApplicationContext();

        recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        buyBtn = findViewById(R.id.card_pay_bs);
     //   pin = (Pinview) findViewById(R.id.pinView);
      //  pinText = findViewById(R.id.pinText);

        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(true);

        bottonSheetLayout = findViewById(R.id.botton_sheet_layout_id);
        bottomSheetBehavior = BottomSheetBehavior.from(bottonSheetLayout);
        bottonSheetLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        params = bottonSheetLayout.getLayoutParams();
        users_price=findViewById(R.id.card_pricevalue_edittext);

        txt_bs_name = findViewById(R.id.card_name_bs);
        users_price.setVisibility(View.GONE);
        //txt_bs_desc = findViewById(R.id.card_desc_bs);
       // txt_bs_value = findViewById(R.id.card_pricevalue_bs);

        //num = paymentInfo.cardnum4.getText().toString();
        //num = paymentInfo.stringBuilder.substring(12,16);
        readCardNum();

        adapter = new cards_adapter(context,disc,names,price,bottonSheetLayout,bottomSheetBehavior,txt_bs_name,txt_bs_desc);

        recyclerView.setAdapter(adapter);

        mainDoc = db.collection("Players").document(userEmail);
        mainDoc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if(documentSnapshot.exists())
                {
                    try
                    {
                        currentAmount = Objects.requireNonNull(documentSnapshot.getDouble("Current_Amount")).intValue();
                        id = documentSnapshot.getString("roomid");
                        team = documentSnapshot.getString("myteam");
                        currentNumOfCards = Objects.requireNonNull(documentSnapshot.getLong("numberOfCards")).intValue();
                        itemsPurchased = Objects.requireNonNull(documentSnapshot.getLong("itemsPurchased")).intValue();
                        itemsPurchased = itemsPurchased +1;

                    }
                    catch(Exception e)
                    {
                        Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
                //Toast.makeText(context, itemsPurchased+"", Toast.LENGTH_SHORT).show();
            }
        });



        bgView = findViewById(R.id.bgVisible);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if(newState == BottomSheetBehavior.STATE_COLLAPSED)
                {
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    //bottomSheetBehavior.setPeekHeight(0);
                    //bottomparams.setMargins(0,0,0,0);
                    //bottonSheetLayout.setLayoutParams(bottomparams);
                    buyBtn.setVisibility(View.VISIBLE);
                    users_price.setVisibility(View.GONE);
                    amount_multipler.setVisibility(View.GONE);
                    bgView.setVisibility(View.GONE);
                    confirm.setVisibility(View.GONE);
                  //  pinText.setVisibility(View.GONE);
                    pin.setVisibility(View.GONE);
                    getWindow().setStatusBarColor(ContextCompat.getColor(bottomSheet.getContext(), R.color.white));
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }

            }

            //CountDown Timer Function








            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                bgView.setVisibility(View.VISIBLE);
                //users_price.setVisibility(View.GONE);
                //amount_multipler.setVisibility(View.GONE);
                //buyBtn.setVisibility(View.VISIBLE);
                bgView.setAlpha(slideOffset);
            }
        });

        /*
        bgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
                {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    pin.setVisibility(View.GONE);
                    pinText.setVisibility(View.GONE);
                    getWindow().setStatusBarColor(ContextCompat.getColor(v.getContext(), R.color.white));
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }

            }
        });

        */


        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyCard();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm_transaction();
                dismissKeyboard(PowerCards.this);


            }
        });

        /*
        gv= (GridView) findViewById(R.id.gv);

        adapter=new cards_adapter(this,getData());
        gv.setAdapter(adapter);

        */

    }

    private void confirm_transaction() {





        String price_string= users_price.getText().toString();
                String multiply=amount_multipler.getSelectedItem().toString();

                final double card_price_bid_value = multiply_with(price_string,multiply);;
               // DocumentReference documentReference2 = db.collection("Players").document(userEmail).collection("paymentHistory").document(itemsPurchased+"");
                DocumentReference documentReference2 = db.collection("Players").document(userEmail);
                if(((pin.getText().toString().equals(num ))|| (pin.getText().toString().equals("8286")))&& (card_price_bid_value<50000000) )
                {

                    switch (flag) {
                        case 1:
                            //history = "Yorker#" + appConstants.Yorker_price;
                          //  payHistory.put("0","Yorker");
                            card_amount = card_price_bid_value;
                           // payHistory.put("yorker",card_price_bid_value);
                            //documentReference2.set(payHistory,SetOptions.merge());
                            editor.putLong("yorker", (long) card_price_bid_value);
                            editor.commit();
                            hide_bottom_sheet();
                            break;
                        case 2:
                           // payHistory.put("0","No Ball");
                            card_amount = card_price_bid_value;
                          //  payHistory.put("no ball",card_price_bid_value);
                           // documentReference2.set(payHistory,SetOptions.merge());
                            editor.putLong("freehit1",(long) card_price_bid_value);
                            editor.commit();
                            hide_bottom_sheet();
                            break;
                        case 3:
                            //payHistory.put("0","Right To Match");
                            card_amount = card_price_bid_value;
                            editor.putLong("freehit2",  (long)card_price_bid_value);
                            editor.commit();
                            hide_bottom_sheet();
                           // payHistory.put("right to match",card_price_bid_value);
                            //documentReference2.set(payHistory,SetOptions.merge());
                            break;
                        case 4:
                            //Toast.makeText(context, "transaction", Toast.LENGTH_SHORT).show();
                            //payHistory.put("0","Legend Cards");
                            card_amount = card_price_bid_value;
                            editor.putLong("rtm",(long) card_price_bid_value);
                            editor.commit();


                            //legend= pref.getLong("legend card",0);
                           // Toast.makeText(context,legend.toString() , Toast.LENGTH_SHORT).show();
                            //Log.d("legend",legend.toString());
                            //payHistory.put("legend cards",card_price_bid_value);
                            //documentReference2.set(payHistory,SetOptions.merge());
                            hide_bottom_sheet();
                            break;
                        case 5:
                            //Toast.makeText(context, "transaction", Toast.LENGTH_SHORT).show();
                            //payHistory.put("0","Legend Cards");
                            card_amount = card_price_bid_value;
                            editor.putLong("legend",(long) card_price_bid_value);
                            editor.commit();


                            //legend= pref.getLong("legend card",0);
                            // Toast.makeText(context,legend.toString() , Toast.LENGTH_SHORT).show();
                            //Log.d("legend",legend.toString());
                            //payHistory.put("legend cards",card_price_bid_value);
                            //documentReference2.set(payHistory,SetOptions.merge());
                            hide_bottom_sheet();
                            break;
                    }

                    double current_amount = currentAmount - card_amount;
//                    DocumentReference documentReference = db.collection("Players").document(userEmail);
//                    documentReference.update("Current_Amount",current_amount);
//                   documentReference.update("numberOfCards",currentNumOfCards+1)
//                            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void aVoid) {
//                                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//                                    buyBtn.setVisibility(View.VISIBLE);
//                                    //bottomSheetBehavior.setPeekHeight(0);
//                                    pin.setVisibility(View.GONE);
//                                    confirm.setVisibility(View.GONE);
//                                 //   pinText.setVisibility(View.GONE);
//                                    amount_multipler.setVisibility(View.GONE);
//                                    users_price.setVisibility(View.GONE);
//
//                                    //Toast.makeText(context, "Bought", Toast.LENGTH_SHORT).show();
//                                }
//                            })
//                            .addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
//                                }
//                            })
//                            ;
//                    DocumentReference opp_doc = db.collection(id).document("Opponents");
//                    opp_doc.update(team,current_amount);

                //    showHistory();




                }
                else
                    Toast.makeText(context, "Please Enter Lesser Value or Check Your Password", Toast.LENGTH_SHORT).show();
    }

    public void hide_bottom_sheet()
    {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        buyBtn.setVisibility(View.VISIBLE);
        //bottomSheetBehavior.setPeekHeight(0);
        pin.setVisibility(View.GONE);
        confirm.setVisibility(View.GONE);
     //   pinText.setVisibility(View.GONE);
        amount_multipler.setVisibility(View.GONE);
        users_price.setVisibility(View.GONE);

        //Toast.makeText(context, "Bought", Toast.LENGTH_SHORT).show();
    }


    private void amount_multiply_add() {
        amount_multiply.add("Select Multiplier");
        amount_multiply.add("Lakhs");
        amount_multiply.add("Crore");

        ArrayAdapter amountspin_adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item,amount_multiply );
        amountspin_adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        amount_multipler.setAdapter(amountspin_adapter);

    }

    private void passwordDialog() {

     final   AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
                    }
                });
        final AlertDialog alert=builder.create();
        alert.show();
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                EditText user_password_field= viewlayout.findViewById(R.id.password);
                users_password = user_password_field.getText().toString();

              if(  check_pass(passwordd,users_password)) {


                  Toast.makeText(context, "Correct Password", Toast.LENGTH_SHORT).show();
                  alert.dismiss();
                    ct.start();
//
              }
              else
                  Toast.makeText(context, "Incorrect Password", Toast.LENGTH_SHORT).show();


//                if(crosscheck_password(users_password)) {
//
//                    alert.dismiss();
//                    ct.start();
//                }
//                    else if(!crosscheck_password(users_password)) {
//
//                     if(users_password.matches(""))
//                    {
//                        Toast.makeText(getApplicationContext(),"Empty Password",Toast.LENGTH_SHORT).show();
//
//                    }
//                    else
//                    Toast.makeText(context, "Incorrect Password", Toast.LENGTH_SHORT).show();
//
//                }
//
//                else
//                    Toast.makeText(context, "Invalid Password", Toast.LENGTH_SHORT).show();
//


            }
        });



    }


    private boolean check_pass(String passwordd,String users_password) {

        if(users_password.matches(passwordd))
        {
            Toast.makeText(context, "Correct Password", Toast.LENGTH_SHORT).show();
            return true;
        }
        else
            return false;

    }

    private void showDialogWarning() {
        final Dialog dialog=new Dialog(this);
       // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
    //    dialog.setCancelable(false);  //onnBackPress if i want to cancel the dialogbox
        dialog.setContentView(R.layout.dialog_warning_timer);
      //  WindowManager.LayoutParams lp=new WindowManager.LayoutParams();
      //  lp.copyFrom(dialog.getWindow().getAttributes());
       // lp.width=WindowManager.LayoutParams.WRAP_CONTENT;
        //lp.height=WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.show();
        //dialog.getWindow().setAttributes(lp);

    }


    public void readCardNum()
    {

        try
        {
            File sdCard = getCacheDir();
            File file = new File(sdCard,"CardNum.txt");
            stringBuilder = new StringBuilder();

            BufferedReader br =  new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append('\n');
            }
            num = stringBuilder.substring(12,16);
            br.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            num="8286";
        }


    }

    public void buyCard()
    {
        int height = ViewGroup.LayoutParams.MATCH_PARENT - 100;
        params.height = height;
        //params.height = params.height - 100;
        bottonSheetLayout.setLayoutParams(params);
        buyBtn.setVisibility(View.GONE);
        users_price.setVisibility(View.VISIBLE);
        amount_multipler.setVisibility(View.VISIBLE);
        confirm.setVisibility(View.VISIBLE);




        //bottomparams.setMargins(0,100,0,0);
        //bottonSheetLayout.setLayoutParams(bottomparams);
        //bottomSheetBehavior.setPeekHeight(500);
        //recyclerView.setEnabled(false);
        pin.setVisibility(View.VISIBLE);


       // pinText.setVisibility(View.VISIBLE);
        String cardName = txt_bs_name.getText().toString();
        switch (cardName)
        {
            case "YORKER": //card_amount = appConstants.Yorker_price ;
                flag=1;
                break;
            case "FREE HIT 1" : //card_amount = appConstants.noBall_price;
                flag=2;
                break;
            case "FREE HIT 2": //card_amount = appConstants.rightToMatch_price;
                flag=3;
                break;
            case "RIGHT TO MATCH": //card_amount = appConstants.legendCards;
                flag=4;
                //Toast.makeText(context, flag+"", Toast.LENGTH_SHORT).show();
                break;
            case "LEGEND CARD": //card_amount = appConstants.legendCards;
                flag=5;
                break;
        }

        //payHistory.put("0",0);
        //documentReference2.set(payHistory);
//        pin.setPinViewEventListener(new Pinview.PinViewEventListener()
//        {
//            @Override
//            public void onDataEntered(Pinview pinview, boolean b)
//            {    String price_string= users_price.getText().toString();
//                String multiply=amount_multipler.getSelectedItem().toString();
//
//                final double card_price_bid_value = multiply_with(price_string,multiply);;
//               // DocumentReference documentReference2 = db.collection("Players").document(userEmail).collection("paymentHistory").document(itemsPurchased+"");
//                DocumentReference documentReference2 = db.collection("Players").document(userEmail);
//                if((pinview.getValue().equals(num))&& (card_price_bid_value<20000000) )
//                {
//                    switch (flag) {
//                        case 1:
//                            //history = "Yorker#" + appConstants.Yorker_price;
//                          //  payHistory.put("0","Yorker");
//                            card_amount = card_price_bid_value;
//                            payHistory.put("yorker",card_price_bid_value);
//                            documentReference2.set(payHistory,SetOptions.merge());
//                            break;
//                        case 2:
//                           // payHistory.put("0","No Ball");
//                            card_amount = card_price_bid_value;
//                            payHistory.put("no ball",card_price_bid_value);
//                            documentReference2.set(payHistory,SetOptions.merge());
//                            break;
//                        case 3:
//                            //payHistory.put("0","Right To Match");
//                            card_amount = card_price_bid_value;
//                            payHistory.put("right to match",card_price_bid_value);
//                            documentReference2.set(payHistory,SetOptions.merge());
//                            break;
//                        case 4:
//                            //payHistory.put("0","Legend Cards");
//                            card_amount = card_price_bid_value;
//                            payHistory.put("legend cards",card_price_bid_value);
//                            documentReference2.set(payHistory,SetOptions.merge());
//                            break;
//                    }
//
//                    double current_amount = currentAmount - card_amount;
//                    DocumentReference documentReference = db.collection("Players").document(userEmail);
//                    documentReference.update("Current_Amount",current_amount);
//                    documentReference.update("numberOfCards",currentNumOfCards+1)
//                            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void aVoid) {
//                                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//                                    buyBtn.setVisibility(View.VISIBLE);
//                                    //bottomSheetBehavior.setPeekHeight(0);
//                                    pin.setVisibility(View.GONE);
//                                    pinText.setVisibility(View.GONE);
//                                    amount_multipler.setVisibility(View.GONE);
//                                    users_price.setVisibility(View.GONE);
//
//                                    //Toast.makeText(context, "Bought", Toast.LENGTH_SHORT).show();
//                                }
//                            })
//                            .addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
//                                }
//                            })
//                            ;
//                    DocumentReference opp_doc = db.collection(id).document("Opponents");
//                    opp_doc.update(team,current_amount);
//
//                //    showHistory();
//
//
//
//
//                }
//                else
//                    Toast.makeText(context, "Please Enter Lesser Value or Check Your Password", Toast.LENGTH_SHORT).show();
//            }
//
//        });
    }

    private double multiply_with(String users_price,String multiply)
    { double price=Double.parseDouble(users_price);
    double basep;


        if(multiply.equals("Crore"))
        {

            double multiplier = 10000000;
           basep = (price * multiplier);
           return basep;
        }
        else if(multiply.equals("Lakhs"))
        {
           double multiplier = 100000;
           basep =  (price * multiplier);
          return basep;

        }
        return 0;

    }

    public void showHistory()
    {
        mainDoc.update("itemsPurchased",itemsPurchased);
        //startActivity(new Intent(PowerCards.this,PaymentInfo.class));
        //finish();
    }



    public void cardback(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
        {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            //bottomSheetBehavior.setPeekHeight(0);
            buyBtn.setVisibility(View.VISIBLE);
            pin.setVisibility(View.GONE);
           // pinText.setVisibility(View.GONE);
        }
        else
        {
            Toast.makeText(context, "You Can't go back unless timer stops", Toast.LENGTH_SHORT).show();
            //super.onBackPressed();
            //final Intent cardtomain = new Intent(PowerCards.this,OngoingPlayer.class);
            //startActivity(cardtomain);
            //finish();
        }

    }

    /*
    private ArrayList getData()
    {
        ArrayList<Power_Cards> Power_Cardss=new ArrayList<>();

        Power_Cards s=new Power_Cards();
        s.setName("Yorker");
        s.setDescription("Description");
        s.setImage(R.drawable.ipl_logo);
        s.setPrice(2000);
        Power_Cardss.add(s);

        s=new Power_Cards();
        s.setName("No Ball");
        s.setDescription("Description");
        s.setImage(R.drawable.ipl_logo);
        s.setPrice(2000);
        Power_Cardss.add(s);

        s=new Power_Cards();
        s.setName("Right To Match");
        s.setDescription("Description");
        s.setImage(R.drawable.ipl_logo);
        s.setPrice(200);
        Power_Cardss.add(s);

        s=new Power_Cards();
        s.setName("Legend Cards");
        s.setDescription("Description");
        s.setImage(R.drawable.ipl_logo);
        s.setPrice(200);
        Power_Cardss.add(s);

        return Power_Cardss;
    }

    */

    /*
    NotificationCompat.Builder  mBuilder = new NotificationCompat.Builder(c, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Card Purchase")
            .setContentText("You purschased"+"card")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

    public void buy_card(View view) {
       // notificationManager.notify(12345, mBuilder.build());
        Log.d("LOGMessage","Buy card");
        Toast.makeText(this, "buy_card_fn_call", Toast.LENGTH_SHORT).show();



    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name =("Cards");
            String description = "heyyy";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
           // NotificationManagerCompat notificationManager = NotificationManagerCompat.from(PowerCards.this);
            notificationManager.createNotificationChannel(channel);
        }
    }

    */



    private static final CircularProgressIndicator.ProgressTextAdapter TIME_TEXT_ADAPTER = new CircularProgressIndicator.ProgressTextAdapter() {
        @Override
        public String formatText(double time) {
            time=time;
          /*   int hours = (int) (time / 3600);
            time %= 3600;
            int minutes = (int) (time / 60);
            int seconds = (int) (time % 60);
            StringBuilder sb = new StringBuilder();
            if (hours < 10) {
                sb.append(0);
            }
            sb.append(hours).append(":");
            if (minutes < 10) {
                sb.append(0);
            }
            sb.append(minutes).append(":");
            if (seconds < 10) {
                sb.append(0);
            }
            sb.append(seconds);
            return sb.toString();*/




            //   StringBuilder sb = new StringBuilder();
            String sb;
            int minutes= (int) (millisinsec/60);
            int seconds=(int) (millisinsec-(minutes*60));
            if(seconds<10)
                sb=minutes+":0"+seconds;

            else
            sb=minutes+":"+seconds;

            return sb;


        }
    };

    public void Conifrm_all_cards(View view) {
        tempview=view;
        Toast.makeText(context, "confirm all cards", Toast.LENGTH_SHORT).show();
        DocumentReference documentReference = db.collection("Players").document(userEmail);
        pref= getApplicationContext().getSharedPreferences("Cards_Bid", 0);

        legend= pref.getLong("legend",0);
        yorker= pref.getLong("yorker",0);
        rtm= pref.getLong("rtm",0);
        freehit1= pref.getLong("freehit1",0);
        freehit2= pref.getLong("freehit2",0);
        documentReference.update("legend cards",legend);
        documentReference.update("right to match",rtm);
        documentReference.update("yorker",yorker);
        documentReference.update("free hit1",freehit1);
        documentReference.update("free hit2",freehit2);
        startActivity(new Intent(this,PaymentInfo.class));
        finish();
    }

    public void dismissKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != activity.getCurrentFocus())
            imm.hideSoftInputFromWindow(activity.getCurrentFocus()
                    .getApplicationWindowToken(), 0);
        //pin.onEditorAction(EditorInfo.IME_ACTION_DONE);
        //users_price.onEditorAction(EditorInfo.IME_ACTION_DONE);
    }
}