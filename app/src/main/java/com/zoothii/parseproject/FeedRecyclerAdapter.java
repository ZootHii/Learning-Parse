package com.zoothii.parseproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import bolts.Task;

class FeedRecyclerAdapter extends RecyclerView.Adapter<FeedRecyclerAdapter.PostHolder> {

    Context mContext;
    ArrayList<Post> postArrayList;
    int likeCounter = 0;

    public FeedRecyclerAdapter(Context mContext, ArrayList<Post> postArrayList) {
        this.mContext = mContext;
        this.postArrayList = postArrayList;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_row, parent, false);
        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FeedRecyclerAdapter.PostHolder holder, final int position) {
        holder.textViewUserNameRow.setText(postArrayList.get(position).getUsername());
        holder.textViewDateRow.setText(String.valueOf(postArrayList.get(position).getDate()));
        holder.imageViewPostRow.setImageURI(postArrayList.get(position).getImagePost());
        holder.imageViewProfileRow.setImageURI(postArrayList.get(position).getImageProfile());
        holder.imageViewDisLikeRow.setVisibility(View.INVISIBLE);
        holder.imageViewLikeRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeCounter++;
                ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
                parseQuery.whereEqualTo("username", postArrayList.get(position).getUsername());
                parseQuery.getFirstInBackground(new GetCallback<ParseUser>() {
                    @Override
                    public void done(ParseUser object, ParseException e) {
                        object.put("like",likeCounter);
                        object.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                Toast.makeText(mContext, "Liked", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                holder.imageViewLikeRow.setVisibility(View.INVISIBLE);
                holder.imageViewDisLikeRow.setVisibility(View.VISIBLE);
            }
        });
        holder.imageViewDisLikeRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeCounter--;
                ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
                ParseQuery<ParseUser> user = parseQuery.whereEqualTo("username", holder.textViewUserNameRow.getText());
                user.getFirstInBackground(new GetCallback<ParseUser>() {
                    @Override
                    public void done(ParseUser object, ParseException e) {
                        object.put("like",likeCounter);
                        object.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                Toast.makeText(mContext, "Disliked", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                holder.imageViewLikeRow.setVisibility(View.VISIBLE);
                holder.imageViewDisLikeRow.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return postArrayList.size();
    }

    class PostHolder extends RecyclerView.ViewHolder {

        ImageView imageViewPostRow, imageViewProfileRow, imageViewLikeRow, imageViewDisLikeRow;
        TextView textViewUserNameRow, textViewDateRow;

        public PostHolder(@NonNull View itemView) {
            super(itemView);
            imageViewPostRow = itemView.findViewById(R.id.imageViewPostRow);
            textViewUserNameRow = itemView.findViewById(R.id.textViewUserNameRow);
            imageViewProfileRow = itemView.findViewById(R.id.imageViewProfileRow);
            textViewDateRow = itemView.findViewById(R.id.textViewDateRow);
            imageViewLikeRow = itemView.findViewById(R.id.imageViewLikeRow);
            imageViewDisLikeRow = itemView.findViewById(R.id.imageViewDisLikeRow);
        }

    }

}
