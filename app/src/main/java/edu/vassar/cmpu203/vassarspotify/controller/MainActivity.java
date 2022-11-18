package edu.vassar.cmpu203.vassarspotify.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.vassar.cmpu203.vassarspotify.model.Profile;
import edu.vassar.cmpu203.vassarspotify.model.ProfileDatabase;
import edu.vassar.cmpu203.vassarspotify.model.Queue;
import edu.vassar.cmpu203.vassarspotify.model.SongDatabase;
import edu.vassar.cmpu203.vassarspotify.model.Song;
import edu.vassar.cmpu203.vassarspotify.view.ILoginFragment;
import edu.vassar.cmpu203.vassarspotify.view.IPlayScreenFragment;
import edu.vassar.cmpu203.vassarspotify.view.ISearchFragment;
import edu.vassar.cmpu203.vassarspotify.view.LoginFragment;
import edu.vassar.cmpu203.vassarspotify.view.MainView;
import edu.vassar.cmpu203.vassarspotify.view.SearchFragment;


public class MainActivity extends AppCompatActivity implements ISearchFragment.Listener, ILoginFragment.Listener, IPlayScreenFragment.Listener {

    ProfileDatabase pd = new ProfileDatabase();

    SongDatabase sd = new SongDatabase();
    private MainView mainView;

    Queue q = new Queue();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mainView = new MainView(this);
        mainView.displayFragment(new LoginFragment(this), false, "login");
//        mainView.displayFragment(new SearchFragment(this),true, "search");
//        this.addItemsView = new AddItemsView(getApplicationContext(), this);
        //mainView.displayFragment(new PlayScreenFragment(this), false, "play");

        setContentView(mainView.getRootView());
    }

    @Override
    public void searchAdded(String searchText, boolean songCheck, boolean artistCheck, SearchFragment sfragment) {
        if (songCheck && !artistCheck) {
            sfragment.updateSearchDisplay(this.sd.searchSong(searchText));
        }
        else if (artistCheck && !songCheck){
            sfragment.updateSearchDisplay(this.sd.searchArtist(searchText));
        }
        else{
            List<Song> tempList = this.sd.searchArtist(searchText);
            tempList.addAll(sd.searchSong(searchText));

            Set<Song> songSet = new HashSet<Song>(tempList);
            List<Song> returnList = new ArrayList<Song>(songSet);

            sfragment.updateSearchDisplay(returnList);
        }
    }

    public Song getSongFromModel(String songName, String artistName){
        return sd.getSong(songName, artistName);
    }

    @Override
    public void LogIn(String username, String password, LoginFragment lfragment) {
        boolean hold = false;
        for (Profile p: pd.getProfiles()){
            if (p.getUsername().equalsIgnoreCase(username)){
                 if (p.checkLogin(username, password)){
                     hold = true;
                }
            }

        }
        if (hold){
            this.mainView.displayFragment(new SearchFragment(this),true, "search");
        }

        lfragment.successfullyLoggedIn(hold);

    }

    public boolean playMusic(Song s){
        return sd.play(s);
    }

    public Song nextSong(Song s){
        return q.getNext(s);
    }

    public Song previousSong(Song s){
        return q.getPrevious(s);
    }

    @Override
    public void CreateUser(String username, String password, LoginFragment lfragment) {
        Profile p = new Profile(username, password);
        pd.addProfile(p);
        lfragment.successfullyLoggedIn(true);
        this.mainView.displayFragment(new SearchFragment(this),true, "search");
    }
}