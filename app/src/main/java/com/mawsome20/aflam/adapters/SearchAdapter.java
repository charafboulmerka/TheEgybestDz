package com.mawsome20.aflam.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.mawsome20.aflam.DetailsActivity;
import com.mawsome20.aflam.R;
import com.mawsome20.aflam.new_conception.models.mix_movies_series;
import com.mawsome20.aflam.utils.ItemAnimation;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.mawsome20.aflam.utils.MyAppClass.getContext;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    List<mix_movies_series> items;
    Context context;

    private int lastPosition = -1;
    private boolean on_attach = true;
    private int animation_type = 2;

    public SearchAdapter(List<mix_movies_series> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.card_home_view, parent,
                false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        mix_movies_series item = items.get(position);

        if (item != null) {

            holder.name.setText(item.getTitle());
            holder.qualityTv.setText("HD");
            holder.releaseDateTv.setText(item.getYear());

            Picasso.get().load(item.getPic_profile()).centerCrop().fit().into(holder.image);

            setAnimation(holder.itemView, position);

            holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToDetailsActivity(item);
                }
            });

        }



    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {



        public ImageView image;
        public TextView name, qualityTv, releaseDateTv;
        public MaterialRippleLayout lyt_parent;


        public ViewHolder(View v) {
            super(v);
            image = v.findViewById(R.id.image);
            name = v.findViewById(R.id.name);
            lyt_parent = v.findViewById(R.id.lyt_parent);
            qualityTv = v.findViewById(R.id.quality_tv);
            releaseDateTv = v.findViewById(R.id.release_date_tv);




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

    private void goToDetailsActivity(mix_movies_series obj) {
        Intent intent=new Intent(getContext(), DetailsActivity.class);
        intent.putExtra("vType",obj.getName_class());
        intent.putExtra("id",obj.getId());
        getContext().startActivity(intent);
    }
}
