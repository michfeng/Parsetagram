package com.codepath.michfeng.parsetagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class FeedActivity extends AppCompatActivity {

    private RecyclerView rvPosts;
    protected PostsAdapter adapter;
    protected List<Post> allPosts;
    private static final String TAG = "FEED_ACTIVITY";
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        
        // look up swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer); 
                
        // setup refresh listener
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchTimelineAsync(0);
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        
        rvPosts = findViewById(R.id.rvPosts);

        // initialize list that holds posts
        allPosts = new ArrayList<>();
        adapter = new PostsAdapter(this, allPosts);

        // set adapter on recycler view
        rvPosts.setAdapter(adapter);

        // set layout manager on recycler view
        rvPosts.setLayoutManager(new LinearLayoutManager(this));

        queryPosts();
    }

    private void fetchTimelineAsync(int page) {
        adapter.clear();
        queryPosts();
        swipeContainer.setRefreshing(false);
    }

    private void queryPosts () {
        // specify which class to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);

        // include data referred by user key
        query.include(Post.KEY_USER);

        // limit query to latest 20 items
        query.setLimit(20);

        // order posts by creation date
        query.addDescendingOrder("createdAt");

        // start asynchronous call for posts
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG,"Issue with getting posts",e);
                    return;
                }
                for (Post post : posts) {
                    Log.i(TAG,"Post: "+post.getDescription()+", username: " + post.getUser().getUsername());
                }

                // save received posts
                allPosts.addAll(posts);

                adapter.notifyDataSetChanged();
            }
        });
    }
}