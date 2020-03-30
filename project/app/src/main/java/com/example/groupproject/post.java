package com.example.groupproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.groupproject.friendlistActivity.FRIEND_NAME;

public class post extends AppCompatActivity {

    Toolbar toolbar;
    private ListView listview;
    private ArrayList<String> list = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listview = (ListView)findViewById(R.id.item);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_expandable_list_item_1,
                getData());
        listview.setAdapter(adapter);

        /* 待修改 */
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position,
                                    long id) {

                String friendName = (String)parent.getItemAtPosition(position);
                Intent intent = new Intent();
                intent.putExtra(FRIEND_NAME, friendName);
                intent.setClass(friendlistActivity.this,chat_nav.class);
                startActivity(intent);
            }

        });
    }

    private ArrayList<String> getData()
    {
        list.add("Post_1");
        list.add("Post_2");
        list.add("Post_3");
        list.add("Post_4");
        list.add("Post_5");
        list.add("Post_1");
        list.add("Post_2");
        list.add("Post_3");
        list.add("Post_4");
        list.add("Post_5");
        list.add("Post_1");
        list.add("Post_2");
        list.add("Post_3");
        list.add("Post_4");
        list.add("Post_5");
        return list;
    }
    public void toBack(View view){
        Intent intent = new Intent();
        intent.setClass(this, channel.class);
        startActivity(intent);
    }
    public void toNewPost(View view){
        Intent intent = new Intent();
        intent.setClass(this, new_post.class);
        startActivity(intent);
    }
}
