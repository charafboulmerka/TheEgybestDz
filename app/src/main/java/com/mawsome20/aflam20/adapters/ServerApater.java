package com.mawsome20.aflam20.adapters;

import android.content.Context;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mawsome20.aflam20.R;
import com.mawsome20.aflam20.models.CommonModels;
import com.mawsome20.aflam20.models.EpisodeMultipleServers;
import com.mawsome20.aflam20.new_conception.models.strm_link;

import java.util.ArrayList;
import java.util.List;

public class ServerApater extends RecyclerView.Adapter<ServerApater.OriginalViewHolder> {

    private String type;
    private List<strm_link> items = new ArrayList<>();
    private Context ctx;

    private ServerApater.OnItemClickListener mOnItemClickListener;

    private ServerApater.OriginalViewHolder viewHolder;



    public interface OnItemClickListener {
        void onItemClick(View view, strm_link obj, int position, OriginalViewHolder holder);
        void getFirstUrl(String url);
        void hideDescriptionLayoutEvent(String type);
    }

    public void setOnItemClickListener(ServerApater.OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;

    }



    public ServerApater(Context context, List<strm_link> items, String type) {
        this.items = items;
        ctx = context;
        this.type = type;
    }


    @Override
    public ServerApater.OriginalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ServerApater.OriginalViewHolder vh;
        View v ;
       // if (type.equals("tv")) {
          //  v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_server, parent, false);
       // } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_server_two, parent, false);
       // }
        vh = new ServerApater.OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ServerApater.OriginalViewHolder holder, final int position) {

        String server_title = "";
        server_title = items.get(position).getName();
        /*
        if (type.equals("movie")){
            server_title = items.get(position).getName();
        }
        else {
            server_title = series_items.get(position).getName();
        }

         */


        holder.name.setText(server_title);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, items.get(position), position,holder);
                    /*
                    if (type.equals("movie")){
                        mOnItemClickListener.onItemClick(v, items.get(position), position,holder,null);
                    }
                    else{
                        mOnItemClickListener.onItemClick(v, null, position,holder,series_items.get(position));
                        //mOnItemClickListener.hideDescriptionLayout();
                    }

                     */
                    mOnItemClickListener.hideDescriptionLayoutEvent(type);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
        /*
        if (type.equals("movie")){
            return items.size();
        }
        else{
            return series_items.size();
        }

         */

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

    public void chanColor(ServerApater.OriginalViewHolder holder,int pos){

        /*if (pos!=0){
            viewHolder.name.setTextColor(ctx.getResources().getColor(R.color.grey_60));
        }*/

        if (holder!=null){
            holder.name.setTextColor(ctx.getResources().getColor(R.color.grey_60));
        }



    }

}