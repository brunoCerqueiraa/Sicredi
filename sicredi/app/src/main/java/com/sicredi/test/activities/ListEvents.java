package com.sicredi.test.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.sicredi.test.R;
import com.sicredi.test.model.TbEvent;

import java.util.ArrayList;
import java.util.List;

public class ListEvents extends AppCompatActivity {
    Intent intent;
    ListView lvEvents;
    List<TbEvent> listEvents = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_events);

        formElements();
        getPreviousIntent();

        MyAdapter adapterList = new MyAdapter(this, listEvents);
        lvEvents.setAdapter(adapterList);

        lvEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TbEvent tbEvent =  listEvents.get(position);
                redirectActivity(new Intent(ListEvents.this, EventDetail.class), tbEvent);
            }
        });
    }

    public void getPreviousIntent(){
        intent = getIntent();
        listEvents = (List<TbEvent>) intent.getSerializableExtra("list");
    }

    public void formElements(){
        lvEvents = findViewById(R.id.lvEvents);
    }

    public void redirectActivity(Intent intent) {
        startActivity(intent);
        ListEvents.this.finish();
    }

    public void redirectActivity(Intent intent, TbEvent tbEvent) {
        intent.putExtra("tbEvent", tbEvent);
        startActivity(intent);
        ListEvents.this.finish();
    }


    class MyAdapter extends ArrayAdapter<TbEvent> {
        Context context;
        List<TbEvent> vecTbEvent;

        MyAdapter(Context context, List<TbEvent> vecTbEvent){
            super(context, R.layout.list_event_adapter, vecTbEvent);
            this.context = context;
            this.vecTbEvent = vecTbEvent;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View styleListView = layoutInflater.inflate(R.layout.list_event_adapter, parent, false);

            TextView tvTitle = styleListView.findViewById(R.id.tvTitle);
            TextView tvPrice = styleListView.findViewById(R.id.tvPrice);

            tvTitle.setText(vecTbEvent.get(position).getTitle());
            tvPrice.setText(vecTbEvent.get(position).getPrice());

            return styleListView;
        }
    }

    @Override
    public void onBackPressed() {
        redirectActivity(new Intent(ListEvents.this, MainDrawer.class));
    }
}