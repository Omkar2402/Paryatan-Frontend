package com.example.sihfrontend;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sihfrontend.user.ticketInfo;
import com.example.sihfrontend.user.ticketInterface;

import java.util.ArrayList;

public  class TicketQRAdapter extends RecyclerView.Adapter<TicketQRAdapter.Viewholder> {

    private Context context;
    private static ArrayList<ticketInfo> TicketInfoArrayList;

    // Constructor
    public TicketQRAdapter(Context context, ArrayList<ticketInfo> courseModelArrayList) {
        this.context = context;
        this.TicketInfoArrayList = courseModelArrayList;
    }
    public void updateTicketList(ArrayList<ticketInfo> arr){
        this.TicketInfoArrayList.clear();
        this.TicketInfoArrayList.addAll(arr);
    }
    @NonNull
    @Override
    public TicketQRAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ticket_qr, parent, false);
//        Viewholder viewholder = new Viewholder(view);
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ticketInterface.onCardClicked(monumentInfoArrayList.get(viewholder.getAdapterPosition()));
//            }
//        });
        return new TicketQRAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketQRAdapter.Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        ticketInfo model = TicketInfoArrayList.get(position);
        try{
            Log.d("Success:","bytes.toString()");
            String name = model.getVisitorName();
            holder.visitorName.setText(name);
            holder.age.setText(model.getAge());
            holder.gender.setText(model.getGender());
            holder.nationality.setText(model.getNationality());

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
        private TextView age;
        private TextView gender;
        private TextView nationality;



        public Viewholder(@NonNull View itemView) {
            super(itemView);
            visitorName = itemView.findViewById(R.id.tvNameQR);
            age = itemView.findViewById(R.id.tvAgeQR);
            gender = itemView.findViewById(R.id.tvGenderQR);
            nationality = itemView.findViewById(R.id.tvNationalityQR);
        }

    }
}
