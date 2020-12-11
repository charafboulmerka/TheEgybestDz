package com.mawsome20.aflam.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.mawsome20.aflam.DetailsActivity;
import com.mawsome20.aflam.R;
import com.mawsome20.aflam.new_conception.models.mix_movies_series;
import com.mawsome20.aflam.utils.ItemAnimation;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class All_MoviesSeriesSearchAdapter extends RecyclerView.Adapter<All_MoviesSeriesSearchAdapter.OriginalViewHolder> {

    private List<mix_movies_series> items = new ArrayList<>();
    private Context ctx;
    private String type;

    private int lastPosition = -1;
    private boolean on_attach = true;
    private int animation_type = 2;
    public int num = 1;


    public All_MoviesSeriesSearchAdapter(Context context, List<mix_movies_series> items, String type) {
        this.items = items;
        this.type=type;
        ctx = context;
    }


    @Override
    public All_MoviesSeriesSearchAdapter.OriginalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        All_MoviesSeriesSearchAdapter.OriginalViewHolder vh;
        View v ;
        if (type.equals("related")){
             v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_home_view, parent, false);
        }else {
             v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_image_albums, parent, false);
        }

        vh = new OriginalViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(All_MoviesSeriesSearchAdapter.OriginalViewHolder holder, final int position) {
        final mix_movies_series obj = items.get(position);

        //holder.qualityTv.setText(obj.getQuality());
        holder.releaseDateTv.setText(obj.getYear());
        holder.name.setText(obj.getTitle());

        Picasso.get().load(obj.getPic_profile()).fit().centerCrop().placeholder(R.drawable.poster_placeholder).into(holder.image);

        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDetailsActivity(obj);
                /*
                if (PreferenceUtils.isMandatoryLogin(ctx)){
                    if (PreferenceUtils.isLoggedIn(ctx)){
                        //goToDetailsActivity(obj);
                    }else {
                        ctx.startActivity(new Intent(ctx, LoginActivity.class));
                    }
                }else {
                  //  goToDetailsActivity(obj);
                }

                 */

            }
        });

        setAnimation(holder.itemView, position);


    }



    private void goToDetailsActivity(mix_movies_series obj) {
        Intent intent=new Intent(ctx,DetailsActivity.class);
        intent.putExtra("vType",obj.getName_class());
        intent.putExtra("id",obj.getId());
        ctx.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        if(num*10 > items.size()){
            return items.size();
        }else{
            return num*10;
        }
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;
        public TextView name, qualityTv, releaseDateTv;
        public MaterialRippleLayout lyt_parent;

        public View view;

        public CardView cardView;

        public OriginalViewHolder(View v) {
            super(v);
            view = v;
            image = v.findViewById(R.id.image);
            name = v.findViewById(R.id.name);
            lyt_parent = v.findViewById(R.id.lyt_parent);
          //  qualityTv = v.findViewById(R.id.quality_tv);
            releaseDateTv = v.findViewById(R.id.release_date_tv);
            cardView = v.findViewById(R.id.top_layout);
        }

    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                on_attach = false;
                super.onScrollStateChanged(recyclerView, newState);
            }

        });

        super.onAttachedToRecyclerView(recyclerView);
    }

    private void setAnimation(View view, int position) {
        if (position > lastPosition) {
            ItemAnimation.animate(view, on_attach ? position : -1, animation_type);
            lastPosition = position;
        }
    }

}