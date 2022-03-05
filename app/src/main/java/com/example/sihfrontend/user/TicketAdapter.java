package com.example.sihfrontend.user;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sihfrontend.R;
import com.example.sihfrontend.user.UserMainActivity;

import java.util.ArrayList;

public  class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.Viewholder> {

    private Context context;
    private static ArrayList<ticketInfo> TicketInfoArrayList;
    public ticketInterface ticketInterface1;

    // Constructor
    public TicketAdapter(Context context, ArrayList<ticketInfo> courseModelArrayList,ticketInterface ticketInterface1) {
        this.context = context;
        this.TicketInfoArrayList = courseModelArrayList;
        this.ticketInterface1 = ticketInterface1;
    }

    @NonNull
    @Override
    public TicketAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ticket_list, parent, false);
//        Viewholder viewholder = new Viewholder(view);
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ticketInterface.onCardClicked(monumentInfoArrayList.get(viewholder.getAdapterPosition()));
//            }
//        });
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketAdapter.Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        ticketInfo model = TicketInfoArrayList.get(position);
        try{
            Log.d("Success:","bytes.toString()");
            String name = model.getVisitorName();
            holder.visitorName.setText(name);

        }catch (Exception e){
            e.printStackTrace();
        }



//        holder.monumentname.setText(model.getMonumentName());
//        holder.monumentimg.setImageResource(model.getMonumentImage());
    }

    //    public static void updateMonuments(ArrayList<monumentInfo> new_arrayList){
//        Log.d("In updatemonument","....");
//        Log.d("new_arraylist",""+new_arrayList.size());
//        monumentInfoArrayList.clear();
//        Log.d("arraylist",""+monumentInfoArrayList.size());
//        for(int i=0;i<new_arrayList.size();i++){
//            monumentInfoArrayList.add(new_arrayList.get(i));
//        }
//        //monumentInfoArrayList.addAll(new_arrayList);
//        Log.d("arrayList",""+monumentInfoArrayList.size());
//        monumentAdapter obj = new monumentAdapter(new_arrayList);
//        obj.notifyDataSetChanged();
//
//    }
    @Override
    public int getItemCount() {
        // this method is used for showing number
        // of card items in recycler view.
        return TicketInfoArrayList.size();
    }


    // View holder class for initializing of
    // your views such as TextView and Imageview.
    public class Viewholder extends RecyclerView.ViewHolder {
        private TextView visitorName;
        private Button edit;
        private Button delete;



        public Viewholder(@NonNull View itemView) {
            super(itemView);
            visitorName = itemView.findViewById(R.id.tvVisitorName);
            edit = itemView.findViewById(R.id.btnTicketEdit);
            delete = itemView.findViewById(R.id.btnTicketDelete);

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ticketInterface.onEditButtonClicked(TicketInfoArrayList.get(getAdapterPosition()));
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ticketInterface.onDeleteButtonClick(TicketInfoArrayList.get(getAdapterPosition()));
                }
            });
        }
    }
}
