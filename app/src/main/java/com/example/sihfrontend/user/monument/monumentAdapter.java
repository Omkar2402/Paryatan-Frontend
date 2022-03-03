package com.example.sihfrontend.user.monument;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sihfrontend.R;

import java.util.ArrayList;

public class monumentAdapter extends RecyclerView.Adapter<monumentAdapter.Viewholder> {

    private Context context;
    private ArrayList<monumentInfo> monumentInfoArrayList;

    // Constructor
    public monumentAdapter(Context context, ArrayList<monumentInfo> courseModelArrayList) {
        this.context = context;
        this.monumentInfoArrayList = courseModelArrayList;
    }

    @NonNull
    @Override
    public monumentAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_user_main, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull monumentAdapter.Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        monumentInfo model = monumentInfoArrayList.get(position);
        holder.monumentname.setText(model.getMonumentName());
//        holder.monumentimg.setImageResource(model.getMonumentImage());
    }

    @Override
    public int getItemCount() {
        // this method is used for showing number
        // of card items in recycler view.
        return monumentInfoArrayList.size();
    }

    // View holder class for initializing of
    // your views such as TextView and Imageview.
    public class Viewholder extends RecyclerView.ViewHolder {
        private ImageView monumentimg;
        private TextView monumentname;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            monumentimg = itemView.findViewById(R.id.imgMonumentImage);
            monumentname = itemView.findViewById(R.id.txtMonumentName);
        }
    }
}
