package com.zoothii.parseproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.parse.FindCallback;
import com.parse.GetFileCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Tab1Feed extends Fragment {

    View view;
    Uri imageUriPost;
    Uri imageUriProfile;
    SwipeRefreshLayout refreshLayout;
    FeedRecyclerAdapter feedRecyclerAdapter;
    RecyclerView recyclerView;
    ArrayList<Post> postArrayList;
    Context myContext;
    Activity myActivity;
    String username;
    String date;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab1_feed, container, false);
        view.setBackgroundColor(Color.WHITE);
        recyclerView = view.findViewById(R.id.recyclerView);
        refreshLayout= view.findViewById(R.id.refreshLayout);
        postArrayList = new ArrayList<>();
        myContext = getContext();
        myActivity = getActivity();

        feedRecyclerAdapter = new FeedRecyclerAdapter(myContext, postArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(feedRecyclerAdapter);
        getDataFromParse();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                myActivity.finish();
                startActivity(myActivity.getIntent());
                myActivity.overridePendingTransition(0,0);
                refreshLayout.setRefreshing(false);
            }
        });
        return view;
    }

    public void getDataFromParse(){
        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Posts");
        parseQuery.orderByDescending("dateDate");
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e != null){
                    Toast.makeText(myContext, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                } else if (objects.size() > 0){
                    for (ParseObject object : objects) {
                        username = object.getString("username");
                        date = object.getString("date");
                        ParseFile parseFilePost = (ParseFile) object.get("imagePost");
                        ParseFile parseFileProfile = (ParseFile) object.get("imageProfile");
                        //ARKA PLANDA İNDİRME SIKINTILI
                        /*parseFileProfile.getFileInBackground(new GetFileCallback() {
                            @Override
                            public void done(File file, ParseException e) {
                                imageUriProfile = Uri.fromFile(file);
                            }
                        });*/
                        /*parseFilePost.getFileInBackground(new GetFileCallback() {
                            @Override
                            public void done(File file, ParseException e) {
                                imageUriPost = Uri.fromFile(file);
                            }
                        });*/
                        try {
                            File file = parseFilePost.getFile();
                            File file1 = parseFileProfile.getFile();
                            imageUriPost = Uri.fromFile(file);
                            imageUriProfile = Uri.fromFile(file1);
                        } catch (Exception exx){
                            e.printStackTrace();
                        }

                        postArrayList.add(new Post(username, date, imageUriPost, imageUriProfile));
                        feedRecyclerAdapter.notifyDataSetChanged();

                    }
                }

            }

        });

    }

}
