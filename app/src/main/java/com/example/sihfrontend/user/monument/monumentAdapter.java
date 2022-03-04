package com.example.sihfrontend.user.monument;
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
import com.example.sihfrontend.user.UserMainActivity;

import java.util.ArrayList;

public class monumentAdapter extends RecyclerView.Adapter<monumentAdapter.Viewholder> {

    private Context context;
    private static ArrayList<monumentInfo> monumentInfoArrayList;

    // Constructor
    public monumentAdapter(Context context, ArrayList<monumentInfo> courseModelArrayList) {
        this.context = context;
        this.monumentInfoArrayList = courseModelArrayList;
    }




    @NonNull
    @Override
    public monumentAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_monument, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull monumentAdapter.Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        monumentInfo model = monumentInfoArrayList.get(position);
        try{
            Log.d("Success:","bytes.toString()");
            byte[] bytes = model.getMonumentImage();
            Log.d("Length",""+bytes.length);
            Log.d("In try","..");
            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            holder.monumentimg.setImageBitmap(bmp);

        }catch (Exception e){
            e.printStackTrace();
        }



        holder.monumentname.setText(model.getMonumentName());
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
