package com.example.groupproject;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class post extends AppCompatActivity {

    private ListView mListView;
    private String[] posts;
    for(int i = 0;i < 10;i++)
        posts[i] = "Post " + i + 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        mListView = (ListView) findViewById(R.id.post_list);
        MyBaseAdapter mAdapter = new MyBaseAdapter();
        mListView.setAdapter(mAdapter);
    }
    class MyBaseAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return posts.length;
        }
        @Override
        public Object getItem(int position) {
            return posts[position];
        }
        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(post.this, R.layout.post_item, null);
            TextView mTextView = (TextView) view.findViewById(R.id.item_text);
            mTextView.setText(posts[position]);
            return view;
        }
    }
}
