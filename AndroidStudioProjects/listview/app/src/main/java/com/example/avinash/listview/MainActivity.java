package com.example.avinash.listview;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

    ListView bandsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] bands = {"fob","mcr","top", "panic!", "bmth"};
        //Adapter converts array to list items
        ListAdapter bandsAdapter = new bandAdapter(this, bands);

        bandsListView = (ListView) findViewById(R.id.bandsListView);
        bandsListView.setAdapter(bandsAdapter);

        bandsListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String band = String.valueOf(parent.getItemAtPosition(position));
                        Toast.makeText(MainActivity.this, band, Toast.LENGTH_SHORT).show();
                    };
                }
        );
    }

}
