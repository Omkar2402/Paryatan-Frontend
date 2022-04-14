package com.example.sihfrontend.streaming.getStreams;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sihfrontend.R;
import com.example.sihfrontend.user.monument.monumentInfo;

import java.util.ArrayList;

public class StreamAdapter extends RecyclerView.Adapter<StreamAdapter.Viewholder> {

    private Context context;
    private static ArrayList<StreamDetails> streamInfoArrayList;
    public StreamInterface streamInterface;

    // Constructor
    public  StreamAdapter(Context context, ArrayList<StreamDetails> courseModelArrayList, StreamInterface streamInterface) {
        this.context = context;
        this.streamInfoArrayList = courseModelArrayList;
        this.streamInterface = streamInterface;
    }

    // method for filtering our recyclerview items.
    public void filterList(ArrayList<StreamDetails> filterllist) {
        // below line is to add our filtered
        // list in our course array list.
        streamInfoArrayList = filterllist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StreamAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stream, parent, false);
        Viewholder viewholder = new Viewholder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                StreamInterface.onCardClicked(streamInfoArrayList.get(viewholder.getAdapterPosition()));
                  streamInterface.onCardClicked(streamInfoArrayList.get(viewholder.getAdapterPosition()));
            }
        });
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        StreamDetails model = streamInfoArrayList.get(position);
        try{
            holder.tvMonumentName.setText(model.getMonument_name());
            holder.tvTime.setText(model.getTime());
            holder.tvDate.setText(model.getDate());

        }catch (Exception e){
            e.printStackTrace();
        }
//        holder.monumentimg.setImageResource(model.getMonumentImage());

    }

    @Override
    public int getItemCount() {
        // this method is used for showing number
        // of card items in recycler view.
        return streamInfoArrayList.size();
    }


    // View holder class for initializing of
    // your views such as TextView and Imageview.
    public class Viewholder extends RecyclerView.ViewHolder {
        private TextView tvDate;
        private TextView tvTime;
        private TextView tvMonumentName;



        public Viewholder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvMonumentName = itemView.findViewById(R.id.tvMonumentName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    streamInterface.onCardClicked(streamInfoArrayList.get(getAdapterPosition()));
                }
            });
        }
    }
}
