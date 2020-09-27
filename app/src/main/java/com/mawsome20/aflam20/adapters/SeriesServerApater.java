package com.mawsome20.aflam20.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.mawsome20.aflam20.R;
import com.mawsome20.aflam20.models.EpisodeMultipleServers;

import java.util.ArrayList;
import java.util.List;

public class SeriesServerApater extends RecyclerView.Adapter<SeriesServerApater.OriginalViewHolder> {

    private String type;
    private List<EpisodeMultipleServers> items = new ArrayList<>();
    private Context ctx;

    private SeriesServerApater.OnItemClickListener mOnItemClickListener;

    private SeriesServerApater.OriginalViewHolder viewHolder;



    public interface OnItemClickListener {
        void onItemClick(View view, EpisodeMultipleServers obj, int position, OriginalViewHolder holder);
        void getFirstUrl(String url);
        void hideDescriptionLayout();
    }

    public void setOnItemClickListener(SeriesServerApater.OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;

    }



    public SeriesServerApater(Context context, List<EpisodeMultipleServers> items) {
        this.items = items;
        ctx = context;
    }


    @Override
    public SeriesServerApater.OriginalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SeriesServerApater.OriginalViewHolder vh;
        View v ;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_server_two, parent, false);
        vh = new SeriesServerApater.OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final SeriesServerApater.OriginalViewHolder holder, final int position) {

        EpisodeMultipleServers obj = items.get(position);
        holder.name.setText(obj.sName);



        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, items.get(position), position,holder);
                    mOnItemClickListener.hideDescriptionLayout();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public CardView cardView;

        public OriginalViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.name);
            cardView=v.findViewById(R.id.card_view_home);
        }
    }

    public void chanColor(SeriesServerApater.OriginalViewHolder holder, int pos){

        /*if (pos!=0){
            viewHolder.name.setTextColor(ctx.getResources().getColor(R.color.grey_60));
        }*/

        if (holder!=null){
            holder.name.setTextColor(ctx.getResources().getColor(R.color.grey_60));
        }



    }

}