package com.mawsome20.aflam20.adapters;

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
import com.mawsome20.aflam20.DetailsActivity;
import com.mawsome20.aflam20.LoginActivity;
import com.mawsome20.aflam20.R;
import com.mawsome20.aflam20.models.CommonModels;
import com.mawsome20.aflam20.new_conception.models.Movies;
import com.mawsome20.aflam20.new_conception.models.Series;
import com.mawsome20.aflam20.utils.ItemAnimation;
import com.mawsome20.aflam20.utils.PreferenceUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class All_SeriesAdapter extends RecyclerView.Adapter<All_SeriesAdapter.OriginalViewHolder> {

    private List<Series> items = new ArrayList<>();
    private Context ctx;

    private int lastPosition = -1;
    private boolean on_attach = true;
    private int animation_type = 2;


    public All_SeriesAdapter(Context context, List<Series> items) {
        this.items = items;
        ctx = context;
    }


    @Override
    public All_SeriesAdapter.OriginalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        All_SeriesAdapter.OriginalViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_image_albums, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(All_SeriesAdapter.OriginalViewHolder holder, final int position) {
        final Series obj = items.get(position);

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



    private void goToDetailsActivity(Series obj) {
        Intent intent=new Intent(ctx,DetailsActivity.class);
        intent.putExtra("vType","tvseries");
        intent.putExtra("id",obj.getId());
        ctx.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return items.size();
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