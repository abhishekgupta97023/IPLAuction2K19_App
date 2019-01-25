package com.tachyon.techlabs.iplauction;

import android.app.NotificationManager;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
//import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class cards_adapter extends RecyclerView.Adapter<cards_adapter.ViewHolder>{

    private Context c;
    private int[] carsimgs;
    private String[] names;
    private String[] disc;
    FirebaseStorage storage=FirebaseStorage.getInstance();
    StorageReference storageRef=storage.getReference();
    private String[] price;
    View view,view2;
    ViewHolder viewHolder;
    private RelativeLayout bottonSheetLayout;
    private BottomSheetBehavior bottomSheetBehavior;
    View bgView;
    TextView bs_name,bs_desc;




    /*
    ArrayList<Power_Cards> cards;
    private String CHANNEL_ID="Abhishek";
    PowerCards pc_object;
    private NotificationManager nm;
    private  NotificationCompat.Builder mBuilder;
    */


    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageViewCard,imgbs;
        public TextView textnamesCard;
        public TextView textdiscCard;
        public TextView textpriceCard;
        public CardView cardView;




        public ViewHolder(View itemView,View bsView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.cardviewid);
            imageViewCard = (ImageView) itemView.findViewById(R.id.cardimg);
            textnamesCard = (TextView) itemView.findViewById(R.id.cardname);
            textdiscCard = (TextView) itemView.findViewById(R.id.carddescript);
            textpriceCard = (TextView) itemView.findViewById(R.id.cardprice);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    switch (position)
                    {
                        case 0 :
                            bs_name.setText(names[position]);
                            //bs_desc.setText(disc[position]);
                         //   bs_value.setText(R.string.yorker_price);
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                            //bottomSheetBehavior.setPeekHeight(300);
                            break;
                        case 1 :
                            bs_name.setText(names[position]);
                            //bs_desc.setText(disc[position]);
                          //  bs_value.setText(R.string.no_ball_price);
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                            break;
                        case 2 :
                            bs_name.setText(names[position]);
                            //bs_desc.setText(disc[position]);
                            //bs_value.setText(R.string.right_to_match_price);
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                            break;
                        case 3 :
                            bs_name.setText(names[position]);
                            //bs_desc.setText(disc[position]);
                           // bs_value.setText(R.string.legend_cards_price);
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                            break;
                        case 4 :
                            bs_name.setText(names[position]);
                            //bs_desc.setText(disc[position]);
                            // bs_value.setText(R.string.legend_cards_price);
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                            break;
                    }
                }
            });

        }
    }

    public cards_adapter(Context context,String[] disc, String[] name,String [] price,
                         RelativeLayout bottomSheetLayout,BottomSheetBehavior bottomSheetBehavior,
                         TextView bs_name,TextView bs_desc)
    {
        this.c = context;
       // this.carsimgs = img;
        this.disc = disc;
        this.names = name;
        this.price = price;
        this.bottonSheetLayout = bottomSheetLayout;
        this.bottomSheetBehavior = bottomSheetBehavior;
        this.bs_name = bs_name;
        //this.bs_desc = bs_desc;
      //  this.bs_value = bs_value;

    }

    /*

     cards_adapter(Context c, ArrayList<Power_Cards> cards) {
        this.c = c;
        this.cards = cards;
           mBuilder = new NotificationCompat.Builder(c, 1234+"")
                 .setSmallIcon(R.mipmap.ic_launcher)
                 .setContentTitle("Card Purchase")
                 .setContentText("You purschased"+"card")
                 .setPriority(NotificationCompat.PRIORITY_DEFAULT);

         nm=(NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
         Toast.makeText(c, "Entered this function", Toast.LENGTH_SHORT).show();
     }

     */


/*
    @Override
    public int getCount() {
        return cards.size();
    }

    @Override
    public Object getItem(int i) {
        return cards.get(i);
    }

    */

    @NonNull
    @Override
    public cards_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.powercard_item,parent,false);
        view2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_bottom_sheet_buy,parent,false);
        viewHolder = new ViewHolder(view,view2);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final cards_adapter.ViewHolder holder, int position) {
       // holder.imageViewCard.setImageResource(carsimgs[position]);
       // Picasso.get().load(carsimgs[position]).into(holder.imageViewCard);
      //  Glide.with(c).load(uri.toString()).into(player_img);
        storageRef.child(names[position]+".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
               // Log.d("playerimg",uri.toString());
                Glide.with(c).load(uri.toString()).into(holder.imageViewCard);
                //GlideApp.with(OngoingPlayer.this).load(storageRef).into(player_img);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                       // Log.d("playerimg","fail");
                      //  Toast.makeText(this, "Not able to load player image", Toast.LENGTH_SHORT).show();
                    }
                });

        holder.textdiscCard.setText(disc[position]);
        holder.textnamesCard.setText(names[position]);
        holder.textpriceCard.setText(price[position]);
    }

    /*
    @Override
    public long getItemId(int i) {
        return i;
    }
    */

    @Override
    public int getItemCount() {
        return disc.length;
    }


    /*

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null)
        {
            view= LayoutInflater.from(c).inflate(R.layout.powercard_item,viewGroup,false);
        }

        final Power_Cards s= (Power_Cards) this.getItem(i);

        ImageView img= (ImageView) view.findViewById(R.id.cardimg);
        TextView nameTxt= (TextView) view.findViewById(R.id.name);
        TextView propTxt= (TextView) view.findViewById(R.id.descript);
        TextView price= (TextView) view.findViewById(R.id.price);
        ImageButton buy=(ImageButton) view.findViewById(R.id.buy_button);

        //BIND
        nameTxt.setText(s.getName());
        propTxt.setText(s.getDescription());
        img.setImageResource(s.getImage());
       String pricee= (s.getPrice()+"");
        price.setText(pricee);




        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


// notificationId is a unique int for each notification that you must define
               // pc_object.notificationManager.notify(1234, pc_object.mBuilder.build());

                nm.notify(1234, mBuilder.build());
                Toast.makeText(c, s.getName(), Toast.LENGTH_SHORT).show();

                            }
        });



        return view;
    }

    */
}