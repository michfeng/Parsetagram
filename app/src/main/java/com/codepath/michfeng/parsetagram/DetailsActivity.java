package com.codepath.michfeng.parsetagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.parceler.Parcels;

public class DetailsActivity extends AppCompatActivity {

    public TextView username;
    public TextView caption;
    public TextView date;
    public ImageView imageView;
    Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if (getIntent().getExtras() != null)
            post = (Post) getIntent().getParcelableExtra("post");
        else {
            Log.e("TAGGGG", "error");
            return;
        }

        // connect fields to views
        username = (TextView) findViewById(R.id.username);
        caption = (TextView) findViewById(R.id.caption);
        date = (TextView) findViewById(R.id.tvTimestamp);
        imageView = (ImageView) findViewById(R.id.imageView);

        username.setText(post.getUser().getUsername());
        caption.setText(post.getDescription());
        date.setText(Post.calculateTimeAgo(post.getCreatedAt()));

        Glide.with(this).load(post.getImage().getUrl()).into(imageView);
    }
}