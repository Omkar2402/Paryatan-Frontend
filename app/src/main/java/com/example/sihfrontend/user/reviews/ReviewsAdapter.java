package com.example.sihfrontend.user.reviews;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sihfrontend.R;
import com.example.sihfrontend.user.UserMainActivity;
import com.example.sihfrontend.user.monument.MonumentDescription;
import com.example.sihfrontend.user.reviews.ReviewsInfo;

import java.util.ArrayList;

public  class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.Viewholder> {

    private ArrayList<ReviewsInfo> reviewsInfoArrayList;
    private Context context;
//    private static ArrayList<ReviewsInfo> reviewsList;
//    public  MonumentInterface monumentInterface;

    // Constructor
    public ReviewsAdapter(Context context, ArrayList<ReviewsInfo> courseModelArrayList) {
        this.context = context;
        this.reviewsInfoArrayList = courseModelArrayList;
        Log.d("Size", String.valueOf(reviewsInfoArrayList.size()));
//        this.monumentInterface = monumentInterface;
    }

    // method for filtering our recyclerview items.
//    public void filterList(ArrayList<ReviewsInfo> filterllist) {
//        // below line is to add our filtered
//        // list in our course array list.
//        reviewsList = filterllist;
//        // below line is to notify our adapter
//        // as change in recycler view data.
//        notifyDataSetChanged();
//    }

    @NonNull
    @Override
    public ReviewsAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
//        reviewsInfoArrayList = new ArrayList<>();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review_list, parent, false);
        Viewholder viewholder = new Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsAdapter.Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        ReviewsInfo model = reviewsInfoArrayList.get(position);
        holder.username.setText(""+model.getVisitorName());
        holder.review.setText(""+model.getReview());
        holder.ratingBar.setRating(0);
        holder.date.setText(""+model.getDateOfVisit());
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
        return reviewsInfoArrayList.size();
    }


    // View holder class for initializing of
    // your views such as TextView and Imageview.
    public class Viewholder extends RecyclerView.ViewHolder {
        private TextView username,date;
        private RatingBar ratingBar;
        private TextView review;



        public Viewholder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.tvName);
            date = itemView.findViewById(R.id.tvdateOfReview);
            ratingBar = itemView.findViewById(R.id.rating);
            review = itemView.findViewById(R.id.tvReview);
        }
    }
}