package com.mawsome20.aflam.adapters;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.mawsome20.aflam.DetailsActivity;
import com.mawsome20.aflam.R;

import com.mawsome20.aflam.new_conception.models.Movies;
import com.mawsome20.aflam.utils.ItemAnimation;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HomePageMoviesAdapter extends RecyclerView.Adapter<HomePageMoviesAdapter.OriginalViewHolder> {

    private List<Movies> items = new ArrayList<>();
    private Context ctx;

    private int lastPosition = -1;
    private boolean on_attach = true;
    private int animation_type = 2;



    public HomePageMoviesAdapter(Context context, List<Movies> items) {
        //Collections.shuffle(items);
        this.items = items;
        ctx = context;
    }


    @Override
    public HomePageMoviesAdapter.OriginalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        HomePageMoviesAdapter.OriginalViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_home_view, parent, false);
        vh = new HomePageMoviesAdapter.OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final HomePageMoviesAdapter.OriginalViewHolder holder, final int position) {

        final Movies obj = items.get(position);
        holder.name.setText(obj.getTitle());
        Picasso.get().load(obj.getPic_profile()).fit().centerCrop().placeholder(R.drawable.poster_placeholder).into(holder.image);

        holder.qualityTv.setText("HD");
        holder.releaseDateTv.setText(obj.getYear());

        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ctx, DetailsActivity.class);
                intent.putExtra("vType","movie");
                intent.putExtra("id",obj.getId());

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ctx.startActivity(intent);
            }
        });


/*
        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if (PreferenceUtils.isMandatoryLogin(ctx)){
                        if (PreferenceUtils.isLoggedIn(ctx)){
                            Intent intent=new Intent(ctx, DetailsActivity.class);
                            intent.putExtra("vType",obj.getVideoType());
                            intent.putExtra("id",obj.getId());

                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            ctx.startActivity(intent);
                        }else {
                            ctx.startActivity(new Intent(ctx, LoginActivity.class));
                        }
                    }else {
                        Intent intent=new Intent(ctx, DetailsActivity.class);
                        intent.putExtra("vType",obj.getVideoType());
                        intent.putExtra("id",obj.getId());

                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        ctx.startActivity(intent);
                    }

                }catch (Exception e){
                    Log.e("HOME_PAGE_ADBT",e.getMessage());
                }

            }
        });


 */
        setAnimation(holder.itemView, position);

    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;
        public TextView name, qualityTv, releaseDateTv;
        public MaterialRippleLayout lyt_parent;


        public OriginalViewHolder(View v) {
            super(v);
            image = v.findViewById(R.id.image);
            name = v.findViewById(R.id.name);
            lyt_parent=v.findViewById(R.id.lyt_parent);
            qualityTv=v.findViewById(R.id.quality_tv);
            releaseDateTv=v.findViewById(R.id.release_date_tv);
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
