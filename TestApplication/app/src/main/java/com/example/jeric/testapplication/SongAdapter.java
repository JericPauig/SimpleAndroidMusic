package com.example.jeric.testapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Jeric on 27/09/2014.
 */
public class SongAdapter extends BaseAdapter {

    private ArrayList<Song> songList;
    private LayoutInflater songInf;

    public SongAdapter(Context c, ArrayList<Song> songs) {
        this.songList = songs;
        this.songInf = LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return songList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //Instantiate new view
        LinearLayout songLayout = (LinearLayout) songInf.inflate(R.layout.song, viewGroup, false);
        //Get all the textviews
        TextView titleView = (TextView) songLayout.findViewById(R.id.song_title);
        TextView artistView = (TextView) songLayout.findViewById(R.id.song_artist);
        //Get the current song based on position
        Song currSong = songList.get(i);
        //Set the text in the views
        titleView.setText(currSong.getTitle());
        artistView.setText(currSong.getArtist());
        songLayout.setTag(i);
        return songLayout;
    }

}
