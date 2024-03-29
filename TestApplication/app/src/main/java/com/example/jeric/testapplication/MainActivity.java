package com.example.jeric.testapplication;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.MediaController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class MainActivity extends Activity implements MediaController.MediaPlayerControl {

    private ArrayList<Song> songList;
    private ListView songView;
    private MusicService musicService;
    private Intent playIntent;
    private boolean musicBound = false;
    private MusicController musicController;

    private void setController(){
        musicController = new MusicController(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Retrieve the List View
        songView = (ListView) findViewById(R.id.song_list);
        //Instantiate List
        songList = new ArrayList<Song>();
        getSongList();

        //Sort the songs by title
        Collections.sort(songList, new Comparator<Song>() {
            public int compare(Song a, Song b) {
                return a.getTitle().compareTo(b.getTitle());
            }
        });

        SongAdapter songAdapter = new SongAdapter(this, songList);
        songView.setAdapter(songAdapter);
    }

    private ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) iBinder;
            musicService = binder.getService();
            musicService.setSongs(songList);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            musicBound = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        if (playIntent == null) {
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                break;
            case R.id.end_play:
                stopService(playIntent);
                musicService = null;
                System.exit(0);
                break;
            case R.id.sort_by_title:
                songList = new ArrayList<Song>();
                getSongList();
                //Sort the songs by title
                Collections.sort(songList, new Comparator<Song>() {
                    public int compare(Song a, Song b) {
                        return a.getTitle().compareTo(b.getTitle());
                    }
                });
                SongAdapter songAdapter = new SongAdapter(this, songList);
                songView.setAdapter(songAdapter);
                musicService.setSongs(songList);
                break;
            case R.id.sort_by_artist:
                songList = new ArrayList<Song>();
                getSongList();

                //Sort the songs by title
                Collections.sort(songList, new Comparator<Song>() {
                    public int compare(Song a, Song b) {
                        return a.getArtist().compareTo(b.getArtist());
                    }
                });

                songAdapter = new SongAdapter(this, songList);
                songView.setAdapter(songAdapter);
                musicService.setSongs(songList);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void songPicked(View view) {
        musicService.setSong(Integer.parseInt(view.getTag().toString()));
        musicService.playSong();
    }

    public void getSongList() {
        //Get a Cursor of all songs
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

        //Find the row values in musicCursor
        if (musicCursor != null && musicCursor.moveToFirst() == true) {
            int titleColumnIndex = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int artistColumnIndex = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int idColumnIndex = musicCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int sizeColumnIndex = musicCursor.getColumnIndex(MediaStore.Audio.Media.SIZE);

            //Iterate through each and fill songList
            do {
                //Most dummy music files are under 500 bytes, so filter them out
                long songSize = musicCursor.getLong(sizeColumnIndex);
                if (songSize < 500000){
                    continue;
                }
                long songId = musicCursor.getLong(idColumnIndex);
                String songTitle = musicCursor.getString(titleColumnIndex);
                String songArtist = musicCursor.getString(artistColumnIndex);
                songList.add(new Song(songId, songTitle, songArtist));
            } while (musicCursor.moveToNext());
        }
    }

    @Override
    public void onDestroy() {
        stopService(playIntent);
        musicService = null;
        super.onDestroy();
    }

    @Override
    public void start() {

    }

    @Override
    public void pause() {

    }

    @Override
    public int getDuration() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        return 0;
    }

    @Override
    public void seekTo(int i) {

    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return false;
    }

    @Override
    public boolean canSeekBackward() {
        return false;
    }

    @Override
    public boolean canSeekForward() {
        return false;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }
}
