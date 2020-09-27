package com.mawsome20.aflam20.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.mawsome20.aflam20.itemByGenreAndCategory;
import com.mawsome20.aflam20.R;
import com.mawsome20.aflam20.new_conception.models.genres;
import com.mawsome20.aflam20.utils.ItemAnimation;
import com.mawsome20.aflam20.utils.Tools;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.ViewHolder> {
    private Context context;
    private List<genres> mList;
    private String type;
    private String layout;
    private int c;

    private int lastPosition = -1;
    private boolean on_attach = true;
    private int animation_type = 2;

    public GenreAdapter(Context context, List<genres> mList, String type, String layout) {
        this.context = context;
        this.mList = mList;
        this.type = type;
        this.layout = layout;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if (layout.equals("home")) {
            v = LayoutInflater.from(context).inflate(R.layout.layout_genre_item, parent,
                    false);
        } else {
            v = LayoutInflater.from(context).inflate(R.layout.layout_genre_item_2, parent,
                    false);
        }

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final genres item = mList.get(position);
        if (item != null) {
            holder.cardView.requestFocus();
            holder.nameTv.setText(new Tools().getArName(item.getName()));

            if (item.getPic().equals("")){
                Picasso.get()
                        .load(R.drawable.icon_favorite )
                        .centerCrop()
                        .fit()
                        .placeholder(R.drawable.poster_placeholder)
                        .into(holder.icon);
            }else {
                Picasso.get()
                        .load(item.getPic())
                        .centerCrop()
                        .fit()
                        .placeholder(R.drawable.poster_placeholder)
                        .into(holder.icon);
            }

            holder.cardView.setBackgroundResource(getColor());

            holder.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent=new Intent(context, itemByGenreAndCategory.class);
                    intent.putExtra("id",item.getName());
                    Log.e("THE_ID",String.valueOf(item.getName()));

                    intent.putExtra("title",new Tools().getArName(item.getName()));
                    intent.putExtra("type",type);
                    Log.e("THE_ID",String.valueOf(type));

                    context.startActivity(intent);


                }
            });

        }

        setAnimation(holder.itemView, position);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTv;
        ImageView icon;
        CardView cardView;
        LinearLayout itemLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTv = itemView.findViewById(R.id.genre_name_tv);
            icon = itemView.findViewById(R.id.icon);
            cardView = itemView.findViewById(R.id.card_view);
            itemLayout = itemView.findViewById(R.id.item_layout);

        }
    }

    private int getColor(){

        int colorList[] = {R.color.red_400,R.color.blue_400,R.color.indigo_400,R.color.orange_400,R.color.light_green_400,R.color.blue_grey_400};
        int colorList2[] = {R.drawable.gradient_1 ,R.drawable.gradient_2,R.drawable.gradient_3,R.drawable.gradient_4,R.drawable.gradient_5,R.drawable.gradient_6};

        if (c >= 6){
            c = 0;
        }

        int color = colorList2[c];
        c++;

        return color;

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
/*
     String getArName(String old_name){
        String new_title="";
        switch (old_name.trim()){
            case "Action":
                new_title="أكشن";
            break;

            case "Adventure":
                new_title="مغامرة";
                break;

            case "Comedy":
                new_title="كوميدي";
                break;

            case "Crime":
                new_title="جريمة";
                break;

            case "Drama":
                new_title="دراما";
                break;

            case "Fantasy":
                new_title="خيال";
                break;

            case "Horror":
                new_title="رعب";
                break;

            case "Mystery":
                new_title="لـغز";
                break;

            case "Romance":
                new_title="رومانسي";
                break;

            case "Thriller":
                new_title="إثارة";
                break;

            case "Si-Fi":
                new_title="خيال العلمي";
                break;

            case "Animation":
                new_title="أنـمـي";
                break;

            case "Biography":
                new_title="سيرة شخصية";
                break;

            case "Documentary":
                new_title="وثائقي";
                break;

            case "Family":
                new_title="أسرة";
                break;


            case "Music":
                new_title="موسيقى";
                break;

            case "Musical":
                new_title="موسيقي";
                break;

            case "History":
                new_title="تاريخي";
                break;

            case "War":
                new_title="حرب";
                break;



            case "TV Series":
                new_title="مسلسل تلفزيوني";
                break;

            case "Short":
                new_title="قصير";
                break;

            case "Sport":
                new_title="رياضة";
                break;

            case "Action & Adventure":
                new_title="أكشن و مغامرة";
                break;

            case "Sci-Fi & Fantasy":
                new_title="خيال علمي";
                break;

            case "Science Fiction":
                new_title="خيالي علمي";
                break;

            case "Sci-Fi":
                new_title="خيالي علمي";
                break;

            case "TV Movie":
                new_title="فيلم تلفزيوني";
                break;

            case "News":
                new_title="أخبار";
                break;

            case "Reality":
                new_title="واقعي";
                break;


            case "Kids":
                new_title="أطفال";
                break;



            case "Historical fiction":
                new_title="خيالي تاريخي";
                break;



            case "Philosophical":
                new_title="فلسفي";
                break;

            case "Political":
                new_title="سياسي";
                break;

            case "Saga":
                new_title="قصة طويلة";
                break;



            case "Social":
                new_title="اجتماعي";
                break;

            case "Speculative":
                new_title="تخميني";
                break;

            case "Urban":
                new_title="حضاري";
                break;

            case "Film-Noir":
                new_title="دراما و جريمة قديمة";
                break;


            case "Magical realism":
                new_title="السحر الواقعي";
                break;

            case "Paranoid fiction":
                new_title="خيالي";
                break;

            case "Satire":
                new_title="هجاء";
                break;

            case "Adult":
                new_title="للكبار";
                break;

            case "Western":
                new_title="واستيرن";
                break;

            default:
                new_title=old_name;
                break;
        }
        return new_title;
    }

 */
}
