package com.mawsome20.aflam20.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mawsome20.aflam20.DetailsActivity;
import com.mawsome20.aflam20.FirebaseSignUpActivity;
import com.mawsome20.aflam20.LoginActivity;
import com.mawsome20.aflam20.R;
import com.mawsome20.aflam20.models.EpiModel;
import com.mawsome20.aflam20.models.EpisodeMultipleServers;
import com.mawsome20.aflam20.new_conception.models.Seasons;
import com.mawsome20.aflam20.new_conception.models.episodes;
import com.mawsome20.aflam20.new_conception.models.strm_link;
import com.mawsome20.aflam20.utils.PreferenceUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.OriginalViewHolder> {

    private List<episodes> items = new ArrayList<>();
    private Context ctx;
    private String  SerieID;
    private Integer SeasonNumber;
    private String SeasonPicUrl = "";
    private String OldWatchedEps = "";
    final EpisodeAdapter.OriginalViewHolder[] viewHolderArray = {null};
    private EpisodeAdapter.OnItemClickListener mOnItemClickListener;
    EpisodeAdapter.OriginalViewHolder viewHolder;
    int i=-1;
    private int seasonNo;
    private RelativeLayout py;
    private DatabaseReference mRef;
    //0-255
    private Integer alpha_value = 80;
    private PreferenceUtils mUtils;
    private List<String> watchedList = new ArrayList<>();

    public Context getCtx(){
        return ctx;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, EpiModel obj, int position, OriginalViewHolder holder);
    }

    public void setOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public EpisodeAdapter(Context context, List<episodes> items, String SerieID, Integer SeasonNumber,String SeasonPicUrl
    ,List<String> watchedList) {
        //  Collections.reverse(items);
        this.items = items;
        ctx = context;
        this.SerieID=SerieID;
        this.SeasonNumber=SeasonNumber;
        this.SeasonPicUrl=SeasonPicUrl;
        this.watchedList=watchedList;
        mRef= FirebaseDatabase.getInstance().getReference();
        mUtils = new PreferenceUtils(context);
        //get data
       // DatabaseHelper db = new DatabaseHelper(ctx);
      //  OldWatchedEps = db.getWatchedEps(SerieName,SeasonNumber.toString());
    }


    @Override
    public EpisodeAdapter.OriginalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        EpisodeAdapter.OriginalViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_episode_item, parent, false);
        vh = new EpisodeAdapter.OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final EpisodeAdapter.OriginalViewHolder holder, final int position) {
    //    DatabaseHelper db = new DatabaseHelper(ctx);
//        OldWatchedEps = db.getWatchedEps(SerieName,SeasonNumber.toString());
        holder.box.setOnCheckedChangeListener(null);

        final episodes ep = items.get(position);

        ArrayList<strm_link> mStreamingLinks=new ArrayList<>() ;
        mStreamingLinks=ep.getStreaming_links();
        //Dealing with download link
        if (ep.getDownload_links().size()!=0){
            holder.downloadBt.setVisibility(View.VISIBLE);
        }
        else{
            holder.downloadBt.setVisibility(View.GONE);
        }

        holder.downloadBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    if (ep.getDownload_links().size()!=0) {
                       // if (PreferenceUtils.isLoggedIn(ctx)) {
                        if (true) {
                            ((DetailsActivity) ctx).listExternalDownload=ep.getDownload_links();
                            ((DetailsActivity) ctx).OnClickedDownloadNow();
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(ep.getDownload_links().get(0)));
                            ctx.startActivity(i);
                            //ctx.startActivity(new Intent((Activity) ctx, WebViewActivity.class).putExtra("url",finalNewDownloadLink));
                        } else {
                            Toast.makeText(ctx, R.string.download_not_permitted, Toast.LENGTH_SHORT).show();
                            Handler hand = new Handler();
                            hand.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ctx.startActivity(new Intent(ctx, FirebaseSignUpActivity.class));
                                }
                            }, 700);
                            Log.e("Download", "not permitted");
                        }
                    } else {
                        Toast.makeText(ctx, R.string.no_download_server_found, Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){Log.e("DETAILS_CATCH",e.getMessage());}
            }
        });

        //Dealing with the streaming links




        holder.name.setText(ep.getEp_title());

        if (watchedList.contains(ep.getEp_title())){
            holder.box.setChecked(true);
            //holder.name.setPaintFlags(holder.name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tvBox.setText("تمت المشاهدة");
            holder.episodIv.setAlpha(alpha_value);

        }else {
            holder.box.setChecked(false);
            //holder.name.setPaintFlags(0);
            holder.tvBox.setText("");
            holder.episodIv.setAlpha(255);
        }







        holder.box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (mUtils.isLoggedIn()){
                    holder.box.setEnabled(false);
                        //holder.name.setPaintFlags(holder.name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        mRef.child("Users").child(mUtils.getUserId()).child("Watched").child(SerieID)
                                .child(SeasonNumber.toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                try{
                                    if (dataSnapshot.exists()){
                                        String data = (String) dataSnapshot.getValue();
                                        Log.e("DATA1013",data);
                                        List<String> eps = Arrays.asList(data.split(","));
                                        if (eps.contains(ep.getEp_title())){
                                            String newData = "";
                                            for (String i:eps){
                                                if (!i.equals(ep.getEp_title()))
                                                    newData=newData+i+"," ;
                                            }
                                            mRef.child("Users").child(mUtils.getUserId()).child("Watched").child(SerieID)
                                                    .child(SeasonNumber.toString()).setValue(getFinalString(newData.trim()))
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            holder.box.setEnabled(true);
                                                            if (task.isSuccessful()){
                                                                holder.box.setChecked(b);
                                                                holder.tvBox.setText("");
                                                                holder.episodIv.setAlpha(255);
                                                                Toast.makeText(ctx,"تمت الإزالة بنجاح",Toast.LENGTH_LONG).show();
                                                            }else {
                                                                Toast.makeText(ctx,"حصل خطأ الرجاء إعادة المحاولة لاحقا",Toast.LENGTH_LONG).show();
                                                            }
                                                        }
                                                    });

                                        }else {
                                            mRef.child("Users").child(mUtils.getUserId()).child("Watched").child(SerieID)
                                                    .child(SeasonNumber.toString()).setValue(data+","+ep.getEp_title())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            holder.box.setEnabled(true);
                                                            if (task.isSuccessful()){
                                                                holder.box.setChecked(b);
                                                                holder.tvBox.setText("تمت المشاهدة");
                                                                holder.episodIv.setAlpha(alpha_value);
                                                                Toast.makeText(ctx,"تمت الإضافة  بنجاح",Toast.LENGTH_LONG).show();
                                                            }else {
                                                                Toast.makeText(ctx,"حصل خطأ الرجاء إعادة المحاولة لاحقا",Toast.LENGTH_LONG).show();
                                                            }
                                                        }
                                                    });

                                        }
                                    }else {
                                        mRef.child("Users").child(mUtils.getUserId()).child("Watched").child(SerieID)
                                                .child(SeasonNumber.toString()).setValue(ep.getEp_title())
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        holder.box.setEnabled(true);
                                                        if (task.isSuccessful()){
                                                            holder.box.setChecked(b);
                                                            holder.tvBox.setText("تمت المشاهدة");
                                                            holder.episodIv.setAlpha(alpha_value);
                                                            Toast.makeText(ctx,"تمت الإضافة بنجاح بنجاح",Toast.LENGTH_LONG).show();
                                                        }else {
                                                            Toast.makeText(ctx,"حصل خطأ الرجاء إعادة المحاولة لاحقا",Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                    }
                                }catch (Exception e){

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                }else {
                    holder.box.setChecked(false);
                    ctx.startActivity(new Intent(ctx, LoginActivity.class));
                    Toast.makeText(ctx,"يجب تسجل الدخول لإستخدام هذه الخاصية",Toast.LENGTH_LONG).show();
                }


            }
        });


        Picasso.get().load(SeasonPicUrl).fit().centerCrop().placeholder(R.drawable.poster_placeholder)
                .into(holder.episodIv);


        ArrayList<strm_link> finalMStreamingLinks = mStreamingLinks;
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO IF IT'S ONLY ONE SERVER PLAY DIRECTLY without opning a dialog
               // ((DetailsActivity)ctx).hideDescriptionLayout();
                //((DetailsActivity)ctx).showSeriesLayout();
                if (finalMStreamingLinks.size()==1){
                    ((DetailsActivity)ctx).ep_postion = position+1;
                    ((DetailsActivity)ctx).mediaUrl = finalMStreamingLinks.get(0).getUrl().trim();
                    ((DetailsActivity)ctx).iniMoviePlayer(finalMStreamingLinks.get(0).getUrl().trim(),finalMStreamingLinks.get(0).getType().trim(),ctx);

                }else {
                    ((DetailsActivity)ctx).ep_postion = position+1;
                    ((DetailsActivity)ctx).setMediaUrlForTvSeries(finalMStreamingLinks, SerieID, ep.getEp_title());
                }

                chanColor();
                holder.name.setTextColor(ctx.getResources().getColor(R.color.colorPrimary));
                holder.playStatusTv.setText("Playing");
                holder.playStatusTv.setVisibility(View.VISIBLE);


                viewHolderArray[0] =holder;
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }



    public class OriginalViewHolder extends RecyclerView.ViewHolder {

        public TextView name, playStatusTv,tvBox;
        public MaterialRippleLayout cardView;
        public ImageView episodIv;
        public MaterialCheckBox box;
        public ImageButton downloadBt;

        public OriginalViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.name);
            box = v.findViewById(R.id.box);
            downloadBt=v.findViewById(R.id.ep_downloadBt);
            playStatusTv = v.findViewById(R.id.play_status_tv);
            cardView=v.findViewById(R.id.lyt_parent);
            episodIv=v.findViewById(R.id.image);
            tvBox=v.findViewById(R.id.tv_box);
        }
    }

    public String correctUrl(String link){
        if (link.contains("@@"))
            return link.split("@@")[1];
        else return link.trim();
    }

    public void chanColor(){

        if (viewHolderArray[0]!=null){
            viewHolderArray[0].name.setTextColor(ctx.getResources().getColor(R.color.grey_20));
            viewHolderArray[0].playStatusTv.setVisibility(View.GONE);
        }
    }

    public String getFinalString(String data){
        if (data.startsWith(",")){
            data= data.substring(1,data.length());
        }
        if (data.endsWith(",")){
            data= data.substring(0,data.length()-1);
        }

        Log.e("FINAL1013",data.trim());
        return data.trim();
    }

}