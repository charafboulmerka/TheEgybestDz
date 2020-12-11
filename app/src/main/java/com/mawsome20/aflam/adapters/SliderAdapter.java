package com.mawsome20.aflam.adapters;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.github.islamkhsh.CardSliderAdapter;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mawsome20.aflam.DetailsActivity;
import com.mawsome20.aflam.R;
import com.mawsome20.aflam.new_conception.models.mix_movies_series;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

import static com.mawsome20.aflam.utils.MyAppClass.getContext;

public class SliderAdapter extends CardSliderAdapter<mix_movies_series> {
    Context ctx;
    public SliderAdapter(@NotNull ArrayList<mix_movies_series> items,Context ctx) {
        super(items);
        this.ctx=ctx;
    }

    @Override
    public void bindView(int i, @NotNull View view, @Nullable final mix_movies_series slide) {
        if (slide != null){
            TextView textView = view.findViewById(R.id.textView);
            TextView textViewBack = view.findViewById(R.id.textViewBack);

            textView.setText(slide.getTitle());

            if (slide.getId().contains("http")){
                textViewBack.setBackgroundResource(0);
            }


            RoundedImageView imageView = view.findViewById(R.id.imageview);
            Picasso.get().load(slide.getPic_cover()).fit().centerCrop().into(imageView);
            View lyt_parent = view.findViewById(R.id.lyt_parent);

            lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                if (slide.getId().contains("https://") || slide.getId().contains("http://")){
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(slide.getId()));
                    browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    getContext().startActivity(browserIntent);
                }else {
                    goToDetailsActivity(slide);
                }
                }
            });
        }
    }


    @Override
    public int getItemContentLayout(int i) {
        return R.layout.slider_item;
    }

    private void goToDetailsActivity(mix_movies_series obj) {
        Intent intent=new Intent(getContext(),DetailsActivity.class);
        intent.putExtra("vType",obj.getName_class());
        intent.putExtra("id",obj.getId());
        ctx.startActivity(intent);
    }
}
