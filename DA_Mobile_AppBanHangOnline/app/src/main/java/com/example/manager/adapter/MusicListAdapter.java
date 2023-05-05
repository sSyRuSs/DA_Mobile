package com.example.manager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import com.example.appbanhangonline.R;
import com.example.manager.model.Contact;

public class MusicListAdapter extends BaseAdapter {
    List<Contact> musicList;
    Context mContext;
    public MusicListAdapter(Context context,List<Contact> list){
        this.musicList =  list;
        this.mContext =context;
    }


    public int getCount() {
        return musicList.size();
    }


    public Object getItem(int i) {
        return musicList.get(i);
    }


    public long getItemId(int i) {
        return i;
    }

    public View getView(int position, View view, ViewGroup parent) {
        if(view==null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.music_item, null, false);
        }

        Contact song = (Contact)getItem(position);
        TextView titleText = (TextView) view.findViewById(R.id.title);
        //TextView subtitleText = (TextView) view.findViewById(R.id.subtitle);

        titleText.setText(song.getName());
        //subtitleText.setText(song.getDetails());
        return view;

    };
}
